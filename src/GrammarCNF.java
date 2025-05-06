import java.util.*;

public class GrammarCNF {
    public final List<Symbol> terminals = new ArrayList<>();
    public final List<Symbol> nonTerminals = new ArrayList<>();

    private final RuleMap rules = new RuleMap();

    public static GrammarCNF Lex(String grammar, String[] terminals, String[] nonTerminals) {
        GrammarCNF gramFin = new GrammarCNF();
        List<String> rules = grammar.lines().toList();

        // sorted so when lexing symbols we will find larger symbols first
        Arrays.sort(terminals, Comparator.comparingInt(String::length).reversed());
        for (String terminal : terminals)
            gramFin.terminals.add(new Symbol(terminal, true));

        Arrays.sort(nonTerminals, Comparator.comparingInt(String::length).reversed());
        for (String nonTerminal : nonTerminals)
            gramFin.nonTerminals.add(new Symbol(nonTerminal, false));

        for (String line : rules) {
            Rule rule = Rule.Lex(gramFin, line);
            gramFin.rules.add(rule);
        }

        return gramFin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("V = ").append(nonTerminals).append("\n");
        sb.append("Σ = ").append(terminals).append("\n\n");
        sb.append("Grammar:\n");
        for (Symbol sym : nonTerminals) {
            sb.append(rules.get(sym).toString()).append("\n");
        }

        return sb.toString();
    }
}

class RuleMap {
    HashMap<Symbol, Rule> rules = new HashMap<>();

    public void add(Rule rule) {
        if (rules.containsKey(rule.left)) {
            rules.get(rule.left).addRight(rule);
        } else {
            rules.put(rule.left, rule);
        }
    }

    public Rule get(Symbol leftHand) {
        return rules.get(leftHand);
    }

    public List<Symbol[]> fromSymbol(Symbol symbol) {
        if (!rules.containsKey(symbol))
            throw new RuntimeException("No rules from the symbol " + symbol);
        return rules.get(symbol).right;
    }

    public List<Rule> getRules() {
        return rules.values().stream().toList();
    }
}