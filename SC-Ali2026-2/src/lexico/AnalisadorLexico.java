package lexico;

import leitura.LeitorTextoFonte;
import tabelas.TabelaReservados;
import tabelas.TabelaSimbolos;

public class AnalisadorLexico {
    private static final int LIMITE_MAXIMO_ATOMO = 30;
    private final LeitorTextoFonte leitor;
    private final TabelaReservados tabelaReservados;
    private final TabelaSimbolos tabelaSimbolos;
    private final int[] bufferChar = new int[2];
    private final int[] bufferLinha = new int[2];
    private int qtdBuffer = 0;

    private String contextoIdentificador = CodAtomo.C01;

    public AnalisadorLexico(LeitorTextoFonte leitor,
                            TabelaReservados tabelaReservados,
                            TabelaSimbolos tabelaSimbolos) {
        this.leitor = leitor;
        this.tabelaReservados = tabelaReservados;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    public void definirContextoIdentificador(String codigoContexto) {
        this.contextoIdentificador = (codigoContexto != null) ? codigoContexto : CodAtomo.C01;
    }

    public Atomo proximoAtomo() {
        while (true) {
            int c = consultarProximoValido(0);
            if (c == -1) {
                return null;
            }

            if (isEspacoOuQuebra(c)) {
                consumirProximoValido();
                continue;
            }

            if (c == '/') {
                int segundo = consultarProximoValido(1);
                if (segundo == '/') {
                    descartarComentarioLinha();
                    continue;
                }
                if (segundo == '*') {
                    descartarComentarioBloco();
                    continue;
                }
            }

            int linhaInicio = obterLinhaValido(0);
            if (isLetra(c) || c == '_') {
                return lerIdentificadorOuReservada(linhaInicio);
            }

            if (isDigito(c)) {
                return lerNumero(linhaInicio);
            }

            if (c == '"') {
                return lerString(linhaInicio);
            }

            if (c == '\'') {
                return lerChar(linhaInicio);
            }

            Atomo simbolo = lerSimboloReservado(linhaInicio);
            if (simbolo != null) {
                return simbolo;
            }
            consumirProximoValido();
        }
    }

    private Atomo lerIdentificadorOuReservada(int linhaInicio) {
        StringBuilder bruto = new StringBuilder();
        boolean permiteSublinhado = (consultarProximoValido(0) == '_') || CodAtomo.C01.equals(contextoIdentificador);

        while (true) {
            int c = consultarProximoValido(0);
            if (c == -1) break;

            boolean validoNoPadrao = isLetra(c) || isDigito(c) || (permiteSublinhado && c == '_');
            if (!validoNoPadrao) break;

            bruto.append(Character.toUpperCase((char) consumirProximoValido()));
        }

        int tamanhoOriginal = bruto.length();
        String lexemeTruncado = (tamanhoOriginal > LIMITE_MAXIMO_ATOMO)
                ? bruto.substring(0, LIMITE_MAXIMO_ATOMO)
                : bruto.toString();

        if (tamanhoOriginal <= LIMITE_MAXIMO_ATOMO) {
            String codReservado = tabelaReservados.buscarCodigo(lexemeTruncado);
            if (codReservado != null) {
                return new Atomo(lexemeTruncado, codReservado, null, linhaInicio);
            }
        }

        String codIdentificador = (lexemeTruncado.charAt(0) == '_') ? CodAtomo.C01 : contextoIdentificador;
        contextoIdentificador = CodAtomo.C01; 

        int indice = tabelaSimbolos.registrar(codIdentificador, lexemeTruncado, tamanhoOriginal, linhaInicio);
        return new Atomo(lexemeTruncado, codIdentificador, indice, linhaInicio);
    }

    private Atomo lerNumero(int linhaInicio) {
        StringBuilder bruto = new StringBuilder();
        String codigo = CodAtomo.C06;

        while (isDigito(consultarProximoValido(0))) {
            bruto.append((char) consumirProximoValido());
        }

        if (consultarProximoValido(0) == '.' && isDigito(consultarProximoValido(1))) {
            codigo = CodAtomo.C07; // realConst
            bruto.append((char) consumirProximoValido());
            while (isDigito(consultarProximoValido(0))) {
                bruto.append((char) consumirProximoValido());
            }

            int exp = consultarProximoValido(0);
            if (exp == 'e' || exp == 'E') {
                int prox = consultarProximoValido(1);
                if (isDigito(prox)) {
                    bruto.append(Character.toUpperCase((char) consumirProximoValido()));
                    while (isDigito(consultarProximoValido(0))) {
                        bruto.append((char) consumirProximoValido());
                    }
                } else if (prox == '+' || prox == '-') {
                    bruto.append(Character.toUpperCase((char) consumirProximoValido())); // 'E'
                    bruto.append((char) consumirProximoValido()); // '+' ou '-'
                    while (isDigito(consultarProximoValido(0))) {
                        bruto.append((char) consumirProximoValido());
                    }
                }
            }
        }

        int tamanhoOriginal = bruto.length();
        String lexemeTruncado = (tamanhoOriginal > LIMITE_MAXIMO_ATOMO)
                ? bruto.substring(0, LIMITE_MAXIMO_ATOMO)
                : bruto.toString();

        if (tamanhoOriginal > LIMITE_MAXIMO_ATOMO) {
            while (!lexemeTruncado.isEmpty()) {
                char ultimo = lexemeTruncado.charAt(lexemeTruncado.length() - 1);
                if (ultimo == '.' || ultimo == 'E' || ultimo == '+' || ultimo == '-') {
                    lexemeTruncado = lexemeTruncado.substring(0, lexemeTruncado.length() - 1);
                } else {
                    break;
                }
            }
            if (!lexemeTruncado.contains(".")) {
                codigo = CodAtomo.C06;
            }
        }

        int indice = tabelaSimbolos.registrar(codigo, lexemeTruncado, tamanhoOriginal, linhaInicio);
        return new Atomo(lexemeTruncado, codigo, indice, linhaInicio);
    }

    private Atomo lerString(int linhaInicio) {
        StringBuilder bruto = new StringBuilder();
        bruto.append((char) consumirProximoValido());

        while (true) {
            int c = consultarProximoValido(0);
            if (c == -1 || c == '\n' || c == '\r') {
                break;
            }
            if (c == '"') {
                bruto.append((char) consumirProximoValido());
                break;
            }
            if (isLetra(c) || c == ' ' || isDigito(c) || c == '$' || c == '_' || c == '.') {
                bruto.append(Character.toUpperCase((char) consumirProximoValido()));
            } else {
                break;
            }
        }

        if (bruto.charAt(bruto.length() - 1) != '"') {
            bruto.append('"');
        }

        int tamanhoOriginal = bruto.length();
        String lexemeTruncado;
        if (tamanhoOriginal > LIMITE_MAXIMO_ATOMO) {
            lexemeTruncado = bruto.substring(0, LIMITE_MAXIMO_ATOMO - 1) + "\"";
        } else {
            lexemeTruncado = bruto.toString();
        }

        int indice = tabelaSimbolos.registrar(CodAtomo.C04, lexemeTruncado, tamanhoOriginal, linhaInicio);
        return new Atomo(lexemeTruncado, CodAtomo.C04, indice, linhaInicio);
    }

    private Atomo lerChar(int linhaInicio) {
        StringBuilder bruto = new StringBuilder();
        bruto.append((char) consumirProximoValido());

        int c = consultarProximoValido(0);
        if (isLetra(c)) {
            bruto.append(Character.toUpperCase((char) consumirProximoValido()));
        }
        if (consultarProximoValido(0) == '\'') {
            bruto.append((char) consumirProximoValido());
        } else {
            bruto.append('\'');
        }

        String lexeme = bruto.toString();
        int indice = tabelaSimbolos.registrar(CodAtomo.C05, lexeme, lexeme.length(), linhaInicio);
        return new Atomo(lexeme, CodAtomo.C05, indice, linhaInicio);
    }

    private Atomo lerSimboloReservado(int linhaInicio) {
        int c1 = consultarProximoValido(0);
        int c2 = consultarProximoValido(1);

        if (c2 != -1) {
            String duplo = "" + (char) c1 + (char) c2;
            String codDuplo = tabelaReservados.buscarCodigo(duplo);
            if (codDuplo != null) {
                consumirProximoValido();
                consumirProximoValido();
                return new Atomo(duplo, codDuplo, null, linhaInicio);
            }
        }

        String simples = String.valueOf((char) c1);
        String codSimples = tabelaReservados.buscarCodigo(simples);
        if (codSimples != null) {
            consumirProximoValido();
            return new Atomo(simples, codSimples, null, linhaInicio);
        }

        return null;
    }

    private void descartarComentarioLinha() {
        consumirProximoBruto();
        consumirProximoBruto();
        while (true) {
            int c = consultarProximoBruto(0);
            if (c == -1 || c == '\n') {
                break;
            }
            consumirProximoBruto();
        }
    }

    private void descartarComentarioBloco() {
        consumirProximoBruto();
        consumirProximoBruto();
        while (true) {
            int c = consumirProximoBruto();
            if (c == -1) {
                break;
            }
            if (c == '*' && consultarProximoBruto(0) == '/') {
                consumirProximoBruto();
                break;
            }
        }
    }

    private int consultarProximoValido(int indice) {
        while (qtdBuffer <= indice) {
            int linhaAntes = leitor.getLinhaAtual();
            int c = leitor.proximoChar();
            if (c == -1) {
                bufferChar[qtdBuffer] = -1;
                bufferLinha[qtdBuffer] = linhaAntes;
                qtdBuffer++;
                break;
            }
            if (!isCaractereValidoLinguagem(c)) {
                continue;
            }
            bufferChar[qtdBuffer] = c;
            bufferLinha[qtdBuffer] = linhaAntes;
            qtdBuffer++;
        }
        return (indice < qtdBuffer) ? bufferChar[indice] : -1;
    }

    private int obterLinhaValido(int indice) {
    	consultarProximoValido(indice);
        return (indice < qtdBuffer) ? bufferLinha[indice] : leitor.getLinhaAtual();
    }

    private int consumirProximoValido() {
        int c = consultarProximoValido(0);
        if (qtdBuffer > 0) {
            bufferChar[0] = bufferChar[1];
            bufferLinha[0] = bufferLinha[1];
            qtdBuffer--;
        }
        return c;
    }

    private int consultarProximoBruto(int indice) {
        while (qtdBuffer <= indice) {
            int linhaAntes = leitor.getLinhaAtual();
            int c = leitor.proximoChar();
            bufferChar[qtdBuffer] = c;
            bufferLinha[qtdBuffer] = linhaAntes;
            qtdBuffer++;
            if (c == -1) break;
        }
        return (indice < qtdBuffer) ? bufferChar[indice] : -1;
    }

    private int consumirProximoBruto() {
        int c = consultarProximoBruto(0);
        if (qtdBuffer > 0) {
            bufferChar[0] = bufferChar[1];
            bufferLinha[0] = bufferLinha[1];
            qtdBuffer--;
        }
        return c;
    }

    private boolean isCaractereValidoLinguagem(int c) {
        if (isLetra(c) || isDigito(c) || isEspacoOuQuebra(c)) {
            return true;
        }
        return switch (c) {
            case ';', ',', ':', '=', '?', '(', ')', '[', ']', '{', '}', '+', '-', '*', '/', '%', '!', '#', '<', '>', '_', '.', '$', '"', '\'' -> true;
            default -> false;
        };
    }

    private boolean isLetra(int c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigito(int c) {
        return c >= '0' && c <= '9';
    }

    private boolean isEspacoOuQuebra(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
}