import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

import java.util.LinkedList;

public class Solver {

    private static class SearchNode implements Comparable<SearchNode> {
        final Board board;
        final SearchNode prev;
        final int move;

        final int manhattan;

        SearchNode(Board b, SearchNode p, int m) {
            board = b;
            prev = p;
            move = m;
            manhattan = board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.priority() - that.priority();
        }

        private int priority() {
            return manhattan + move;
        }
    }

    private SearchNode solutionNode;
    private final boolean solvable;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // initial the minPQ
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solutionNode = null;

        SearchNode initialNode = new SearchNode(initial, null, 0);
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        minPQ.insert(initialNode);

        SearchNode initialNodeTwin = new SearchNode(initial.twin(), null, 0);
        MinPQ<SearchNode> minPQTwin = new MinPQ<>();
        minPQTwin.insert(initialNodeTwin);

        // find the solution
        SearchNode min;
        SearchNode minTwin;
        do {
            min = minPQ.delMin();

            for (Board neighbor : min.board.neighbors()) {
                if (isPrevious(neighbor, min)) {
                    continue;
                }
                SearchNode neighborNode = new SearchNode(neighbor, min, min.move + 1);
                minPQ.insert(neighborNode);
            }

            minTwin = minPQTwin.delMin();
            for (Board neighbor : minTwin.board.neighbors()) {
                if (isPrevious(neighbor, minTwin)) {
                    continue;
                }
                SearchNode neighborNode = new SearchNode(neighbor, minTwin, minTwin.move + 1);
                minPQTwin.insert(neighborNode);
            }
        } while (!min.board.isGoal() && !minTwin.board.isGoal());
        if (min.board.isGoal()) {
            solutionNode = min;
            solvable = true;
        } else {
            solvable = false;
        }
    }

    private boolean isPrevious(Board neighbor, SearchNode node) {
        return node.prev != null && (neighbor.equals(node.prev.board));
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return (!solvable) ? -1 : solutionNode.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        LinkedList<Board> solution = new LinkedList<>();
        for (SearchNode tmp = solutionNode; tmp != null; tmp = tmp.prev) {
            solution.addFirst(tmp.board);
        }
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}