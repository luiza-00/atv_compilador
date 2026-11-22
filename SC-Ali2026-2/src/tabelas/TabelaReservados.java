package tabelas;

import java.util.Map;
import lexico.CodAtomo;

public class TabelaReservados {
    private final Map<String, String> reservados;
    public TabelaReservados() {
        this.reservados = Map.ofEntries(
            Map.entry("BOOLEAN", CodAtomo.A01),
            Map.entry("BREAK", CodAtomo.A02),
            Map.entry("CHARACTER", CodAtomo.A03),
            Map.entry("DECLARATIONS", CodAtomo.A04),
            Map.entry("ELSE", CodAtomo.A05),
            Map.entry("ENDDECLARATIONS", CodAtomo.A06),
            Map.entry("ENDFUNCTION", CodAtomo.A07),
            Map.entry("ENDFUNCTIONS", CodAtomo.A08),
            Map.entry("ENDIF", CodAtomo.A09),
            Map.entry("ENDPROGRAM", CodAtomo.A10),
            Map.entry("ENDWHILE", CodAtomo.A11),
            Map.entry("FALSE", CodAtomo.A12),
            Map.entry("FUNCTIONS", CodAtomo.A13),
            Map.entry("FUNCTYPE", CodAtomo.A14),
            Map.entry("IF", CodAtomo.A15),
            Map.entry("INTEGER", CodAtomo.A16),
            Map.entry("PARAMTYPE", CodAtomo.A17),
            Map.entry("PRINT", CodAtomo.A18),
            Map.entry("PROGRAM", CodAtomo.A19),
            Map.entry("REAL", CodAtomo.A20),
            Map.entry("RETURN", CodAtomo.A21),
            Map.entry("STRING", CodAtomo.A22),
            Map.entry("TRUE", CodAtomo.A23),
            Map.entry("VARTYPE", CodAtomo.A24),
            Map.entry("VOID", CodAtomo.A25),
            Map.entry("WHILE", CodAtomo.A26),
            Map.entry(";", CodAtomo.B01),
            Map.entry(",", CodAtomo.B02),
            Map.entry(":", CodAtomo.B03),
            Map.entry(":=", CodAtomo.B04),
            Map.entry("?", CodAtomo.B05),
            Map.entry("(", CodAtomo.B06),
            Map.entry(")", CodAtomo.B07),
            Map.entry("[", CodAtomo.B08),
            Map.entry("]", CodAtomo.B09),
            Map.entry("{", CodAtomo.B10),
            Map.entry("}", CodAtomo.B11),
            Map.entry("+", CodAtomo.B12),
            Map.entry("-", CodAtomo.B13),
            Map.entry("*", CodAtomo.B14),
            Map.entry("/", CodAtomo.B15),
            Map.entry("%", CodAtomo.B16),
            Map.entry("==", CodAtomo.B17),
            Map.entry("!=", CodAtomo.B18),
            Map.entry("#", CodAtomo.B18),
            Map.entry("<", CodAtomo.B19),
            Map.entry("<=", CodAtomo.B20),
            Map.entry(">", CodAtomo.B21),
            Map.entry(">=", CodAtomo.B22)
        );
    }

    public String buscarCodigo(String lexeme) {
        if (lexeme == null) {
            return null;
        }
        return reservados.get(lexeme.toUpperCase());
    }
}
