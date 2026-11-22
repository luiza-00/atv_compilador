package relatorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import tabelas.EntradaSimbolo;
import tabelas.TabelaSimbolos;

public class ReTabela {

    private static final String TITULO = "RELATÓRIO DA TABELA DE SÍMBOLOS";

    private final CabecalhoRelatorio cabecalho;

    public ReTabela(CabecalhoRelatorio cabecalho) {
        this.cabecalho = cabecalho;
    }

    public void gerar(Path caminhoSaidaTab, String nomeArquivoFonte, TabelaSimbolos tabelaSimbolos) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append(cabecalho.formatar(TITULO, nomeArquivoFonte));

        for (EntradaSimbolo entrada : tabelaSimbolos.listarEntradas()) {
            String linhasFormatadas = entrada.getLinhas().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ", "(", ")"));

            conteudo.append("Entrada: ").append(entrada.getNumeroEntrada())
                    .append(", Código: ").append(entrada.getCodigoAtomo())
                    .append(", Lexeme: ").append(entrada.getLexeme())
                    .append(", QtdCharsAntesTrunc: ").append(entrada.getQtdCharsAntesTrunc())
                    .append(", QtdCharDepoisTrunc: ").append(entrada.getQtdCharDepoisTrunc())
                    .append(", TipoSimb: ").append(entrada.getTipoSimbolo())
                    .append(", Linhas: ").append(linhasFormatadas)
                    .append(".").append(System.lineSeparator());
        }

        Files.writeString(caminhoSaidaTab, conteudo.toString(), StandardCharsets.UTF_8);
    }
}
