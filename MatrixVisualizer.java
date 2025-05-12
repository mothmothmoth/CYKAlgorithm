import java.util.List;

public class MatrixVisualizer {
    /**
     * This is a simple method that lets us visualize a matrix by printing the contents of each cell neatly inside []
     * @param matrix
     */
    public static void printMatrix(List<Symbol>[][] matrix) {
        // UPDATE - made all cells maximum length equal to cell content max found
        // UPDATE - made padding centred instead of left aligned
        int size = matrix.length;
        int maxContent = 0;

        // First we calculate the length of the longest content
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                String content = getContent(matrix[row][col]);

                maxContent = Math.max(maxContent, content.length());
            }
        }

        // Print each cell with pads
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                String content = getContent(matrix[row][col]);
                String padded = padCenter(content, maxContent);
                // fill cels first with padding
                System.out.print("[" + padded + "] ");
            }
            System.out.println();
        }
    }

    // Comma-separated Symbol list
    private static String getContent(List<Symbol> cell) {
        if (cell == null || cell.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cell.size(); i++) {
            sb.append(cell.get(i));
            if (i < cell.size() - 1) sb.append(",");
        }

        return sb.toString();
    }

    //  helper method to center the text with padding
    private static String padCenter(String str, int width) {
        int padding = width - str.length();
        int left = padding / 2;
        int right = padding - left;

        return " ".repeat(left) + str + " ".repeat(right);
    }
}
