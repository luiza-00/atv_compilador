package relatorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lexico.Atomo;

public class ReLexico {

    private static final String TITULO = "RELATÓRIO DA ANÁLISE LÉXICA";
    private final CabecalhoRelatorio cabecalho;

    public ReLexico(CabecalhoRelatorio cabecalho) {
        this.cabecalho = cabecalho;
    }

    public void gerar(Path caminhoSaidaLex, String nomeArquivoFonte, List<Atomo> atomos) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append(cabecalho.formatar(TITULO, nomeArquivoFonte));
        for (Atomo atomo : atomos) {
            String indice = (atomo.getIndiceTabSimb() != null)
                    ? String.valueOf(atomo.getIndiceTabSimb())
                    : "";

            conteudo.append("Lexeme: ").append(atomo.getLexeme())
                    .append(", Código: ").append(atomo.getCodigo())
                    .append(", indiceTabSimb: ").append(indice)
                    .append(", Linha: ").append(atomo.getLinha())
                    .append(".").append(System.lineSeparator());
        }
        Files.writeString(caminhoSaidaLex, conteudo.toString(), StandardCharsets.UTF_8);
    }
}
