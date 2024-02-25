import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] lineSegments = new LineSegment[0];
    private int numberOfSegments = 0;

    // should work properly even if the input has 5 or more collinear points
    public FastCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is empty");
        }

        // TODO: merge these steps via in-place sorting
        Point[] pCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point can't be null");
            }
            pCopy[i] = points[i];
        }

        Arrays.sort(pCopy);
        for (int i = 0; i < points.length; i++) {
            if (i < pCopy.length - 1 && pCopy[i].compareTo(pCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicated point: " + pCopy[i]);
            }
        }

        calculateSegments(points, pCopy);
    }

    public int numberOfSegments() {
        return this.numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] shrinkedArray = new LineSegment[this.numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            shrinkedArray[i] = this.lineSegments[i];
        }
        return shrinkedArray;
    }

    // The method segments() should include each line segment containing 4 points exactly once
    private void calculateSegments(final Point[] originalPoints, final Point[] points) {
        if (points.length < 4) {
            this.lineSegments = new LineSegment[0];
            return;
        }

        // TODO: shrink after calculation
        this.lineSegments = new LineSegment[points.length * 2];
        for (int i = 0; i < originalPoints.length; i++) {
            Point p = originalPoints[i]; // keep points order
            Arrays.sort(points, p.slopeOrder());

            int count = 1;
            Point min = p;
            Point max = p;
            double initSlope = p.slopeTo(points[1]); // 0 is our point.
            for (int j = 1; j < points.length; j++) { // TODO more than 5
                double slope = p.slopeTo(points[j]);
                if (Double.compare(initSlope, slope) != 0) {
                    if (count > 3 && p.compareTo(min) == 0) {
                        lineSegments[numberOfSegments++] = new LineSegment(min, max);
                    }
                    initSlope = slope;
                    count = 1;
                    min = p;
                    max = p;
                }

                if (min.compareTo(points[j]) > 0) {
                    min = points[j];
                }
                else if (max.compareTo(points[j]) < 0) {
                    max = points[j];
                }
                count++;
            }
            if (count > 3 && p.compareTo(min) == 0) {
                lineSegments[numberOfSegments++] = new LineSegment(min, max);
            }
        }
    }

    public static void main(String[] args) {
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
