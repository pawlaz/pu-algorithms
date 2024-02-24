import java.util.Arrays;

public class BruteCollinearPoints {
    private static final Point ZERO = new Point(0, 0);
    private Point[] originalPoints;
    private LineSegment[] lineSegments = new LineSegment[0];
    private int numberOfSegments = 0;

    // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
    public BruteCollinearPoints(final Point[] points) {
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

    private LineSegment collinearLineSegment(final Point... points) {
        Arrays.sort(points, ZERO.slopeOrder());

        Point min;
        Point max;

        if (points[0].compareTo(points[points.length - 1]) < 0) {
            min = points[0];
            max = points[points.length - 1];
        }
        else {
            min = points[points.length - 1];
            max = points[0];
        }

        double prevSlope = Double.NEGATIVE_INFINITY; // just to avoid null and wrapping
        for (int i = 0; i < points.length - 1; i++) {
            Point p1 = points[i];
            Point p2 = points[i + 1];
            double slope = p1.slopeTo(p2);

            if (slope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Duplicated point " + p1.toString());
            }

            boolean isCurve = !(p2.compareTo(min) >= 0 && p2.compareTo(max) <= 0);

            // not ascending order (remove duplicates) / no line segment
            if (isCurve || (prevSlope != Double.NEGATIVE_INFINITY
                    && Double.compare(prevSlope, slope) != 0)) {
                return null;
            }
            prevSlope = slope;
        }
        return new LineSegment(points[0], points[points.length - 1]);
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

        for (int i = 0; i < this.originalPoints.length - 3; i++) {
            for (int j = i + 1; j < this.originalPoints.length - 2; j++) {
                for (int k = j + 1; k < this.originalPoints.length - 1; k++) {
                    for (int l = k + 1; l < this.originalPoints.length; l++) {
                        Point p1 = this.originalPoints[i];
                        Point p2 = this.originalPoints[j];
                        Point p3 = this.originalPoints[k];
                        Point p4 = this.originalPoints[l];

                        LineSegment segment = collinearLineSegment(p1, p2, p3, p4);
                        if (segment != null) {
                            this.lineSegments[this.numberOfSegments++] = segment;
                        }
                    }
                }
            }
        }

        LineSegment[] shrinkedArray = new LineSegment[this.numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            shrinkedArray[i] = this.lineSegments[i];
        }
        return shrinkedArray;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(3, 3);
        Point p4 = new Point(4, 4);
        Point p5 = new Point(3, 5);
        Point p6 = new Point(2, 4);
        Point p7 = new Point(3, 1);
        Point p8 = new Point(5, 1);
        Point p9 = new Point(4, 2);
        Point p10 = new Point(5, 3);
        Point p11 = new Point(6, 4);
        Point p12 = new Point(6, 3);
        Point p13 = new Point(6, 2);
        Point p14 = new Point(6, 1);
        Point[] points = { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14 };
        BruteCollinearPoints b = new BruteCollinearPoints(points);
        for (LineSegment s : b.segments()) {
            System.out.println(s);
        }
    }
}
