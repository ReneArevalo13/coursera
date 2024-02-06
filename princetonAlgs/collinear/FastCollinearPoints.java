/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        /*
        Corner cases. Throw an IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a
        repeated point.
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
        Point[] slopeOrderPoints = copyPoints.clone();
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
        // main loop
        if (copyPoints.length > 3) {
            for (Point p : copyPoints) {
                Arrays.sort(slopeOrderPoints, p.slopeOrder());
                findSegments(slopeOrderPoints, p, foundLines);
            }
        }
        segments = foundLines.toArray(new LineSegment[foundLines.size()]);

    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments;
    }

    private void findSegments(Point[] points, Point p, ArrayList<LineSegment> foundLines) {
        int start = 1;
        // calculate slope to the first point (1st)
        double firstSlope = p.slopeTo(points[1]);

        for (int i = 2; i < points.length; i++) {
            // calculate slope to the next point (2nd)
            double secondSlope = p.slopeTo(points[i]);
            // check to see if the collinearity STOPS as we progress through the points
            if (!collinearSlope(firstSlope, secondSlope)) {
                // check if we have at least 3 collinear points
                if (i - start >= 3) {
                    Point[] lineStart = genSegment(points, p, start, i);
                    // make sure line segment start is p
                    if (lineStart[0].compareTo(p) == 0) {
                        foundLines.add(new LineSegment(lineStart[0], lineStart[1]));
                    }
                }
                // update pointers for next iteration
                start = i;
                firstSlope = secondSlope;
            }
        }
        if (points.length - start >= 3) {
            Point[] lastPoints = genSegment(points, p, start, points.length);
            if (lastPoints[0].compareTo(p) == 0) {
                foundLines.add(new LineSegment(lastPoints[0], lastPoints[1]));
            }
        }
    }


    /**
     * helper method to return the start and end points for the line segment
     *
     * @param points: slope ordered points
     * @param p:      origin point
     * @param start:  start index
     * @param end:    end index
     * @return start and end points of collinear line segment
     */
    private Point[] genSegment(Point[] points, Point p, int start, int end) {
        ArrayList<Point> segment = new ArrayList<Point>();
        // add origin
        segment.add(p);
        segment.addAll(Arrays.asList(points).subList(start, end));
        segment.sort(null);
        return new Point[] { segment.get(0), segment.get(segment.size() - 1) };
    }

    /**
     * helper to compare two slopes
     * returns true if the slopes are equal
     **/
    private boolean collinearSlope(double m1, double m2) {
        return Double.compare(m1, m2) == 0;
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
