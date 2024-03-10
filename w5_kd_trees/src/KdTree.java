/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

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
        root = put(root, p, true);
    }

    private Node put(final Node node, final Point2D point, final boolean isVertical) {
        if (node == null) {
            size++;
            return new Node(point, isVertical);
        }

        if (node.isSamePoint(point)) {
            return node;
        }

        int cmp = node.compareTo(point);
        if (cmp > 0) {
            node.left = put(node.left, point, !isVertical);
        }
        else if (cmp < 0) {
            node.right = put(node.right, point, !isVertical);
        }

        return node;
    }

    private Node get(final Node node, final Point2D point) {
        if (node == null) return null;

        if (node.isSamePoint(point)) {
            return node;
        }

        if (node.compareTo(point) > 0) {
            return get(node.left, point);
        }
        else {
            return get(node.right, point);
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

        public Node(final Point2D point, final boolean isVertical) {
            this.point = point;
            this.rect = null; // TODO
            this.isVertical = isVertical;
        }

        public Color getColor() {
            return isVertical ? StdDraw.RED : StdDraw.BLUE;
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
            // TODO: draw lines
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
