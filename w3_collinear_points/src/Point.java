import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
    }

    public void drawTo(final Point that) {

    }

    /**
     * Compare two points by y-coordinates, breaking ties by x-coordinates.
     */
    public int compareTo(final Point that) {
        if (this.y < that.y) {
            return -1;
        }
        else if (this.y > that.y) {
            return 1;
        }

        if (this.x < that.x) {
            return -1;
        }
        else if (this.x > that.x) {
            return 1;
        }

        return 0;
    }

    public double slopeTo(final Point that) {
        // Treat the slope of a degenerate line segment (between a point and itself) as negative infinity.
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }

        // Treat the slope of a vertical line segment as positive infinity;
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }

        // Treat the slope of a horizontal line segment as positive zero;
        if (this.y == that.y) {
            return +0.0;
        }

        return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compare two points by slopes they make with this point.
     * Formally, the point (x1, y1) is less than the point (x2, y2)
     * if and only if the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0)
     */
    public Comparator<Point> slopeOrder() {
        return (p1, p2) -> {
            double slope1 = slopeTo(p1);
            double slope2 = slopeTo(p2);

            if (slope1 < slope2) {
                return -1;
            }
            else if (slope1 > slope2) {
                return 1;
            }

            return 0;
        };
    }

    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Do not override the equals() or hashCode() methods :(
}
