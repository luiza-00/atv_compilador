package principal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import leitura.LeitorTextoFonte;
import lexico.AnalisadorLexico;
import relatorio.CabecalhoRelatorio;
import relatorio.ReLexico;
import relatorio.ReTabela;
import sintatico.Controlador;
import sintatico.ResultadoAnalise;
import tabelas.TabelaReservados;
import tabelas.TabelaSimbolos;

public class Main {

    private static final String EXTENSAO_FONTE = ".262";
    private static final String EXTENSAO_LEX = ".LEX";
    private static final String EXTENSAO_TAB = ".TAB";

    public static void main(String[] args) {
        String parametroEntrada = (args.length > 0 && !args[0].isBlank())
                ? args[0].trim()
                : solicitarNomeArquivo();

        if (parametroEntrada.isEmpty()) {
            System.err.println("Erro: nenhum arquivo fonte foi informado.");
            return;
        }

        String caminhoSemExtensao = parametroEntrada.toLowerCase().endsWith(EXTENSAO_FONTE)
                ? parametroEntrada.substring(0, parametroEntrada.length() - EXTENSAO_FONTE.length())
                : parametroEntrada;

        Path arquivoFonte = Paths.get(caminhoSemExtensao + EXTENSAO_FONTE)
                .toAbsolutePath()
                .normalize();

        if (!Files.exists(arquivoFonte) || !Files.isRegularFile(arquivoFonte)) {
            System.err.println("Erro: arquivo fonte não encontrado em " + arquivoFonte);
            return;
        }

        try {
            TabelaReservados tabelaReservados = new TabelaReservados();
            TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();
            LeitorTextoFonte leitor = new LeitorTextoFonte(arquivoFonte);

            AnalisadorLexico analisadorLexico = new AnalisadorLexico(leitor, tabelaReservados, tabelaSimbolos);
            Controlador controlador = new Controlador(analisadorLexico, tabelaSimbolos);
            ResultadoAnalise resultado = controlador.executar();

            Path pastaOrigem = arquivoFonte.getParent();
            String nomeArquivoCompleto = arquivoFonte.getFileName().toString();
            String nomeBase = nomeArquivoCompleto.substring(0, nomeArquivoCompleto.length() - EXTENSAO_FONTE.length());

            Path arquivoSaidaLex = pastaOrigem.resolve(nomeBase + EXTENSAO_LEX);
            Path arquivoSaidaTab = pastaOrigem.resolve(nomeBase + EXTENSAO_TAB);

            CabecalhoRelatorio cabecalho = CabecalhoRelatorio.padraoEquipe();
            ReLexico escritorLex = new ReLexico(cabecalho);
            ReTabela escritorTab = new ReTabela(cabecalho);

            escritorLex.gerar(arquivoSaidaLex, nomeArquivoCompleto, resultado.getFluxoAtomos().listarTodos());
            escritorTab.gerar(arquivoSaidaTab, nomeArquivoCompleto, resultado.getTabelaSimbolos());

            System.out.println("Análise concluída com sucesso.");
            System.out.println("Relatório léxico gerado em: " + arquivoSaidaLex);
            System.out.println("Tabela de símbolos gerada em: " + arquivoSaidaTab);

        } catch (IOException e) {
            System.err.println("Erro de entrada/saída ao processar o arquivo: " + e.getMessage());
        }
    }

    private static String solicitarNomeArquivo() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Informe o nome (ou caminho) do arquivo fonte sem a extensão .262: ");
            return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        }
    }
}
