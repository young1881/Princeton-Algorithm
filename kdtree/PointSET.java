import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (contains(p)) {
            return;
        }

        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        SET<Point2D> res = new SET<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                res.add(p);
            }
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        Point2D floor = pointSet.floor(p);
        Point2D ceiling = pointSet.ceiling(p);

        if (floor == null && ceiling == null) {
            return null;
        }

        double distCeiling = ceiling == null ? Double.POSITIVE_INFINITY : p.distanceTo(ceiling);
        double distFloor = floor == null ? Double.POSITIVE_INFINITY : p.distanceTo(floor);
        double d = Math.min(distCeiling, distFloor);

        Point2D minPoint = new Point2D(p.x(), p.y() - d);
        Point2D maxPoint = new Point2D(p.x(), p.y() + d);
        Point2D nearest = ceiling == null ? floor : ceiling;  // cannot be both null

        // The sub-set is inclusive for both extremities
        for (Point2D candidate : pointSet.subSet(minPoint, true, maxPoint, true)) {
            if (p.distanceTo(candidate) < p.distanceTo(nearest)) {
                nearest = candidate;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET set = new PointSET();

        StdOut.println("The empty state: " + set.isEmpty());

        for (int i = 0; i < 10; i += 2) {
            for (int j = 0; j < 10; j += 3) {
                Point2D p = new Point2D(i, j);
                set.insert(p);
            }
        }

        StdOut.println("The empty state: " + set.isEmpty());
        StdOut.println("The size: " + set.size());
        StdOut.println("Contains (0,0)? " + set.contains(new Point2D(0, 0)));
        StdOut.println("Contains (0,1)? " + set.contains(new Point2D(0, 1)));


        RectHV rec = new RectHV(1, 1, 6, 6);
        StdOut.print("Points in rec: ");
        for (Point2D p : set.range(rec)) {
            StdOut.println(p);
        }
        StdOut.println(set.nearest(new Point2D(2, 3)));
    }
}