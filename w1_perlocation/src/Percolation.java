import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int OPEN_VALUE = -1;
    private static final int CONNECTED_TO_TOP = -2;
    private static final int NOT_SUITABLE = -3;
    private final int n;
    private final int[][] grid;
    private final WeightedQuickUnionUF weightedUF;
    private boolean isPercolated = false;
    private int openSites = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Provided 'n' has to be more or equal to 1");
        }
        this.n = n;
        this.grid = new int[n][n];
        this.weightedUF = new WeightedQuickUnionUF(n * n);
        this.fillEmptyGrid();
    }

    private void fillEmptyGrid() {
        int counter = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.grid[i][j] = counter;
                counter++;
            }
        }
    }

    private void validateIndexes(int row, int col) {
        if (row < 0 || row >= this.n) {
            throw new IllegalArgumentException("Row has incorrect value: " + row);
        }
        if (col < 0 || col >= this.n) {
            throw new IllegalArgumentException("Col has incorrect value: " + col);
        }
    }

    private int getSiteInitValue(int row, int col) {
        return row * this.n + col;
    }

    private int root(int row, int col) {
        return weightedUF.find(getSiteInitValue(row, col));
    }

    private boolean checkOpen(int row, int col) {
        this.validateIndexes(row, col);
        int val = this.grid[row][col];
        return val == OPEN_VALUE || val == CONNECTED_TO_TOP;
    }

    public boolean isOpen(int row, int col) {
        return checkOpen(row - 1, col - 1);
    }

    private int getNeighbour(int row, int col) {
        try {
            validateIndexes(row, col);
            if (!checkOpen(row, col)) {
                return NOT_SUITABLE;
            }
            return getSiteInitValue(row, col);
        }
        catch (IllegalArgumentException e) {
            return NOT_SUITABLE;
        }
    }

    private void setOpen(int row, int col, int val) {
        this.grid[row][col] = val;
        openSites++;
    }

    private void linkElems(int firstNumber, int fRow, int fCol, int secondNumber, int sRow,
                           int sCol) {
        if (firstNumber == NOT_SUITABLE || secondNumber == NOT_SUITABLE) {
            return;
        }

        int rootF = root(fRow, fCol);
        int rootS = root(sRow, sCol);
        this.weightedUF.union(firstNumber, secondNumber);

        int fVal = this.grid[fRow][fCol];
        int sVal = this.grid[sRow][sCol];

        if (fVal == CONNECTED_TO_TOP || sVal == CONNECTED_TO_TOP || rootF == CONNECTED_TO_TOP
                || rootS == CONNECTED_TO_TOP) {
            this.grid[fRow][fCol] = CONNECTED_TO_TOP;
            this.grid[sRow][sCol] = CONNECTED_TO_TOP;

            int fRootRow = rootF / n;
            int fRootCol = rootF - fRootRow * n;
            this.grid[fRootRow][fRootCol] = CONNECTED_TO_TOP;

            int sRootRow = rootS / n;
            int sRootCol = rootS - sRootRow * n;
            this.grid[sRootRow][sRootCol] = CONNECTED_TO_TOP;
        }
    }

    public void open(int row, int col) {
        row = row - 1;
        col = col - 1;

        this.validateIndexes(row, col);
        int val = this.grid[row][col];
        int selfVal = getSiteInitValue(row, col);

        if (val < 0) {
            return; // connected to top or self connected
        }

        if (val != selfVal) {
            return; // already open
        }

        int value = row == 0 ? CONNECTED_TO_TOP : OPEN_VALUE;
        setOpen(row, col, value);

        int top = getNeighbour(row - 1, col);
        int bottom = getNeighbour(row + 1, col);
        int left = getNeighbour(row, col - 1);
        int right = getNeighbour(row, col + 1);

        linkElems(selfVal, row, col, top, row - 1, col);
        linkElems(selfVal, row, col, bottom, row + 1, col);
        linkElems(selfVal, row, col, left, row, col - 1);
        linkElems(selfVal, row, col, right, row, col + 1);
    }

    private boolean checkFull(int row, int col) {
        this.validateIndexes(row, col);
        if (checkOpen(row, col)) {
            if (this.grid[row][col] == CONNECTED_TO_TOP) {
                return true;
            }
            int root = root(row, col);
            int prow = root / n;
            int pcol = root - prow * n;
            return this.grid[prow][pcol] == CONNECTED_TO_TOP;
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        return checkFull(row - 1, col - 1);
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        if (isPercolated) {
            return true;
        }

        int row = n - 1;
        for (int col = 0; col < n; col++) {
            if (checkFull(row, col)) {
                isPercolated = true;
                return true;
            }
        }

        return false;
    }

    public void printGrid() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_RED = "\u001B[31m";
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (checkFull(i, j)) {
                    System.out.printf(ANSI_RED + "%4d" + ANSI_RESET, this.grid[i][j]);
                }
                else if (this.grid[i][j] == OPEN_VALUE) {
                    System.out.printf(ANSI_BLUE + "%4d" + ANSI_RESET, this.grid[i][j]);
                }
                else {
                    System.out.printf("%4d", this.grid[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void printRoots() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (getSiteInitValue(i, j) != this.grid[i][j]) {
                    System.out.printf(ANSI_BLUE + "%4d" + ANSI_RESET, root(i, j));
                }
                else {
                    System.out.printf("%4d", this.grid[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int enteredN = 20;
        int trials = 10;

        for (int t = 0; t < trials; t++) {
            System.out.println("----------------");
            Percolation p = new Percolation(enteredN);
            while (!p.percolates()) {
                int row = StdRandom.uniformInt(1, enteredN + 1);
                int col = StdRandom.uniformInt(1, enteredN + 1);
                p.open(row, col);
            }
            p.printGrid();
            System.out.println();
        }
    }
}
