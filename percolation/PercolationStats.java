import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] test;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be a positive integer!");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trials must be a positive integer!");
        }
        test = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            int count = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1; // base-1
                int col = StdRandom.uniform(n) + 1; // base-1
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    count++;
                }
            }
            test[i] = count * 1.0 / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(test);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(test);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(test.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(test.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java-algs4 PercolationStats n T");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = ["
                + stats.confidenceLo() + ", "
                + stats.confidenceHi() + "]");
    }

}