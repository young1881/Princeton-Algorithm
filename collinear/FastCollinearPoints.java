import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private final LinkedList<LineSegment> collinearLineSegments = new LinkedList<>();

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        checkDuplicated(points);

        int len = points.length;
        if (len <= 3) return;

        Point[] pointsClone = points.clone();
        Arrays.sort(pointsClone);

        // Think of p as the origin
        for (Point p : pointsClone) {
            // For each other point q, determine the slope it makes with p.
            Point[] sortBySlope = pointsClone.clone();
            // Sort the points according to the slopes they make with p.
            Arrays.sort(sortBySlope, p.slopeOrder());

            // Check if any 3 (or more) adjacent points in the sorted order
            // have equal slopes with respect to p.
            // If so, these points, together with p, are collinear.
            int i = 0;

            while (i < len) {
                LinkedList<Point> adjacentPoints = new LinkedList<>();
                double slopeRef = p.slopeTo(sortBySlope[i]);
                do {
                    adjacentPoints.addLast(sortBySlope[i]);
                    i++;
                } while (i < len && p.slopeTo(sortBySlope[i]) == slopeRef);
                if (adjacentPoints.size() >= 3 && p.compareTo(adjacentPoints.removeFirst()) < 0) {
                    LineSegment pCollinear = new LineSegment(p, adjacentPoints.removeLast());
                    collinearLineSegments.addLast(pCollinear);
                }
            }
        }
    }

    private void checkNull(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument to constructor is null");
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("one point is null");
            }
        }
    }

    private void checkDuplicated(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicate(s) found.");
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return collinearLineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment segment : collinearLineSegments) {
            res[i++] = segment;
        }
        return res;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}