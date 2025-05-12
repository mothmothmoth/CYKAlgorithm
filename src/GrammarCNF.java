import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GrammarCNF {
    public final List<Symbol> terminals = new ArrayList<>();
    public final List<Symbol> nonTerminals = new ArrayList<>();

    private final RuleMap rules = new RuleMap();

    public static String ImportGrammar(String filePath) throws IOException {
        byte[] file = Files.readAllBytes(Paths.get(filePath));
        return new String(file, StandardCharsets.UTF_8);
    }

    public static GrammarCNF Lex(String grammar) {
        GrammarCNF gramFin = new GrammarCNF();
        List<String> rules = grammar.lines().toList();

        // lexes each rule; terminals & nonTerminals are filled by the rule & symbol lexers
        for (String line : rules) {
            Rule rule = Rule.Lex(gramFin, line);
            gramFin.rules.add(rule);
        }

        return gramFin;
    }

    public Symbol getTerminalFromString(String string) {
        for (Symbol symbol : terminals) {
            if (symbol.represents(string)) {
                return symbol;
            }
        }
        throw new RuntimeException("Symbol " + string + " does not exist as a terminal");
    }

    // locates and returns the starting symbol; either S0 or S
    public Symbol getStart() {
        try {
            return getNonTerminalFromString("S0");
        } catch (RuntimeException e) {
            return getNonTerminalFromString("S");
        }
    }

    public Symbol getNonTerminalFromString(String string) {
        for (Symbol symbol : nonTerminals) {
            if (symbol.represents(string)) {
                return symbol;
            }
        }
        throw new RuntimeException("Symbol " + string + " does not exist as a non-terminal");
    }

    public List<Symbol[]> symbolsFromSymbol(Symbol symbol) {
        return rules.symbolsFromSymbol(symbol);
    }

    public List<Symbol> symbolsToSymbol(Symbol[] symbolSet) {
        return rules.symbolsToSymbol(symbolSet);
    }

    public int containsTerminal(Symbol terminalMatch) {
        for (int i = 0; i < terminals.size(); i++) {
            if (terminals.get(i).symbol.equals(terminalMatch.symbol)) {
                return i;
            }
        }
        return -1;
    }

    public int containsNonTerminal(Symbol nonTerminalMatch) {
        for (int i = 0; i < nonTerminals.size(); i++) {
            if (nonTerminals.get(i).symbol.equals(nonTerminalMatch.symbol)) {
                return i;
            }
        }
        return -1;
    }

    // Method to convert input string to an array of Symbols
    public Symbol[] stringToSymbolArray (String input) {
        Symbol[] result = new Symbol[input.length()];
        for (int i = 0; i < input.length(); i++) {
            String temp = String.valueOf(input.charAt(i));
            result[i] = getTerminalFromString(temp);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Rule> rules = this.rules.getRules();

        sb.append("V = ").append(nonTerminals).append("\n");
        sb.append("Σ = ").append(terminals).append("\n\n");
        sb.append("Grammar:\n");
        for (Rule rule : rules) {
            sb.append(rule.toString()).append("\n");
        }

        return sb.toString();
    }
}

class RuleMap {
    LinkedHashMap<Symbol, Rule> rules = new LinkedHashMap<>();

    // if a rule already exists, add the right-hand side to that rule; else put in the rule into the map
    public void add(Rule rule) {
        if (rules.containsKey(rule.left)) {
            rules.get(rule.left).addRight(rule);
        } else {
            rules.put(rule.left, rule);
        }
    }

    // produces a list of that symbol's rule's right hand side
    protected List<Symbol[]> symbolsFromSymbol(Symbol symbol) {
        if (!rules.containsKey(symbol))
            throw new RuntimeException("No rules from the symbol " + symbol);
        return rules.get(symbol).right;
    }

    // produces a list of all rule left-hands that can turn into this symbol
    // symbolSet is an array incase you want to search for if a symbol can create a pair of symbols
    protected List<Symbol> symbolsToSymbol(Symbol[] symbolSet) {
        List<Symbol> leftSides = new ArrayList<>();

        // searches every right-hand side of every rule
        for (Map.Entry<Symbol, Rule> rule : this.rules.sequencedEntrySet()) {
            for (Symbol[] symbols : rule.getValue().right) {
                if (Arrays.equals(symbols, symbolSet)) {
                    leftSides.add(rule.getKey());
                    break;
                    // if one of the right-hand sides equals the symbolSet, add the left-hand side to
                    // the return list and break out of the rule; going onto the next rule
                }
            }
        }

        return leftSides;
    }

    protected List<Rule> getRules() {
        return rules.sequencedValues().stream().toList();
    }
}