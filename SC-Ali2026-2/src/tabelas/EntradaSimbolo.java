package tabelas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntradaSimbolo {
    private final int numeroEntrada;
    private final String codigoAtomo;
    private final String lexeme;
    private int qtdCharsAntesTrunc;
    private int qtdCharDepoisTrunc;
    private String tipoSimbolo;
    private final List<Integer> linhas;

    EntradaSimbolo(int numeroEntrada, String codigoAtomo, String lexeme, int qtdCharsAntesTrunc, int linhaInicial) {
        this.numeroEntrada = numeroEntrada;
        this.codigoAtomo = codigoAtomo;
        this.lexeme = lexeme;
        this.qtdCharsAntesTrunc = qtdCharsAntesTrunc;
        this.qtdCharDepoisTrunc = lexeme.length();
        this.tipoSimbolo = "-";
        this.linhas = new ArrayList<>(5);
        this.linhas.add(linhaInicial);
    }

    void registrarOcorrencia(int linha, int tamanhoOriginal) {
        if (this.linhas.size() < 5) {
            this.linhas.add(linha);
        }
        if (tamanhoOriginal > this.qtdCharsAntesTrunc) {
            this.qtdCharsAntesTrunc = tamanhoOriginal;
        }
    }

    void definirTipo(String tipoSimbolo) {
        if (tipoSimbolo != null && !tipoSimbolo.isBlank()) {
            this.tipoSimbolo = tipoSimbolo;
        }
    }

    public int getNumeroEntrada() {
        return numeroEntrada;
    }

    public String getCodigoAtomo() {
        return codigoAtomo;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getQtdCharsAntesTrunc() {
        return qtdCharsAntesTrunc;
    }

    public int getQtdCharDepoisTrunc() {
        return qtdCharDepoisTrunc;
    }

    public String getTipoSimbolo() {
        return tipoSimbolo;
    }

    public List<Integer> getLinhas() {
        return Collections.unmodifiableList(linhas);
    }
}
