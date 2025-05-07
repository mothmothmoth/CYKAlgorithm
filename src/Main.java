import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String grammar = """
                S → T T | U
                T → Z T1 | T Z1 | Y
                T1 → T
                Z → 0
                Z1 → Z
                Y → #
                U → Z U1 | Y
                U1 → U W
                W → 0 0
                """;
        GrammarCNF cnf = GrammarCNF.Lex(grammar);
        Symbol zero = cnf.getTerminalFromString("0");
        List<Symbol[]> symbolsFrom = cnf.symbolsFromSymbol(cnf.getNonTerminalFromString("T"));
        List<Symbol> symbolsTo = cnf.symbolsToSymbol(new Symbol[]{zero, zero});

        System.out.println(cnf);
        for (Symbol[] symbols : symbolsFrom) {
            System.out.print(Arrays.toString(symbols));
        }
        System.out.println("\n" + symbolsTo);
    }
}
