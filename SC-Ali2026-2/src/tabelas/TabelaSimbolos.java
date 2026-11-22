package tabelas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lexico.CodAtomo;

public class TabelaSimbolos {

    private static final Set<String> TIPOS_VALIDOS = Set.of(
        "FP", "IN", "ST", "CH", "BL", "VD", "AF", "AI", "AS", "AC", "AB"
    );

    private final Map<String, EntradaSimbolo> indicePorLexeme = new LinkedHashMap<>();
    private final List<EntradaSimbolo> entradas = new ArrayList<>();
    public int registrar(String codigoAtomo, String lexemeTruncado, int tamanhoOriginal, int linha) {
        String chave = lexemeTruncado.toUpperCase();
        EntradaSimbolo existente = indicePorLexeme.get(chave);

        if (existente != null) {
            existente.registrarOcorrencia(linha, tamanhoOriginal);
            return existente.getNumeroEntrada();
        }

        int novoIndice = entradas.size() + 1;
        EntradaSimbolo novaEntrada = new EntradaSimbolo(
            novoIndice, codigoAtomo, chave, tamanhoOriginal, linha
        );

        String tipoConstante = inferirTipoConstante(codigoAtomo);
        if (tipoConstante != null) {
            novaEntrada.definirTipo(tipoConstante);
        }

        indicePorLexeme.put(chave, novaEntrada);
        entradas.add(novaEntrada);
        return novoIndice;
    }

    public void atualizarTipo(int numeroEntrada, String siglaTipo) {
        if (!TIPOS_VALIDOS.contains(siglaTipo)) {
            throw new IllegalArgumentException("Sigla de tipo inválida: " + siglaTipo);
        }
        EntradaSimbolo entrada = buscarPorEntrada(numeroEntrada);
        if (entrada != null) {
            entrada.definirTipo(siglaTipo);
        }
    }

    public EntradaSimbolo buscarPorEntrada(int numeroEntrada) {
        if (numeroEntrada >= 1 && numeroEntrada <= entradas.size()) {
            return entradas.get(numeroEntrada - 1);
        }
        return null;
    }

    public List<EntradaSimbolo> listarEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    private String inferirTipoConstante(String codigoAtomo) {
        return switch (codigoAtomo) {
            case CodAtomo.C04 -> "ST"; // stringConst
            case CodAtomo.C05 -> "CH"; // charConst
            case CodAtomo.C06 -> "IN"; // intConst
            case CodAtomo.C07 -> "FP"; // realConst
            default -> null;
        };
    }
}
