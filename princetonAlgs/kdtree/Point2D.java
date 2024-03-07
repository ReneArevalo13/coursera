import edu.princeton.cs.algs4.StdDraw;

public class Point2D implements Comparable<Point2D> {
    private double x;
    private double y;

    // construct the point (x, y)
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // x-coordinate
    public double x() {
        return x;
    }

    // y-coordinate
    public double y() {
        return y;
    }

    // Euclidean distance between two points
    public double distanceTo(Point2D that) {
        double x0 = this.x;
        double y0 = this.y;
        double x1 = that.x;
        double y1 = that.y;
        return Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));

    }

    // square of Euclidean distance between two points
    public double distanceSquaredTo(Point2D that) {
        return distanceTo(that) * distanceTo(that);

    }

    // for use in an ordered symbol table
    public int compareTo(Point2D that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // does this point equal that object?
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (that.getClass() != this.getClass()) return false;
        Point2D point = (Point2D) that;
        return this.x == point.x && this.y == point.y;
    }

    // draw to standard draw
    public void draw() {
        StdDraw.point(this.x, this.y);
    }

    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
