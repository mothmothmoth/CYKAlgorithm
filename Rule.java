import java.util.ArrayList;
import java.util.List;
/**
 * This class defines the core rules of the CYK algorithm.
 * rule : nonTerminal → elem ["|" elem]*
 * elem : nonTerminal nonTerminal | terminal
 */
public class Rule {
    public Symbol left;
    public List<Symbol[]> right = new ArrayList<>(); // double list for case like S → AB | x

    public static Rule Lex(GrammarCNF grammar, String rule) {
        Rule ruleFin = new Rule();
        String[] ruleSplit = rule.replace("→", "|").split("\\|");

        // lexes the symbols for the left side and checks if its correct
        ruleFin.left = Symbol.Lex(grammar, ruleSplit[0])[0];
        if (ruleFin.left.terminal)
            throw new RuntimeException(ruleFin.left + "cannot be on left-hand of CNF rule");

        // lexes the right-hand side(s)
        for (int i = 1; i < ruleSplit.length; i++) { // elems
            Symbol[] symbols = Symbol.Lex(grammar, ruleSplit[i]);
            ruleFin.right.add(symbols);
        }

        return ruleFin;
    }

    public void addRight(Rule from) { // add right-hand rules from "from" to itself
        this.right.addAll(from.right);
    }

    @Override
    public String toString() {
        StringBuilder rightSide = new StringBuilder();

        for (Symbol[] rightSection : right) {
            for (Symbol sym : rightSection)
                rightSide.append(sym);
            rightSide.append(" | ");
        }
        rightSide.delete(rightSide.length() - 3, rightSide.length()); // remove last separator

        return left + " → " + rightSide;
    }
}

