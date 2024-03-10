import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {
    private final TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    private void validateArgs(final Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("The argument of the function can't be null");
            }
        }
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(final Point2D p) {
        validateArgs(p);
        points.add(p);
    }

    public boolean contains(final Point2D p) {
        validateArgs(p);
        return points.contains(p);
    }

    public void draw() {
        points.parallelStream().forEach(Point2D::draw);
    }

    public Iterable<Point2D> range(final RectHV rect) {
        validateArgs(rect);
        return points.parallelStream().filter(rect::contains).collect(Collectors.toList());
    }

    public Point2D nearest(final Point2D p) {
        validateArgs(p);
        Point2D nearestPoint = null;
        double shortestDistance = 0;
        for (Point2D point : points) {
            double distance = p.distanceSquaredTo(point);
            if (nearestPoint == null || Double.compare(distance, shortestDistance) < 0) {
                nearestPoint = point;
                shortestDistance = distance;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {

    }
}
