package sintatico;

import lexico.AnalisadorLexico;
import lexico.Atomo;
import lexico.CodAtomo;
import tabelas.TabelaSimbolos;

public class Controlador {

    private enum EstadoContexto {
        NEUTRO,
        LENDO_TIPO_VAR,
        LENDO_LISTA_VAR,
        LENDO_TIPO_FUNC,
        ESPERANDO_NOME_FUNC,
        LENDO_TIPO_PARAM,
        LENDO_LISTA_PARAM
    }

    private final AnalisadorLexico analisadorLexico;
    private final TabelaSimbolos tabelaSimbolos;
    private EstadoContexto estado = EstadoContexto.NEUTRO;
    private String tipoBaseAtual = null;
    private boolean declaracaoVetor = false;
    private Integer ultimoIndiceParametro = null;

    public Controlador(AnalisadorLexico analisadorLexico, TabelaSimbolos tabelaSimbolos) {
        this.analisadorLexico = analisadorLexico;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    public ResultadoAnalise executar() {
        FluxoAtomos fluxo = new FluxoAtomos();

        Atomo atomo;
        while ((atomo = analisadorLexico.proximoAtomo()) != null) {
            fluxo.adicionar(atomo);
            atualizarContextoETipos(atomo);
        }

        return new ResultadoAnalise(fluxo, tabelaSimbolos, true);
    }

    private void atualizarContextoETipos(Atomo atomo) {
        String cod = atomo.getCodigo();

        if (CodAtomo.A19.equals(cod)) {
            analisadorLexico.definirContextoIdentificador(CodAtomo.C03);
            resetarEstadoDeclaracao();
            return;
        }

        if (CodAtomo.A24.equals(cod)) {
            estado = EstadoContexto.LENDO_TIPO_VAR;
            tipoBaseAtual = null;
            declaracaoVetor = false;
            return;
        }

        if (CodAtomo.A14.equals(cod)) {
            estado = EstadoContexto.LENDO_TIPO_FUNC;
            tipoBaseAtual = null;
            declaracaoVetor = false;
            return;
        }

        if (CodAtomo.A17.equals(cod)) {
            estado = EstadoContexto.LENDO_TIPO_PARAM;
            tipoBaseAtual = null;
            declaracaoVetor = false;
            ultimoIndiceParametro = null;
            return;
        }

        if (estado == EstadoContexto.LENDO_TIPO_VAR
                || estado == EstadoContexto.LENDO_TIPO_FUNC
                || estado == EstadoContexto.LENDO_TIPO_PARAM) {

            String sigla = mapearTipoPrimitivo(cod);
            if (sigla != null) {
                tipoBaseAtual = sigla;
                return;
            }
            if (CodAtomo.B08.equals(cod)) {
                declaracaoVetor = true;
                return;
            }
            if (CodAtomo.B03.equals(cod)) {
                if (estado == EstadoContexto.LENDO_TIPO_VAR) {
                    estado = EstadoContexto.LENDO_LISTA_VAR;
                } else if (estado == EstadoContexto.LENDO_TIPO_FUNC) {
                    estado = EstadoContexto.ESPERANDO_NOME_FUNC;
                    analisadorLexico.definirContextoIdentificador(CodAtomo.C02);
                } else {
                    estado = EstadoContexto.LENDO_LISTA_PARAM;
                }
                return;
            }
        }

        if (estado == EstadoContexto.ESPERANDO_NOME_FUNC && CodAtomo.C02.equals(cod)) {
            if (tipoBaseAtual != null && atomo.getIndiceTabSimb() != null) {
                tabelaSimbolos.atualizarTipo(atomo.getIndiceTabSimb(), tipoBaseAtual);
            }
            resetarEstadoDeclaracao();
            return;
        }

        if (estado == EstadoContexto.LENDO_LISTA_VAR) {
            if (CodAtomo.C01.equals(cod) && tipoBaseAtual != null && atomo.getIndiceTabSimb() != null) {
                String siglaFinal = declaracaoVetor ? converterParaVetor(tipoBaseAtual) : tipoBaseAtual;
                tabelaSimbolos.atualizarTipo(atomo.getIndiceTabSimb(), siglaFinal);
            } else if (CodAtomo.B01.equals(cod) || CodAtomo.A06.equals(cod)) { 
                resetarEstadoDeclaracao();
            }
            return;
        }

        if (estado == EstadoContexto.LENDO_LISTA_PARAM) {
            if (CodAtomo.C01.equals(cod) && tipoBaseAtual != null && atomo.getIndiceTabSimb() != null) {
                tabelaSimbolos.atualizarTipo(atomo.getIndiceTabSimb(), tipoBaseAtual);
                ultimoIndiceParametro = atomo.getIndiceTabSimb();
            } else if (CodAtomo.B08.equals(cod) && ultimoIndiceParametro != null && tipoBaseAtual != null) {
                tabelaSimbolos.atualizarTipo(ultimoIndiceParametro, converterParaVetor(tipoBaseAtual));
            } else if (CodAtomo.B01.equals(cod) || CodAtomo.B07.equals(cod)) {
                resetarEstadoDeclaracao();
            }
        }
    }

    private void resetarEstadoDeclaracao() {
        estado = EstadoContexto.NEUTRO;
        tipoBaseAtual = null;
        declaracaoVetor = false;
        ultimoIndiceParametro = null;
    }

    private String mapearTipoPrimitivo(String codAtomo) {
        return switch (codAtomo) {
            case CodAtomo.A20 -> "FP"; // real
            case CodAtomo.A16 -> "IN"; // integer
            case CodAtomo.A22 -> "ST"; // string
            case CodAtomo.A03 -> "CH"; // character
            case CodAtomo.A01 -> "BL"; // boolean
            case CodAtomo.A25 -> "VD"; // void
            default -> null;
        };
    }

    private String converterParaVetor(String tipoBase) {
        return switch (tipoBase) {
            case "FP" -> "AF"; // Floating Point
            case "IN" -> "AI"; // Integer
            case "ST" -> "AS"; // String
            case "CH" -> "AC"; // Character
            case "BL" -> "AB"; // Boolean
            default -> tipoBase;
        };
    }
}
