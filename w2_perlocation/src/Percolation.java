import java.util.stream.IntStream;

public class Percolation {
    private final int n;
    private final int[][] grid;
    private final int TOP_ROOT = -1;
    private final int SINGLE_SITE = -2;
    private final int NOT_SUITABLE = -3;
    private int openSites = 0;
    private boolean isPercolated = false;

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

    private int getNeighbourRoot(int row, int col) {
        try {
            validateIndexes(row, col);
            if (!isOpen(row, col)) {
                return NOT_SUITABLE;
            }
            return root(row, col, true);
        }
        catch (IllegalArgumentException e) {
            return NOT_SUITABLE;
        }
    }

    private int getMinRoot(int... vals) {
        int newRoot = IntStream.of(vals).filter(num -> num >= TOP_ROOT).min().orElse(SINGLE_SITE);
        return newRoot;
    }

    private void setValue(int row, int col, int rootVal) {
        this.grid[row][col] = rootVal;
        if (row == this.n - 1 && rootVal == TOP_ROOT) {
            this.isPercolated = true;
        }
    }

    private void updateNeighbour(int row, int col, int nRoot, int rootToSet) {
        if (nRoot == NOT_SUITABLE || nRoot == rootToSet) {
            return;
        }
        if (getSiteInitValue(row, col) == rootToSet) {
            return; // remove loop
        }
        setValue(row, col, rootToSet);
        if (nRoot > TOP_ROOT) {
            int bpRow = nRoot / n;
            int bpCol = nRoot - bpRow * n;
            setValue(bpRow, bpCol, rootToSet);
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
            int bRoot = root(row + 1, col, true);
            updateNeighbour(row + 1, col, bRoot, TOP_ROOT);
            return;
        }

        // TODO check first and last row
        int tRoot = getNeighbourRoot(row - 1, col);
        int bRoot = getNeighbourRoot(row + 1, col);
        int lRoot = getNeighbourRoot(row, col - 1);
        int rRoot = getNeighbourRoot(row, col + 1);
        int selectedRoot = getMinRoot(tRoot, bRoot, lRoot, rRoot);
        setValue(row, col, selectedRoot);

        int neigbourRoot = selectedRoot != SINGLE_SITE ? selectedRoot : selfVal;
        updateNeighbour(row - 1, col, tRoot, neigbourRoot);
        updateNeighbour(row + 1, col, bRoot, neigbourRoot);
        updateNeighbour(row, col - 1, lRoot, neigbourRoot);
        updateNeighbour(row, col + 1, rRoot, neigbourRoot);
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
        //         System.out.println("--- ITERATION END ---");
        //     }
        //     System.out.println("Perlocates!");
        // }
    }
}
