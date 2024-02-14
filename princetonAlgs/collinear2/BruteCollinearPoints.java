import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    /**
     * Examines 4 points at a time and checks whether they all lie on the same line segment,
     * returning all such line
     * segments. To check whether the 4 points p, q, r, and s are collinear, check whether the three
     * slopes between
     * p and q, between p and r, and between p and s are all equal.
     **/
    public BruteCollinearPoints(Point[] points) {
        /*
        Corner cases. Throw an IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a repeated point.
         */
        // Check for null values
        if (points == null) {
            throw new IllegalArgumentException("Input is null.");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("There is a null in the input.");
            }
        }
        // copy the points into new array variable
        Point[] copyPoints = points.clone();
        // sort the array of points
        Arrays.sort(copyPoints);
        // Check for repeated points in argument
        if (copyPoints.length > 1) {
            for (int i = 1; i < copyPoints.length; i++) {
                if (copyPoints[i].compareTo(copyPoints[i - 1]) == 0) {
                    throw new IllegalArgumentException("Duplicate points in input");
                }
            }
        }
        // create array to hold the line segments found
        ArrayList<LineSegment> foundLines = new ArrayList<LineSegment>();

        if (copyPoints.length > 3) {
            // create temporary array to check colineararity
            Point[] temp = new Point[4];
            // Loop through all points 4 at a time
            for (int i = 0; i < copyPoints.length - 3; i++) {
                temp[0] = copyPoints[i];
                for (int j = i + 1; j < copyPoints.length - 2; j++) {
                    temp[1] = copyPoints[j];
                    for (int k = j + 1; k < copyPoints.length - 1; k++) {
                        temp[2] = copyPoints[k];
                        for (int w = k + 1; w < copyPoints.length; w++) {
                            temp[3] = copyPoints[w];
                            if (collinear(temp)) {
                                LineSegment segment = createLineSegment(temp);
                                foundLines.add(segment);
                            }
                        }
                    }
                }
            }
        }
        segments = foundLines.toArray(new LineSegment[foundLines.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments.clone();
    }

    /**
     * to check whether the 4 points p, q, r, and s are collinear, check whether the three slopes
     * between p and q,
     * between p and r, and between p and s are all equal.
     *
     * @param points
     * @return true if the 4 points are collinear as defined above
     */
    private boolean collinear(Point[] points) {
        double slope1 = points[0].slopeTo(points[1]);
        double slope2 = points[0].slopeTo(points[2]);
        double slope3 = points[0].slopeTo(points[3]);

        return (Double.compare(slope1, slope2) == 0) && (Double.compare(slope1, slope3) == 0);
    }

    private LineSegment createLineSegment(Point[] points) {
        Arrays.sort(points);
        return new LineSegment(points[0], points[3]);
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

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
