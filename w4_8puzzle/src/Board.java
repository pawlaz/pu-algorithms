import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {
    private static final int BLANK_VALUE = 0;
    private final int n;
    private final int[][] tiles;
    private final int hammingDistance;
    private final int manhattanDistance;
    private final int blankRow;
    private final int blankCol;

    public Board(int[][] tiles) {
        int arrLength = tiles.length;
        if (arrLength < 2 || arrLength > 127) {
            throw new IllegalArgumentException(
                    String.format("N=%d has to be 2 ≤ n < 128", arrLength)
            );
        }

        this.n = arrLength;
        this.tiles = new int[n][n];
        int zeros = 0;
        int zeroRow = 0;
        int zeroCol = 0;
        int hammingValue = 0;
        int manhattanValue = 0;
        for (int i = 0; i < n; i++) {
            if (tiles[i].length != n) {
                throw new IllegalArgumentException("Not n-by-n board provided");
            }
            for (int j = 0; j < n; j++) {
                int tileValue = tiles[i][j];
                this.tiles[i][j] = tileValue;

                if (tileValue == BLANK_VALUE) {
                    zeroRow = i;
                    zeroCol = j;
                    zeros++;
                    continue;
                }

                if (tileValue != i * n + j + 1) {
                    hammingValue++;
                }

                tileValue -= 1;
                int expectedRow = tileValue / n;
                int expectedCol = tileValue - expectedRow * n;
                manhattanValue += Math.abs(i - expectedRow) + Math.abs(j - expectedCol);

            }
        }

        if (zeros != 1) { // TODO: check for unique values also
            throw new IllegalArgumentException("Incorrect amount of empty tiles: " + zeros);
        }

        blankRow = zeroRow;
        blankCol = zeroCol;
        hammingDistance = hammingValue;
        manhattanDistance = manhattanValue;
    }

    public String toString() {
        // System.getProperty("line.separator") is forbidden
        String newLine = "\n";
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(newLine);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(" ").append(tiles[i][j]);
            }
            sb.append(newLine);
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hammingDistance;
    }


    public int manhattan() {
        return manhattanDistance;
    }

    public boolean isGoal() {
        return hammingDistance == 0;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board board = (Board) y;
        return n == board.n && Arrays.deepEquals(tiles, board.tiles);
    }

    private Board getCopyAndSwap(int row1, int col1, int row2, int col2) {
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newTiles[i][j] = tiles[i][j];
            }
        }

        int tmp = tiles[row1][col1];
        newTiles[row1][col1] = tiles[row2][col2];
        newTiles[row2][col2] = tmp;
        return new Board(newTiles);
    }

    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();

        if (blankRow > 0) {
            neighbors.enqueue(getCopyAndSwap(blankRow, blankCol, blankRow - 1, blankCol));
        }


        if (blankRow < n - 1) {
            neighbors.enqueue(getCopyAndSwap(blankRow, blankCol, blankRow + 1, blankCol));
        }

        if (blankCol > 0) {
            neighbors.enqueue(getCopyAndSwap(blankRow, blankCol, blankRow, blankCol - 1));
        }

        if (blankCol < n - 1) {
            neighbors.enqueue(getCopyAndSwap(blankRow, blankCol, blankRow, blankCol + 1));
        }

        return neighbors;
    }

    public Board twin() {
        if (tiles[0][0] != BLANK_VALUE && tiles[0][1] != BLANK_VALUE) {
            return getCopyAndSwap(0, 0, 0, 1);
        }

        return getCopyAndSwap(1, 0, 1, 1);
    }

    public static void main(String[] args) {
        int[][] a = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 5, 6 } };
        int[][] a2 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 5, 6 } };
        Board b = new Board(a);
        Board b2 = new Board(a2);
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.equals(b2));

        System.out.println(b);
        System.out.println(b.twin());

        Iterable<Board> neigbours = b.neighbors();
        for (Board brd : neigbours) {
            System.out.println(brd);
        }
    }
}
