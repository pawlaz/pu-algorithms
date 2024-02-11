import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Percolation {
    private class Point {
        private final int row;
        private final int col;
        private final int rootVal;

        public Point(int row, int col, int val) {
            this.row = row;
            this.col = col;
            this.rootVal = val;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public int getRootVal() {
            return rootVal;
        }
    }

    private final int n;
    private final int[][] grid;
    private final int topRootValue = -1;
    private final int singleSiteValue = -2;
    private final int notSuitableValue = -3;
    private int openSites = 0;
    private boolean isPercolated = false;
    private List<Integer> connectedToBot = new ArrayList<>();

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

        return prev && val < topRootValue ? prevNonNgegativeVal : val;
    }

    public boolean isOpen(int row, int col) {
        this.validateIndexes(row, col);
        return this.grid[row][col] != getSiteInitValue(row, col);
    }

    private Point getNeighbour(int row, int col) {
        try {
            validateIndexes(row, col);
            if (!isOpen(row, col)) {
                return null;
            }
            int val = root(row, col);
            return new Point(row, col, val);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Point getMin(Point... vals) {
        Point newRoot = Arrays.stream(vals)
                              .filter(num -> num != null && num.rootVal >= topRootValue)
                              .min(
                                      (point, t1) -> Integer.compare(point.rootVal, t1.rootVal))
                              .orElse(null);
        return newRoot;
    }

    private void setValue(int row, int col, int rootVal) {
        if (getSiteInitValue(row, col) == rootVal) {
            return; // remove loop
        }

        this.grid[row][col] = rootVal;
    }

    private void updateNeighbour(int row, int col, Point neighbour, int rootToSet) {
        if (neighbour == null) {
            return;
        }

        if (neighbour.rootVal == rootToSet) {
            return;
        }

        int val = this.grid[row][col];
        setValue(row, col, rootToSet);
        while (val >= 0) {
            int parentRow = val / n;
            int parentColumn = val - parentRow * n;
            val = this.grid[parentRow][parentColumn];
            setValue(parentRow, parentColumn, rootToSet);
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

        openSites++;
        boolean lastRow = row == this.n - 1;
        int selectedRoot;
        if (row == 0) {
            selectedRoot = topRootValue;
            setValue(row, col, selectedRoot);
            if (!isOpen(row + 1, col)) {
                return;
            }
            Point b = getNeighbour(row + 1, col);
            if (b != null) {
                updateNeighbour(row + 1, col, b, selectedRoot);
            }
        }
        else {
            Point top = getNeighbour(row - 1, col);
            Point bottom = getNeighbour(row + 1, col);
            Point left = getNeighbour(row, col - 1);
            Point right = getNeighbour(row, col + 1);
            Point newRoot = getMin(top, bottom, left, right);
            if (newRoot == null) {
                selectedRoot = singleSiteValue;
            }
            else {
                selectedRoot = newRoot.rootVal;
            }

            setValue(row, col, selectedRoot);
            int nVal = selectedRoot == singleSiteValue ? selfVal : selectedRoot;
            updateNeighbour(row - 1, col, top, nVal);
            updateNeighbour(row + 1, col, bottom, nVal);
            updateNeighbour(row, col - 1, left, nVal);
            updateNeighbour(row, col + 1, right, nVal);
        }

        if (lastRow) {
            connectedToBot.add(selfVal);
            if (selectedRoot == topRootValue) {
                isPercolated = true;
            }
        }
    }

    public boolean isFull(int row, int col) {
        this.validateIndexes(row, col);
        int root = root(row, col);
        return root == topRootValue;
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        if (isPercolated) {
            return true;
        }

        for (int el : connectedToBot) {
            int row = el / n;
            int column = el - row * n;
            int rootVal = root(row, column);
            if (rootVal == topRootValue) {
                isPercolated = true;
                break;
            }
        }

        return isPercolated;
    }

    public static void main(String[] args) {

    }
}
