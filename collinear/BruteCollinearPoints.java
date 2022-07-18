import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {

    private final LinkedList<LineSegment> collinearLineSegments = new LinkedList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkNull(points);
        checkDuplicated(points);

        int len = points.length;

        if (len <= 3) return;
        Point[] tmp = Arrays.copyOf(points, len);
        Arrays.sort(tmp);

        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                for (int k = j + 1; k < len; k++) {
                    for (int h = k + 1; h < len; h++) {
                        double s1 = tmp[i].slopeTo(tmp[j]);
                        double s2 = tmp[i].slopeTo(tmp[k]);
                        double s3 = tmp[i].slopeTo(tmp[h]);
                        if (s1 == s2 && s1 == s3) {
                            collinearLineSegments.addLast(new LineSegment(tmp[i], tmp[h]));
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}