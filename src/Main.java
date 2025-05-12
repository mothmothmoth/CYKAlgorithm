import java.io.IOException;
import java.util.*;

public class Main {
    /**
     * This class takes an input grammar file and a input for the string the user wants to test, then it outputs the full matrix and whether it belongs in CYK or not.
     */
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String grammar = GrammarCNF.ImportGrammar("grammar");
        GrammarCNF cnf = GrammarCNF.Lex(grammar);
        // Define starting symbol
        Symbol startingSymbol = cnf.getStart();
        System.out.println(cnf);

        //Algorithm implementation starts here
        System.out.println("Enter the string you want to check: ");
        String input = scan.nextLine();

        //String input = "aba";  // for quick testing
        int length = input.length();
        int c = length - 1; // variable used in matrix calculations

        @SuppressWarnings("unchecked")
        List<Symbol>[][] matrix = new ArrayList[length][length];

        // First we convert the input string to an array of symbols
        Symbol[] inputSymbols = cnf.stringToSymbolArray(input);


        // Step 1
        // Fill the matrix diagonal with single terminals
        System.out.println("Step 1");
        System.out.println("Fill the matrix diagonal with single terminals");

        // Row is reversed because CYK algorithm uses bottom right triangle of matrix
        for (int row = c ; row >= 0; row--) {
            for (int col = 0; col < c + 1; col++) {
                // I only want to fill the counter-diagonal of the matrix
                if (row + col == c) {
                    matrix[row][col] = new ArrayList<>(List.of(inputSymbols[col]));
                }
            }
        }
        MatrixVisualizer.printMatrix(matrix);
        System.out.println();

        // Step 2
        // Replace the matrix diagonal with Non-Terminals that produce each corresponding terminal
        System.out.println("Step 2");
        System.out.println("Replace the matrix diagonal with Non-Terminals that produce each corresponding terminal");
        for (int row = c ; row >= 0; row--) {
            for (int col = 0; col < c + 1; col++) {
                // I only want to fill the counter-diagonal of the matrix
                if (row + col == c) {
                    Symbol[] terminal = matrix[row][col].toArray(new Symbol[0]);
                    List<Symbol> nonTerminals = cnf.symbolsToSymbol(terminal);
                    matrix[row][col].clear();
                    matrix[row][col].addAll(nonTerminals);
                }
            }
        }
        MatrixVisualizer.printMatrix(matrix);
        System.out.println();

        // Step 3
        // Fill the lower matrix by taking cartesian products appropriately, finding the production rule and adding a set of the resultant non-terminals in the matrix cell
        System.out.println("Step 3");
        System.out.println("Fill the lower matrix by taking cartesian products appropriately, finding the production rule and adding a set of the resultant non-terminals in the matrix cell");
        for (int d = c + 1; d <= 2 * c; d++) { // only moving diagonally below the counter-diagonal
            for (int row = c; row >= 0; row--) {
                int col = d - row;
                if (col >= 0 && col <= c && row <= c && row + col == d) {
                    // Logic for cartesian product: We will use 2 counters (row,m) and (n,col) that will go along the longitude and latitude of the main cell (row, col) at work in correct order
                    int leftStart = 0;
                    int topEnd = 0;
                    for (int p = c ; p >= 0; p--) {
                        for (int q = 0; q < c + 1; q++) {
                            if ( p + q == c) {
                                if (p == row) leftStart = q;
                                if (q == col) topEnd = p;
                            }
                        }
                    }
                    List<Symbol[]> cartesianProduct = new ArrayList<>();
                    boolean control = true;
                    while (control) {
                        int rowCounter = row - 1;
                        List<Symbol> left = matrix[row][leftStart];
                        List<Symbol> right = matrix[rowCounter][col];

                        for (Symbol l : left) {
                            for (Symbol r : right) {
                                cartesianProduct.add(new Symbol[] { l, r });
                            }
                        }

                        leftStart++;
                        rowCounter--;
                        if(leftStart >= col || rowCounter < topEnd) {
                            control = false;
                        }
                    }
                    Set<Symbol> symbolSet = new HashSet<>();

                    for (Symbol[] rightSideNonTerminal : cartesianProduct) {
                        //System.out.println(Arrays.toString(rightSideNonTerminal)); //for debugging purposes
                        List<Symbol> leftSideNonTerminal = cnf.symbolsToSymbol(rightSideNonTerminal);
                        symbolSet.addAll(leftSideNonTerminal);
                    }

                    matrix[row][col] = new ArrayList<>(symbolSet);
                }
            }
        }
        MatrixVisualizer.printMatrix(matrix);
        System.out.println();
        if (matrix[c][c].contains(startingSymbol)) {
            System.out.println("Language defined by the Grammar in CNF ACCEPTS this string!");
        }
        else {
            System.out.println("Language defined by the Grammar in CNF does not ACCEPT this string!");
        }

        scan.close();
    }


}
