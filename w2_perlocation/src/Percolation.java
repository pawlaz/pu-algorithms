import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final int[][] grid;
    private final WeightedQuickUnionUF weightedUF;
    private final int openValue = 1;
    private final int notSuitableValue = -1;
    private boolean isPercolated = false;
    private int openSites = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Provided 'n' has to be more or equal to 1");
        }
        this.n = n;
        this.grid = new int[n][n];
        this.weightedUF = new WeightedQuickUnionUF(n);
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
        return this.grid[row][col] == openValue;
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

    private void setOpen(int row, int col) {
        this.grid[row][col] = openValue;
        openSites++;
    }

    private void linkElems(int first, int second) {
        if (first == notSuitableValue || second == notSuitableValue) {
            return;
        }
        this.weightedUF.union(first, second);
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

        setOpen(row, col);

        int top = getNeighbour(row - 1, col);
        int bottom = getNeighbour(row + 1, col);
        int left = getNeighbour(row, col - 1);
        int right = getNeighbour(row, col + 1);

        linkElems(selfVal, top);
        linkElems(selfVal, bottom);
        linkElems(selfVal, left);
        linkElems(selfVal, right);
    }

    public boolean isFull(int row, int col) {
        this.validateIndexes(row, col);
        if (isOpen(row, col)) {
            int root = root(row, col);
            return root < n;
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
        for (int col = n * row; col < n; col++) {
            if (isFull(row, col)) {
                isPercolated = true;
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {

    }
}
