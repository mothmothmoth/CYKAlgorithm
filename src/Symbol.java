import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

// EPSILON ε is a constant symbol, the only symbol that is an epsilon; this is used for removing symbols
// non-terminal symbols have to start with an uppercase number and can be followed by any amount of numbers
// terminal symbols can be any single non-uppercase character
public class Symbol {
    public static final Symbol EPSILON = new Symbol();

    public final String symbol;
    public final boolean terminal;
    public final boolean epsilon;

    public Symbol() {
        symbol = "ε";
        terminal = false;
        epsilon = true;
    }

    public Symbol(String symbol) {
        this.symbol = symbol;
        terminal = symbol.length() == 1 && !Character.isUpperCase(symbol.charAt(0));
        epsilon = false;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Symbol symbolToCompare)) return false;
        return symbol.equals(symbolToCompare.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    public static Symbol[] Lex(GrammarCNF grammar, String symbols) {
        String[] symbolStrings = Pattern.compile("[A-Z]\\d*|[^A-Z]") // separates non-terminals & terminals
                .matcher(symbols.replace(" ", "")) // remove spaces for easier lexing
                .results().map(MatchResult::group).toArray(String[]::new);
        Symbol[] symbolArray = new Symbol[symbolStrings.length];
        boolean terminalExists = false;
        boolean nonTerminalExists = false;


        for (int i = 0; i < symbolArray.length; i++) {
            if (symbolStrings[i].charAt(0) == 'ε') { // epsilon is a special symbol, needs special treatment
                symbolArray[i] = EPSILON;
                break;
            }
            Symbol symbol = new Symbol(symbolStrings[i]);
            int match;

            // if the symbol (non-terminal or terminal) exists already in the grammar, use that object
            // for this return; else make a new symbol and add it to the grammar, returning it
            if (Character.isUpperCase(symbolStrings[i].charAt(0))) {
                match = grammar.containsNonTerminal(symbol);
                if (match == -1)
                    grammar.nonTerminals.add(symbol);
                symbolArray[i] = match == -1 ? symbol : grammar.nonTerminals.get(match);

                nonTerminalExists = true;
            } else {
                match = grammar.containsTerminal(symbol);
                if (match == -1)
                    grammar.terminals.add(symbol);
                symbolArray[i] = match == -1 ? symbol : grammar.terminals.get(match);
                terminalExists = true;
            }
        }

        if (terminalExists && nonTerminalExists)
            throw new RuntimeException("Terminal & non-terminal in " + symbols);
        return symbolArray;
    }

    public boolean represents(String symbol) {
        return this.symbol.equals(symbol);
    }
}
