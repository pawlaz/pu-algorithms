import java.util.Arrays;

public class BruteCollinearPoints {
    private static final Point ZERO = new Point(0, 0);
    private Point[] points;
    private LineSegment[] lineSegments = new LineSegment[0];
    private int numberOfSegments = 0;

    // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
    public BruteCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is empty");
        }

        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point can't be null");
            }
            this.points[i] = points[i];
        }

        Arrays.sort(this.points);
        for (int i = 0; i < points.length; i++) {
            if (i < points.length - 1 && points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicated point: " + points[i]);
            }
        }
    }

    private LineSegment collinearLineSegment(final Point... pointsGroup) {
        Arrays.sort(pointsGroup, ZERO.slopeOrder());

        Point min;
        Point max;

        if (pointsGroup[0].compareTo(pointsGroup[pointsGroup.length - 1]) < 0) {
            min = pointsGroup[0];
            max = pointsGroup[pointsGroup.length - 1];
        }
        else {
            min = pointsGroup[pointsGroup.length - 1];
            max = pointsGroup[0];
        }

        double prevSlope = Double.NEGATIVE_INFINITY; // just to avoid null and wrapping
        for (int i = 0; i < pointsGroup.length - 1; i++) {
            Point p1 = pointsGroup[i];
            Point p2 = pointsGroup[i + 1];
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
        return new LineSegment(pointsGroup[0], pointsGroup[pointsGroup.length - 1]);
    }

    public int numberOfSegments() {
        return this.numberOfSegments;
    }

    private LineSegment[] shrinkResult() {
        LineSegment[] shrinkedArray = new LineSegment[this.numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            shrinkedArray[i] = this.lineSegments[i];
        }
        return shrinkedArray;
    }

    // The method segments() should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        if (this.points.length < 4) {
            return new LineSegment[0];
        }

        if (this.lineSegments.length != 0) {
            return shrinkResult();
        }

        this.lineSegments = new LineSegment[this.points.length * 2];
        for (int i = 0; i < this.points.length - 3; i++) {
            for (int j = i + 1; j < this.points.length - 2; j++) {
                for (int k = j + 1; k < this.points.length - 1; k++) {
                    for (int l = k + 1; l < this.points.length; l++) {
                        Point p1 = this.points[i];
                        Point p2 = this.points[j];
                        Point p3 = this.points[k];
                        Point p4 = this.points[l];

                        LineSegment segment = collinearLineSegment(p1, p2, p3, p4);
                        if (segment != null) {
                            this.lineSegments[this.numberOfSegments++] = segment;
                        }
                    }
                }
            }
        }
        return shrinkResult();
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
