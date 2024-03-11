/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

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

        if (current.compareTo(point) > 0) {
            current.left = put(current.left, point, !isVertical, current);
        }
        else {
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
        return findPointsInRange(root, rect, new LinkedList<>());
    }

    // pruning rule: if the query rectangle does not intersect the rectangle corresponding to a node,
    // there is no need to explore that node (or its subtrees). A subtree is searched only
    // if it might contain a point contained in the query rectangle.
    private List<Point2D> findPointsInRange(
            final Node current,
            final RectHV rect,
            final List<Point2D> acc
    ) {
        if (current == null) {
            return acc;
        }
        if (rect.intersects(current.rect)) {
            if (rect.contains(current.point)) {
                acc.add(current.point);
            }
            findPointsInRange(current.left, rect, acc);
            findPointsInRange(current.right, rect, acc);
        }
        return acc;
    }

    public Point2D nearest(final Point2D p) {
        validateArgs(p);
        if (isEmpty()) {
            return null;
        }
        return findNearestPoint(root, p, root.point);
    }

    private Point2D findNearestPoint(
            final Node current,
            final Point2D p,
            final Point2D closestPrev
    ) {
        if (current == null) {
            return closestPrev;
        }
        Point2D closest = closestPrev;
        double closestDistance = closestPrev.distanceSquaredTo(p);
        // pruning rule: if the closest point discovered so far is closer than the distance between
        // the query point and the rectangle corresponding to a node, there is no need to explore that
        // node (or its subtrees). That is, search a node only if it might contain a point
        // that is closer than the best one found so far.
        if (current.rect.distanceSquaredTo(p) > closestDistance) {
            return closestPrev;
        }
        double currentDistance = current.point.distanceSquaredTo(p);
        if (currentDistance < closestDistance) {
            closest = current.point;
        }

        // The effectiveness of the pruning rule depends on quickly finding a nearby point.
        // To do this, organize the recursive method so that when there are two possible subtrees to go down,
        // you always choose the subtree that is on the same side of the splitting line as the query point as
        // the first subtree to explore—the closest point found while exploring the first subtree may
        // enable pruning of the second subtree.
        if (current.compareTo(p) > 0) {
            closest = findNearestPoint(current.left, p, closest);
            closest = findNearestPoint(current.right, p, closest);
        }
        else {
            closest = findNearestPoint(current.right, p, closest);
            closest = findNearestPoint(current.left, p, closest);
        }

        return closest;
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
