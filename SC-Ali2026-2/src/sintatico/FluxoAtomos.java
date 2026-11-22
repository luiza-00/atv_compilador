package sintatico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lexico.Atomo;

public class FluxoAtomos {

    private final List<Atomo> atomos = new ArrayList<>();

    public void adicionar(Atomo atomo) {
        if (atomo != null) {
            atomos.add(atomo);
        }
    }

    public List<Atomo> listarTodos() {
        return Collections.unmodifiableList(atomos);
    }

    public int tamanho() {
        return atomos.size();
    }

    public boolean isVazio() {
        return atomos.isEmpty();
    }
}
