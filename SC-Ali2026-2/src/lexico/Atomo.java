package lexico;

public class Atomo {

    private final String lexeme;
    private final String codigo;
    private final Integer indiceTabSimb;
    private final int linha;

    public Atomo(String lexeme, String codigo, Integer indiceTabSimb, int linha) {
        this.lexeme = lexeme;
        this.codigo = codigo;
        this.indiceTabSimb = indiceTabSimb;
        this.linha = linha;
    }

    public Atomo(String lexeme, String codigo, int linha) {
        this(lexeme, codigo, null, linha);
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getIndiceTabSimb() {
        return indiceTabSimb;
    }

    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "Atomo[lexeme=" + lexeme + ", codigo=" + codigo
                + ", indiceTabSimb=" + indiceTabSimb + ", linha=" + linha + "]";
    }
}
