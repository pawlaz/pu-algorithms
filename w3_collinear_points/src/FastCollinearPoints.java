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
    private Point[] originalPoints;
    private LineSegment[] lineSegments = new LineSegment[0];
    private int numberOfSegments = 0;

    // should work properly even if the input has 5 or more collinear points
    public FastCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is empty");
        }

        Arrays.sort(points);
        this.originalPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point can't be null");
            }

            this.originalPoints[i] = points[i];

            if (i < points.length - 1 && points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicated point: " + points[i]);
            }
        }
    }

    public int numberOfSegments() {
        return this.numberOfSegments;
    }

    // The method segments() should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        if (this.originalPoints.length < 4) {
            return new LineSegment[0];
        }

        if (this.lineSegments.length == 0) {
            this.lineSegments = new LineSegment[this.originalPoints.length * 2];
        }

        for (int i = 0; i < this.originalPoints.length; i++) {
            Point p = this.originalPoints[i];
            Arrays.sort(this.originalPoints, p.slopeOrder());

            int duplicates = -1;
            int count = 0;
            Point min = p;
            Point max = p;
            double initSlope = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < this.originalPoints.length; j++) { // TODO more than 5
                double slope = p.slopeTo(this.originalPoints[j]);
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
                    if (min.compareTo(this.originalPoints[j]) > 0) {
                        min = this.originalPoints[j];
                    }
                    else if (max.compareTo(this.originalPoints[j]) < 0) {
                        max = this.originalPoints[j];
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
        return shrinkedArray;
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
