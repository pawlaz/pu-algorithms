/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root = null;
    private int size = 0;

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(final Point2D p) {
        validateArgs(p);
        root = put(root, p, true, null);
    }

    private Node put(
            final Node current, final Point2D point,
            final boolean isVertical, final Node prev
    ) {
        if (current == null) {
            size++;
            return new Node(point, isVertical, prev);
        }

        if (current.isSamePoint(point)) {
            return current;
        }

        int cmp = current.compareTo(point);
        if (cmp > 0) {
            current.left = put(current.left, point, !isVertical, current);
        }
        else if (cmp < 0) {
            current.right = put(current.right, point, !isVertical, current);
        }

        return current;
    }

    private Node get(final Node current, final Point2D point) {
        if (current == null) return null;

        if (current.isSamePoint(point)) {
            return current;
        }

        if (current.compareTo(point) > 0) {
            return get(current.left, point);
        }
        else {
            return get(current.right, point);
        }
    }

    private void validateArgs(final Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("The argument of the function can't be null");
            }
        }
    }

    public boolean contains(final Point2D p) {
        validateArgs(p);
        return get(root, p) != null;
    }

    public void draw() {
        if (root != null) {
            root.draw();
        }
    }

    public Iterable<Point2D> range(final RectHV rect) {
        validateArgs(rect);
        return null; // TODO

    }

    public Point2D nearest(final Point2D p) {
        validateArgs(p);
        return null; // TODO
    }

    public static void main(String[] args) {

    }

    private class Node implements Comparable<Point2D> {
        private final Point2D point;
        private final RectHV rect;
        private final boolean isVertical;
        private Node left = null;
        private Node right = null;

        public Node(final Point2D point, final boolean isVertical, final Node parent) {
            this.point = point;
            this.isVertical = isVertical;
            if (parent == null) {
                // all x- or y-coordinates of points inserted into the KdTree will be between 0 and 1
                this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            }
            else {
                double minX = parent.rect.xmin();
                double minY = parent.rect.ymin();
                double maxX = parent.rect.xmax();
                double maxY = parent.rect.ymax();

                int cmp = parent.compareTo(point);
                if (isVertical) {
                    if (cmp > 0) {
                        maxY = parent.point.y();
                    }
                    else {
                        minY = parent.point.y();
                    }
                }
                else {
                    if (cmp > 0) {
                        maxX = parent.point.x();
                    }
                    else {
                        minX = parent.point.x();
                    }
                }

                this.rect = new RectHV(minX, minY, maxX, maxY);
            }
        }

        public boolean isSamePoint(final Point2D pointToCheck) {
            return this.point.compareTo(pointToCheck) == 0;
        }

        public int compareTo(final Point2D thatPoint) {
            if (isVertical) {
                return Double.compare(this.point.x(), thatPoint.x());
            }
            else {
                return Double.compare(this.point.y(), thatPoint.y());
            }
        }

        public void draw() {
            StdDraw.setPenRadius(0.001);
            if (isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
            }

            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();
            if (left != null) {
                left.draw();
            }

            if (right != null) {
                right.draw();
            }
        }
    }
}
