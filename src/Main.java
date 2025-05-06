public class Main {
    public static void main(String[] args) {
        String grammar = """
                S → PS1 | SS
                S1 → SQ
                P → (
                Q → )
                """;
        String[] terminals = new String[]{"(", ")"};
        String[] nonTerminals = new String[]{"S", "S1", "P", "Q"};
        GrammarCNF cnf = GrammarCNF.Lex(grammar, terminals, nonTerminals);

        System.out.println(cnf);
    }
}
