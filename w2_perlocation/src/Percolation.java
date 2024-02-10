import java.util.stream.IntStream;

public class Percolation {
    private final int n;
    private final int[][] grid;
    private final int TOP_ROOT = -1;
    private final int SINGLE_SITE = -2;
    private final int NOT_SUITABLE = -3;
    private int openSites = 0;
    private boolean isPercolated = false;

    // TODO: treesConnectToBttom: store roots, if root added - remove
    // TODO Trees weight

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Provided 'n' has to be more or equal to 1");
        }
        this.n = n;
        this.grid = new int[n][n];
        fillEmptyGrid();
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
        return root(row, col, false);
    }

    private int root(int row, int col, boolean prev) {
        int val = this.grid[row][col];

        int self = getSiteInitValue(row, col);
        if (val == self) {
            return val; // not connected, exit
        }

        int prevNonNgegativeVal = val;
        while (val >= 0) {
            int parentRow = val / n;
            int parentColumn = val - parentRow * n;
            val = this.grid[parentRow][parentColumn];
            if (val >= 0) {
                prevNonNgegativeVal = val;
            }
        }

        return prev && val < TOP_ROOT ? prevNonNgegativeVal : val;
    }

    public boolean isOpen(int row, int col) {
        this.validateIndexes(row, col);
        return this.grid[row][col] != getSiteInitValue(row, col);
    }

    private int getNeighbour(int row, int col) {
        try {
            validateIndexes(row, col);
            if (!isOpen(row, col)) {
                return NOT_SUITABLE;
            }
            return this.grid[row][col];
        }
        catch (IllegalArgumentException e) {
            return NOT_SUITABLE;
        }
    }

    private int getMin(int... vals) {
        int newRoot = IntStream.of(vals).filter(num -> num >= TOP_ROOT).min().orElse(SINGLE_SITE);
        return newRoot;
    }

    private void setValue(int row, int col, int rootVal) {
        if (getSiteInitValue(row, col) == rootVal) {
            return; // remove loop
        }

        this.grid[row][col] = rootVal;
        if (row == this.n - 1 && rootVal == TOP_ROOT) {
            this.isPercolated = true;
        }
    }

    private void updateNeighbour(int row, int col, int validNeighbourValue, int rootToSet) {
        if (validNeighbourValue == NOT_SUITABLE) {
            return;
        }

        int val = validNeighbourValue;
        setValue(row, col, rootToSet);
        while (val >= 0) {
            int parentRow = val / n;
            int parentColumn = val - parentRow * n;
            val = this.grid[parentRow][parentColumn];
            setValue(parentRow, parentColumn, rootToSet);
        }
    }


    // TODO: link to shortest root
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

        openSites++;
        if (row == 0) { // TODO: refactor
            setValue(row, col, TOP_ROOT);
            if (!isOpen(row + 1, col)) {
                return;
            }
            int b = getNeighbour(row + 1, col);
            updateNeighbour(row + 1, col, b, TOP_ROOT);
            return;
        }

        int t = getNeighbour(row - 1, col);
        int b = getNeighbour(row + 1, col);
        int l = getNeighbour(row, col - 1);
        int r = getNeighbour(row, col + 1);
        int selectedRoot = getMin(t, b, l, r);
        setValue(row, col, selectedRoot);

        int neigbourRoot = selectedRoot != SINGLE_SITE ? selectedRoot : selfVal;
        updateNeighbour(row - 1, col, t, neigbourRoot);
        updateNeighbour(row + 1, col, b, neigbourRoot);
        updateNeighbour(row, col - 1, l, neigbourRoot);
        updateNeighbour(row, col + 1, r, neigbourRoot);
    }

    public boolean isFull(int row, int col) {
        this.validateIndexes(row, col);
        int root = root(row, col);
        return root == TOP_ROOT;
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        return this.isPercolated;
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

    public static void main(String[] args) {
        int enteredN = 5;

        Percolation p = new Percolation(enteredN);
        p.open(3, 4);
        p.printGrid();
        System.out.println();
        p.open(2, 4);
        p.printGrid();
        System.out.println();
        p.open(1, 4);
        p.printGrid();
        System.out.println();
        p.open(2, 3);
        p.printGrid();
        System.out.println();
        p.open(2, 0);
        p.printGrid();
        System.out.println();
        p.open(0, 1);
        p.printGrid();
        System.out.println();
        p.open(2, 2);
        p.printGrid();
        System.out.println();
        p.open(1, 3);
        p.printGrid();
        System.out.println();
        p.open(0, 0);
        p.printGrid();
        System.out.println();
        p.open(1, 1);
        p.printGrid();
        System.out.println();
        p.open(3, 2);
        p.printGrid();
        System.out.println();
        p.open(4, 2);
        p.printGrid();
        System.out.println();
        p.open(2, 1);
        p.printGrid();
        System.out.println(p.percolates());


        // for (int h = 0; h < 1000; h++) {
        //     Percolation p = new Percolation(enteredN);
        //     int iteration = 1;
        //     while (!p.percolates()) {
        //         System.out.printf("--- ITERATION %d ---\n", iteration);
        //         int row = StdRandom.uniformInt(enteredN);
        //         int col = StdRandom.uniformInt(enteredN);
        //         System.out.printf("OPEN row=%d col=%d\n", row, col);
        //         p.open(row, col);
        //         System.out.println("OPEN SITES: " + p.numberOfOpenSites());
        //         p.printGrid();
        //         iteration++;
        //
        //         if (!p.percolates() && p.openSites == enteredN * enteredN) {
        //             System.out.println("STOP");
        //             return;
        //         }
        //         System.out.println("--- ITERATION END ---");
        //     }
        //     System.out.println("Perlocates!");
        // }
    }
}
