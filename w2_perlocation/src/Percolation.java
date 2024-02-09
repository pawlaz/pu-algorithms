public class Percolation {
    private final int n;
    private final int[][] grid;
    private final int TOP_ROOT = -1;
    private final int SINGLE_SITE = -2;
    private int openSites = 0;
    private boolean perlocates = false;

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

    private int get_site_init_value(int row, int col) {
        return row * this.n + col;
    }

    private int root(int row, int col) {
        int val = this.grid[row][col];

        int self = get_site_init_value(row, col);
        if (val == self) {
            return val; // not connected, exit
        }

        while (val >= 0) {
            int p_row = val / n;
            int p_c = val - p_row * n;
            val = this.grid[p_row][p_c];
        }

        return val;
    }

    public boolean isOpen(int row, int col) {
        this.validateIndexes(row, col);
        int root = root(row, col);
        return root != get_site_init_value(row, col);
    }

    public void open(int row, int col) {
        this.validateIndexes(row, col);
        int val = this.grid[row][col];
        int self = get_site_init_value(row, val);

        if (val < 0) {
            return; // connected to top or self connected
        }

        // row = 0 => root = -1 else root = -2


        // todo openSites
        // todo perlocates if root = -1 and row = n - 1
    }

    public boolean isFull(int row, int col) {
        this.validateIndexes(row, col);
        return this.grid[row][col] == TOP_ROOT;
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        return this.perlocates;
    }

    public void printGrid() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                System.out.printf("%4d", this.grid[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        System.out.println(p.isOpen(1, 1));
        // p.open(1, 1);
        System.out.println(p.isOpen(1, 1));

        p.printGrid();

        // Choose a site uniformly at random among all blocked sites.
        // Open the site.
        // The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.
    }
}
