package sintatico;

import tabelas.TabelaSimbolos;

public class ResultadoAnalise {

    private final FluxoAtomos fluxoAtomos;
    private final TabelaSimbolos tabelaSimbolos;
    private final boolean concluida;
    public ResultadoAnalise(FluxoAtomos fluxoAtomos, TabelaSimbolos tabelaSimbolos, boolean concluida) {
        this.fluxoAtomos = fluxoAtomos;
        this.tabelaSimbolos = tabelaSimbolos;
        this.concluida = concluida;
    }

    public FluxoAtomos getFluxoAtomos() {
        return fluxoAtomos;
    }

    public TabelaSimbolos getTabelaSimbolos() {
        return tabelaSimbolos;
    }

    public boolean isConcluida() {
        return concluida;
    }
}
