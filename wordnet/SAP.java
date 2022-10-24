import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

import java.util.Iterator;

public class SAP {
    private final int vMax;
    private final Digraph wordNet;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        wordNet = new Digraph(G);
        vMax = G.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v > vMax || w > vMax) {
            throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfp1 = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths bfp2 = new BreadthFirstDirectedPaths(wordNet, w);

        int length = Integer.MAX_VALUE;

        for (int i = 0; i < vMax; i++) {
            if (bfp1.hasPathTo(i) && bfp2.hasPathTo(i)) {
                length = Math.min(length, bfp1.distTo(i) + bfp2.distTo(i));
            }
        }

        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (v > vMax || w > vMax) {
            throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfp1 = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths bfp2 = new BreadthFirstDirectedPaths(wordNet, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < vMax; i++) {
            if (bfp1.hasPathTo(i) && bfp2.hasPathTo(i)) {
                int temp = bfp1.distTo(i) + bfp2.distTo(i);
                if (temp < length) {
                    length = temp;
                    ancestor = i;
                }
            }
        }

        return length == Integer.MAX_VALUE ? -1 : ancestor;

    }

    private boolean isEmpty(Iterable<Integer> a) {
        Iterator<Integer> it = a.iterator();
        return !it.hasNext();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty(v) || isEmpty(w)) {
            return -1;
        }

        BreadthFirstDirectedPaths bfp1 = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths bfp2 = new BreadthFirstDirectedPaths(wordNet, w);

        int length = Integer.MAX_VALUE;

        for (int i = 0; i < vMax; i++) {
            if (bfp1.hasPathTo(i) && bfp2.hasPathTo(i)) {
                length = Math.min(length, bfp1.distTo(i) + bfp2.distTo(i));
            }
        }

        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty(v) || isEmpty(w)) {
            return -1;
        }

        BreadthFirstDirectedPaths bfp1 = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths bfp2 = new BreadthFirstDirectedPaths(wordNet, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < vMax; i++) {
            if (bfp1.hasPathTo(i) && bfp2.hasPathTo(i)) {
                int temp = bfp1.distTo(i) + bfp2.distTo(i);
                if (temp < length) {
                    length = temp;
                    ancestor = i;
                }
            }
        }

        return length == Integer.MAX_VALUE ? -1 : ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}