import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final int[][] grid;
    private final WeightedQuickUnionUF weightedUF;
    private final int openValue = -1;
    private final int connectedToTop = -2;
    private final int notSuitableValue = -3;
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

    public boolean isOpen(int row, int col) {
        this.validateIndexes(row, col);
        int val = this.grid[row][col];
        return val == openValue || val == connectedToTop;
    }

    private int getNeighbour(int row, int col) {
        try {
            validateIndexes(row, col);
            if (!isOpen(row, col)) {
                return notSuitableValue;
            }
            return getSiteInitValue(row, col);
        }
        catch (IllegalArgumentException e) {
            return notSuitableValue;
        }
    }

    private void setOpen(int row, int col, int val) {
        this.grid[row][col] = val;
        openSites++;
    }

    private void linkElems(int firstNumber, int fRow, int fCol, int secondNumber, int sRow,
                           int sCol) {
        if (firstNumber == notSuitableValue || secondNumber == notSuitableValue) {
            return;
        }
        this.weightedUF.union(firstNumber, secondNumber);

        int fVal = this.grid[fRow][fCol];
        int sVal = this.grid[sRow][sCol];

        if (fVal == connectedToTop || sVal == connectedToTop) {
            this.grid[fRow][fCol] = connectedToTop;
            this.grid[sRow][sCol] = connectedToTop;
            int newRoot = root(fRow, fCol);
            int rootrow = newRoot / n;
            int rootcol = newRoot - rootrow * n;
            this.grid[rootrow][rootcol] = connectedToTop;
        }
    }

    public void open(int row, int col) {
        this.validateIndexes(row, col);
        int val = this.grid[row][col];
        int selfVal = getSiteInitValue(row, col);

        if (val < 0) {
            return; // connected to top or self connected
        }

        if (val != selfVal) {
            return; // already open
        }

        int value = row == 0 ? connectedToTop : openValue;
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

    public boolean isFull(int row, int col) {
        this.validateIndexes(row, col);
        if (isOpen(row, col)) {
            if (this.grid[row][col] == connectedToTop) {
                return true;
            }
            int root = root(row, col);
            int prow = root / n;
            int pcol = root - prow * n;
            return this.grid[prow][pcol] == connectedToTop;
        }
        return false;
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
            if (isFull(row, col)) {
                isPercolated = true;
                return true;
            }
        }

        return false;
    }

    public void printGrid() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (getSiteInitValue(i, j) != this.grid[i][j]) {
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
    }
}
