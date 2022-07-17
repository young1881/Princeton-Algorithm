import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> collinearLineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument to constructor is null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("one point is null");
            }
        }

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
                            collinearLineSegments.add(new LineSegment(tmp[i], tmp[h]));
                        }
                    }
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
}