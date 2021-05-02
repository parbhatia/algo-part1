import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private Percolation perco;
    private double[] resultArray;
    private int numTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        numTrials = trials;
        resultArray = new double[trials];
        for (int i = 0; i < trials; ++i) {
            perco = new Percolation(n);
            int openSites = 0;
            while (!perco.percolates()) {
                int randRow = StdRandom.uniform(1, n + 1);
                int randCol = StdRandom.uniform(1, n + 1);
                //keep trying to open site randomly
                while (!perco.isOpen(randRow, randCol)) {
                    perco.open(randRow, randCol);
                    ++openSites;
                }
            }
            //store trial result
            resultArray[i] = (double) openSites / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(resultArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(resultArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * Math.sqrt(stddev()) / Math.sqrt(numTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * Math.sqrt(stddev()) / Math.sqrt(numTrials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats PS = new PercolationStats(n, trials);
        StdOut.println("mean    = " + PS.mean());
        StdOut.println("stddev    = " + PS.stddev());
        StdOut.println(
                "95% confidence interval    = " + "[" + PS.confidenceLo() + "," + PS.confidenceHi()
                        + "]");
    }

}
