import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final double BORDER_PIXEL_ENERGY = 1000;
    private final Picture p;

    public SeamCarver(final Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("The picture can't be null");
        }
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

    private double calcGradient(final Color c1, final Color c2) {
        return Math.pow(c1.getRed() - c2.getRed(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2)
                + Math.pow(c1.getBlue() - c2.getBlue(), 2);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException("Invalid range");
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return BORDER_PIXEL_ENERGY;
        }

        Color top = p.get(x, y + 1);
        Color bottom = p.get(x, y - 1);
        Color right = p.get(x + 1, y);
        Color left = p.get(x - 1, y);

        return Math.sqrt(calcGradient(right, left) + calcGradient(top, bottom));
    }

    public int[] findHorizontalSeam() {
        return new int[0];
    }

    public int[] findVerticalSeam() {
        return new int[0];
    }

    private void validateSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("The seam can't be null");
        }

        // TODO
        // Throw an IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with an array of the wrong length
        // or if the array is not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).
        // Throw an IllegalArgumentException if removeVerticalSeam() is called when the width of the picture is less than or equal to 1
        // or if removeHorizontalSeam() is called when the height of the picture is less than or equal to 1.
    }

    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam);
    }

    public static void main(String[] args) {

    }
}
