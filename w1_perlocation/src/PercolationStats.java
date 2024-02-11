import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double PERCOLATION_THRESHOLD_CONST = 1.96;
    private final int trials;
    private final double[] results;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Incorrect arguments");
        }

        this.trials = trials;
        this.results = new double[trials];

        int maxT = n * n;
        for (int t = 0; t < trials; t++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                p.open(row, col);
            }
            results[t] = (double) p.numberOfOpenSites() / maxT;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (PERCOLATION_THRESHOLD_CONST * stddev() / Math.sqrt(this.trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (PERCOLATION_THRESHOLD_CONST * stddev() / Math.sqrt(this.trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Not enought args");
        }
        int enteredN;
        int trials;
        try {
            enteredN = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }

        PercolationStats pstats = new PercolationStats(enteredN, trials);
        StdOut.printf("mean                    = %.16f%n", pstats.mean());
        StdOut.printf("stddev                  = %.16f%n", pstats.stddev());
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]%n", pstats.confidenceLo(),
                      pstats.confidenceHi());
    }
}
