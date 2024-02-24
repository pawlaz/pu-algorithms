public class LineSegment {
    private final Point p;
    private final Point q;

    public LineSegment(Point p, Point q) {
        this.p = p;
        this.q = q;
    }

    public void draw() {

    }

    public String toString() {
        return "LineSegment{" +
                "p=" + p +
                ", q=" + q +
                '}';
    }
}
