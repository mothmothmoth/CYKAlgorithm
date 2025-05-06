import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// usually 1 letter, but it can have a number. examples: a, B, C3
// terminals will always be lowercase!
public class Symbol {
    public final String symbol;
    public final boolean terminal;

    public Symbol(String symbol, boolean isTerminal) {
        this.symbol = symbol;
        terminal = isTerminal;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static Symbol[] Lex(GrammarCNF grammar, String symbols) {
        List<Symbol> symList = new ArrayList<>();
        Symbol[] symArray;

        symList.addAll(matchFromList(grammar.terminals, symbols));
        symList.addAll(matchFromList(grammar.nonTerminals, symbols));

        if (symList.size() > 1) {
            boolean terminalExists = false;
            boolean nonTerminalExists = false;

            for (Symbol sym : symList) { // check for terminals mixed with non-terminals
                if (sym.terminal) {
                    terminalExists = true;
                } else {
                    nonTerminalExists = true;
                }
            }

            if (terminalExists && nonTerminalExists)
                throw new RuntimeException("Cannot mix terminals & non-terminals in " + symbols);
        }

        symArray = new Symbol[symList.size()];
        symList.toArray(symArray);
        return symArray;
    }

    private static List<Symbol> matchFromList(List<Symbol> list, String symbols) {
        List<Symbol> symList = new ArrayList<>();

        int prevIndex = -1;
        for (Symbol symbol : list) {
            int index;

            do {
                index = symbols.indexOf(symbol.symbol);

                if (index != -1) {
                    if (index >= prevIndex) {
                        symList.addLast(symbol);
                    } else {
                        symList.addFirst(symbol);
                    }
                    prevIndex = index;
                    symbols = symbols.replaceFirst(Pattern.quote(symbol.symbol),
                            "?".repeat(symbol.symbol.length()));
                }
            } while (index != -1);

            if (symbols.trim().isEmpty()) {
                break;
            }
        }

        return symList;
    }
}
