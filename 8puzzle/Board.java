import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Board {
    private final int[][] tiles;
    private final int size;
    private final int n;
    private static int blankIndex;
    private static int hamming;
    private static int manhattan;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new NullPointerException();
        }

        this.tiles = copyTiles(tiles);
        n = tiles.length;
        size = n * n;

        // compute the hamming & manhattan distance in one loop
        hamming = 0;
        manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    blankIndex = i * n + j;
                    continue;
                }
                int expectedI = (tiles[i][j] - 1) / n;
                int expectedJ = (tiles[i][j] - 1) % n;
                int distance = Math.abs(expectedI - i) + Math.abs(expectedJ - j);
                hamming += (distance == 0) ? 0 : 1;
                manhattan += distance;
            }
        }
    }

    private int[][] copyTiles(int[][] matrix) {
        int[][] clone = new int[matrix.length][];
        for (int row = 0; row < matrix.length; row++) {
            clone[row] = matrix[row].clone();
        }
        return clone;
    }

    // string representation of this board
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == n - 1) {
                    res.append(tiles[i][j]).append("\n");
                    break;
                }
                res.append(tiles[i][j]).append(" ");
            }
        }
        return res.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // get the value of tiles by given index
    private int getValue(int index) {
        int j = index % n;
        int i = index / n;
        return tiles[i][j];
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size - 1; i++) {
            if (getValue(i) != i + 1) {
                return false;
            }
        }
        return (getValue(size - 1) == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board yBoard = (Board) y;
        if (this.dimension() != yBoard.dimension()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.getValue(i) != yBoard.getValue(i)) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();

        if (blankIndex % n != 0) {
            int[][] leftBoard = copyTiles(tiles);
            swap(leftBoard, blankIndex, blankIndex - 1);
            neighbors.add(new Board(leftBoard));
        }

        if (blankIndex % n != n - 1) {
            int[][] rightBoard = copyTiles(tiles);
            swap(rightBoard, blankIndex, blankIndex + 1);
            neighbors.add(new Board(rightBoard));
        }

        if (blankIndex / n != 0) {
            int[][] upBoard = copyTiles(tiles);
            swap(upBoard, blankIndex, blankIndex - n);
            neighbors.add(new Board(upBoard));
        }

        if (blankIndex / n != n - 1) {
            int[][] downBoard = copyTiles(tiles);
            swap(downBoard, blankIndex, blankIndex + n);
            neighbors.add(new Board(downBoard));
        }

        return neighbors;
    }

    private void swap(int[][] board, int index1, int index2) {
        int i1 = index1 / n;
        int j1 = index1 % n;
        int i2 = index2 / n;
        int j2 = index2 % n;
        int tmp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = tmp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] res = copyTiles(tiles);
        if (blankIndex % n != 0) {
            swap(res, 0, n);
        } else {
            swap(res, 1, n + 1);
        }

        return (new Board(res));
    }

    // unit testing (not graded)
    public static void main(String[] args) {
//        int[][] tiles1 = {{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
        int[][] tiles1 = {{0, 1}, {2, 3}};
        Board board1 = new Board(tiles1);
        StdOut.println(board1.dimension());
        StdOut.println(board1.isGoal());
        StdOut.println(board1.hamming());
        StdOut.println(board1.manhattan());
        StdOut.println("\n" + board1.twin());

        LinkedList<Board> neighbors = (LinkedList<Board>) board1.neighbors();
        for (Board neighbor : neighbors) {
            StdOut.println(neighbor.toString());
        }
    }
}