package relatorio;

import java.util.List;

public class CabecalhoRelatorio {

    public record Integrante(String nome, String email, String telefone) {
    }

    private final String codigoEquipe;
    private final List<Integrante> integrantes;

    public CabecalhoRelatorio(String codigoEquipe, List<Integrante> integrantes) {
        this.codigoEquipe = codigoEquipe;
        this.integrantes = List.copyOf(integrantes);
    }

    public static CabecalhoRelatorio padraoEquipe() {
        return new CabecalhoRelatorio(
            "EQ01",
            List.of(
                new Integrante("Ícaro Matos Silva", "icaromatos.silva@ucsal.edu.br", "(71) 99186-1206"),
                new Integrante("Luiza Gabriella Dos Santos Santos", "luizagabriella.santos@ucsal.edu.br", "(71) 99929-0590"),
                new Integrante("Maria Eduarda Candeias Miranda", "mariaeduardacandeias.miranda@ucsal.edu.br", "(71) 98266-5786"),
                new Integrante("Thársyla Jones Luz De Andrade", "tharsyla.andrade@ucsal.edu.br", "(71) 98202-0582")
            )
        );
    }

    public String formatar(String tituloRelatorio, String nomeArquivoFonte) {
        StringBuilder sb = new StringBuilder();
        sb.append("Código da Equipe: ").append(codigoEquipe).append(System.lineSeparator());
        sb.append("Componentes:").append(System.lineSeparator());

        for (Integrante membro : integrantes) {
            sb.append(membro.nome()).append("; ")
              .append(membro.email()).append("; ")
              .append(membro.telefone()).append(System.lineSeparator());
        }

        sb.append(tituloRelatorio).append(System.lineSeparator());
        sb.append("Texto fonte analisado: ").append(nomeArquivoFonte).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
