import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;

public class KdTree {
    private enum STATE {
        HORIZONTAL, VERTICAL
    }

    private static class Node {
        public Point2D point;
        public STATE state;
        public Node left;
        public Node right;
        public RectHV rect;

        public Node(Point2D p, STATE s, RectHV r) {
            point = p;
            state = s;
            rect = r;
            left = null;
            right = null;
        }

        public STATE nextState() {
            return this.state == STATE.HORIZONTAL ? STATE.VERTICAL : STATE.HORIZONTAL;
        }

        public RectHV southwestRec() {
            if (this.state == STATE.VERTICAL) {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
        }

        public RectHV northeastRec() {
            if (this.state == STATE.VERTICAL) {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
        }

        public boolean greaterThan(Point2D p) {
            return ((state == STATE.HORIZONTAL) && point.y() > p.y()
                    || (state == STATE.VERTICAL) && point.x() > p.x());
        }
    }

    private Node root;
    private int size;
    private double distance;
    private Point2D nearestPoint;

    public KdTree() {

    }

    private KdTree(Point2D p) {
        checkNull(p);
        root = new Node(p, STATE.HORIZONTAL, new RectHV(0, 0, 1, 1));
        size = 1;
    }

    private void checkNull(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null || root.point == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);

        root = insert(p, root, null, true);
    }

    private Node insert(Point2D p, Node curr, Node parent, boolean isLeft) {
        if (curr == null) {
            size++;
            if (parent == null) {
                return new Node(p, STATE.HORIZONTAL, new RectHV(0, 0, 1, 1));
            } else {
                if (isLeft) {
                    return new Node(p, parent.nextState(), parent.southwestRec());
                } else {
                    return new Node(p, parent.nextState(), parent.northeastRec());
                }
            }
        } else if (curr.point.equals(p)) {
            return curr;
        } else if (curr.greaterThan(p)) {
            curr.left = insert(p, curr.left, curr, true);
        } else {
            curr.right = insert(p, curr.right, curr, false);
        }

        return curr;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        Node curr = root;
        if (isEmpty()) return false;
        while (curr != null) {
            if (curr.point.equals(p)) return true;
            if (curr.greaterThan(p)) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        LinkedList<Point2D> res = new LinkedList<>();
        addPoint(res, rect, root);
        return res;
    }

    private void addPoint(LinkedList<Point2D> res, RectHV rect, Node node) {
        if (node == null) return;
        if (rect.contains(node.point)) {
            res.addLast(node.point);
        }
        if (node.greaterThan(new Point2D(rect.xmax(), rect.ymax()))) {
            addPoint(res, rect, node.left);
        } else if (!node.greaterThan(new Point2D(rect.xmin(), rect.ymin()))) {
            addPoint(res, rect, node.right);
        } else {
            addPoint(res, rect, node.left);
            addPoint(res, rect, node.right);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);

        nearestPoint = null;
        distance = Double.POSITIVE_INFINITY;
        nearest(p, root);
        return nearestPoint;
    }

    private void nearest(Point2D p, Node node) {
        if (node == null) return;
        if (p.distanceSquaredTo(node.point) < distance) {
            nearestPoint = node.point;
            distance = p.distanceSquaredTo(node.point);
        }
        boolean searchLeft = false, searchRight = false;
        if (node.left != null && node.left.rect.distanceSquaredTo(p) < distance) {
            searchLeft = true;
        }
        if (node.right != null && node.right.rect.distanceSquaredTo(p) < distance) {
            searchRight = true;
        }
        if (searchLeft && searchRight) {
            if (node.greaterThan(p)) {
                nearest(p, node.left);
                if (node.right.rect.distanceSquaredTo(p) < distance) {
                    nearest(p, node.right);
                }
            } else {
                nearest(p, node.right);
                if (node.left.rect.distanceSquaredTo(p) < distance) {
                    nearest(p, node.left);
                }
            }
        } else {
            if (searchLeft) {
                nearest(p, node.left);
            }
            if (searchRight) {
                nearest(p, node.right);
            }
        }
    }

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        System.out.println(kdtree.size());
        System.out.println(kdtree.isEmpty());
        kdtree.insert(new Point2D(0.7, 0.2));
        System.out.println(kdtree.size());
        System.out.println(kdtree.isEmpty());
        System.out.println(kdtree.contains(new Point2D(0.7, 0.2)));

        kdtree.insert(new Point2D(0.5, 0.4));
        System.out.println(kdtree.size());
        System.out.println(kdtree.contains(new Point2D(0.5, 0.4)));

        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        System.out.println(kdtree.contains(new Point2D(0.4, 0.7)));
        Point2D p = new Point2D(0.9, 0.6);
        System.out.println(kdtree.nearest(p));

        KdTree kdTree2 = new KdTree();
        kdTree2.insert(new Point2D(0.7, 0.2));
        kdTree2.insert(new Point2D(0.5, 0.4));
        kdTree2.insert(new Point2D(0.2, 0.3));
        kdTree2.insert(new Point2D(0.4, 0.7));
        kdTree2.insert(new Point2D(0.9, 0.6));
        for (Point2D point : kdTree2.range(new RectHV(0.3, 0.4, 0.7, 0.9))) {
            System.out.println(point);
        }
    }
}
