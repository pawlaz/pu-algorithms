import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private final Picture p;

    public SeamCarver(final Picture picture) {
        p = new Picture(picture);
    }

    public Picture picture() {
        return p;
    }

    public int width() {
        return p.width();
    }

    public int height() {
        return p.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return 0;
    }

    public int[] findHorizontalSeam() {
        return new int[0];
    }

    public int[] findVerticalSeam() {
        return new int[0];
    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

    }

    public static void main(String[] args) {

    }
}
