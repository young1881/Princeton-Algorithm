import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // if blocked, false; open, true
    private final boolean[] state;
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final int top;
    private final boolean[] connectedToBottom;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is supposed to be positive integer!");
        }

        this.n = n;
        state = new boolean[n * n + 1];
        // state[0] represents the state of top, which is always open.
        state[0] = true;
        for (int i = 1; i < n * n + 1; i++) {
            state[i] = false;
        }
        openSites = 0;
        uf = new WeightedQuickUnionUF(n * n + 1);
        top = 0;

        /*
            To avoid backwash, we use an array connectedToBottom to denotes the connection between
            bottom and the sites of n-th row.
            for index of [n^2-n+1, n^2], they correspond to connectedToBottom of index[1,n]
         */
        connectedToBottom = new boolean[n + 1];
        for (int i = 0; i < n + 1; i++) {
            connectedToBottom[i] = false;
        }
    }

    // returns the index by row and col, index is supposed to between 1 and n*n
    private int index(int row, int col) {
        if (row < 1 || row > n) {
            throw new IllegalArgumentException("Row is out of bounds.");
        }
        if (col < 1 || col > n) {
            throw new IllegalArgumentException("Column is out of bounds.");
        }
        return (row - 1) * n + col;
    }

    // connects the neighbors of this row and col
    private void connect(int rowA, int colA, int rowB, int colB) {
        if (0 < rowB && rowB <= n && 0 < colB && colB <= n && isOpen(rowB, colB)) {
            int index1 = index(rowA, colA);
            int index2 = index(rowB, colB);
            uf.union(index1, index2);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int thisIndex = index(row, col);
        // for fear of open site repeatedly
        if (!state[thisIndex]) {
            state[thisIndex] = true;
            openSites++;
        }

        if (row == 1) {
            uf.union(thisIndex, top);
        }
        if (row == n) {
//            uf.union(thisIndex, bottom);  // if we use this line, backwash problem shall arise!
            connectedToBottom[thisIndex - n * n + n] = true;
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
        int index = index(row, col);
        return isFull(index);
    }

    private boolean isFull(int index) {
        int findIndex = uf.find(index);
        int topIndex = uf.find(top);
        return findIndex == topIndex;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = 1; i <= n; i++) {
            if (connectedToBottom[i] && isFull(i + n * n - n)) {
                return true;
            }
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 3);
        perc.open(2, 3);
        System.out.println("percolation state: " + perc.percolates());
        perc.open(3, 3);
        System.out.println("percolation state: " + perc.percolates());
        perc.open(3, 1);
        System.out.println("full state of (3,1): " + perc.isFull(3, 1));
    }
}