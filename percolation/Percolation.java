import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // if blocked, false; open, true
    private final boolean[] state;
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final int top;
    private final int bottom;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;

        state = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            state[i] = false;
        }
        openSites = 0;

        uf = new WeightedQuickUnionUF(n * n + 2);
        top = 0;
        bottom = n * n + 1;
    }

    // returns the index by row and col
    private int index(int row, int col) {
        if (row < 1 || row > n) {
            throw new IndexOutOfBoundsException("Row is out of bounds.");
        }
        if (col < 1 || col > n) {
            throw new IndexOutOfBoundsException("Column is out of bounds.");
        }
        return (row - 1) * n + col - 1;
    }

    // connects the neighbors of this row and col
    private void connect(int rowA, int colA, int rowB, int colB) {
        if (0 < rowB && rowB <= n && 0 < colB && colB <= n && isOpen(rowB, colB)) {
            uf.union(index(rowA, colA), index(rowB, colB));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int thisIndex = index(row, col);
        state[thisIndex] = true;
        openSites++;

        if (row == 1) {
            uf.union(thisIndex, top);
        }
        if (row == n) {
            uf.union(thisIndex, bottom);
        }

        connect(row, col, row + 1, col);
        connect(row, col, row - 1, col);
        connect(row, col, row, col + 1);
        connect(row, col, row, col - 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return state[index(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return uf.find(index(row, col)) == uf.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find(bottom) == uf.find(top);
    }

    // test client (optional)
    public static void main(String[] args){
        System.out.println("Run PercolationStats!");
    }
}
