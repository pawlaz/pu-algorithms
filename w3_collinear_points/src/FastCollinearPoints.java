/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;
    private int numberOfSegments;

    // should work properly even if the input has 5 or more collinear points
    public FastCollinearPoints(final Point[] points) {
        this.numberOfSegments = 0;
        this.lineSegments = new LineSegment[points.length * 2];
        calculateSegments(points);
    }

    private void calculateSegments(final Point[] points) {
        // TODO: corner cases
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Arrays.sort(points, p.slopeOrder());

            int duplicates = -1;
            int count = 0;
            Point min = p;
            Point max = p;
            double initSlope = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < points.length; j++) { // TODO more than 5
                double slope = p.slopeTo(points[j]);
                if (Double.compare(initSlope, Double.NEGATIVE_INFINITY) == 0) {
                    initSlope = slope;
                }
                if (Double.compare(slope, Double.NEGATIVE_INFINITY) == 0 && ++duplicates > 0) {
                    throw new IllegalArgumentException();
                }

                if (Double.compare(initSlope, slope) != 0) {
                    if (count >= 3 && p.compareTo(min) == 0) {
                        lineSegments[numberOfSegments++] = new LineSegment(min, max);
                    }
                    initSlope = Double.NEGATIVE_INFINITY;
                    count = 0;
                    min = p;
                    max = p;
                }
                else {
                    if (min.compareTo(points[j]) > 0) {
                        min = points[j];
                    }
                    else if (max.compareTo(points[j]) < 0) {
                        max = points[j];
                    }
                    count++;
                }
            }
            if (count >= 3 && p.compareTo(min) == 0) {
                lineSegments[numberOfSegments++] = new LineSegment(min, max);
            }
        }

        LineSegment[] shrinkedArray = new LineSegment[this.numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            shrinkedArray[i] = this.lineSegments[i];
        }
        this.lineSegments = shrinkedArray;
    }

    public int numberOfSegments() {
        return this.numberOfSegments;
    }

    // The method segments() should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        return this.lineSegments;
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
