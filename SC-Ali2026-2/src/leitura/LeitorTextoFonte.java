package leitura;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LeitorTextoFonte {

    private final String conteudo;
    private int posicao = 0;
    private int linhaAtual = 1;

    public LeitorTextoFonte(Path caminhoArquivo) throws IOException {
        this.conteudo = Files.readString(caminhoArquivo, StandardCharsets.ISO_8859_1);
    }

    public int proximoChar() {
        if (posicao >= conteudo.length()) {
            return -1;
        }
        char c = conteudo.charAt(posicao++);
        if (c == '\n') {
            linhaAtual++;
        }
        return c;
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }
}