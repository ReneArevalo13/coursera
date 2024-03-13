import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();

    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null value");
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null value");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // extract data from rect
        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();
        Queue<Point2D> pointsInRange = new Queue<>();

        for (Point2D p : points) {
            if (p.x() >= xmin && p.y() >= ymin && p.x() <= xmax && p.y() <= ymax) {
                pointsInRange.enqueue(p);
            }
        }
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null value");
        if (points.isEmpty()) return null;

        double distance = 10000.00;
        Point2D champion = null;

        for (Point2D i : points) {
            double tmp = p.distanceSquaredTo(i);
            if (tmp < distance) {
                champion = i;
            }
        }
        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}

















