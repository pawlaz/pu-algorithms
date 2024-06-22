import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private static final double BORDER_PIXEL_ENERGY = 1000;
    private Picture p;
    private int[][] edgeTo;

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

    private void relax(
            int prevIndex, int currentIndex,
            double energy, int x, int y,
            double[] distTo, double[] prevDistTo
    ) {
        if (prevIndex < 0 || prevIndex >= prevDistTo.length) {
            return;
        }

        double weight = prevDistTo[prevIndex];
        if (distTo[currentIndex] > weight + energy) {
            distTo[currentIndex] = weight + energy;
            edgeTo[x][y] = prevIndex;
        }
    }

    private int findIndexWithMinWeight(double[] distTo) {
        // coordinate with min weight will be a seam end
        double minWeight = Double.POSITIVE_INFINITY;
        int min = 0;

        for (int i = 0; i < distTo.length; i++) {
            double weight = distTo[i];
            if (weight < minWeight) {
                min = i;
                minWeight = weight;
            }
        }

        return min; // x for vertical, y for horizontal
    }

    public int[] findHorizontalSeam() {
        this.edgeTo = new int[this.width()][this.height()];
        double[] distTo = new double[this.height()];

        Arrays.fill(distTo, BORDER_PIXEL_ENERGY);
        for (int x = 1; x < this.width(); x++) {
            double[] prevDistTo = Arrays.copyOf(distTo, distTo.length);
            Arrays.fill(distTo, Double.POSITIVE_INFINITY);

            for (int y = 1; y < this.height(); y++) {
                double energy = energy(x, y);

                relax(y - 1, y, energy, x, y, distTo, prevDistTo);
                relax(y, y, energy, x, y, distTo, prevDistTo);
                relax(y + 1, y, energy, x, y, distTo, prevDistTo);
            }
        }


        int min = findIndexWithMinWeight(distTo);
        int[] seam = new int[this.width()];
        for (int x = this.width() - 1; x >= 0; x--) {
            seam[x] = min;
            min = edgeTo[x][min];
        }

        return seam;
    }

    public int[] findVerticalSeam() {
        this.edgeTo = new int[this.width()][this.height()];
        double[] distTo = new double[this.width()];

        Arrays.fill(distTo, BORDER_PIXEL_ENERGY);
        for (int y = 1; y < this.height(); y++) {
            double[] prevDistTo = Arrays.copyOf(distTo, distTo.length);
            Arrays.fill(distTo, Double.POSITIVE_INFINITY);

            for (int x = 1; x < this.width(); x++) {
                double energy = energy(x, y);

                relax(x - 1, x, energy, x, y, distTo, prevDistTo);
                relax(x, x, energy, x, y, distTo, prevDistTo);
                relax(x + 1, x, energy, x, y, distTo, prevDistTo);
            }
        }

        int min = findIndexWithMinWeight(distTo);
        int[] seam = new int[this.height()];
        for (int y = this.height() - 1; y >= 0; y--) {
            seam[y] = min;
            min = edgeTo[min][y];
        }

        return seam;
    }

    private void validateSeam(int[] seam, int min, int max) {
        if (seam == null || min <= 1 || seam.length != max) {
            throw new IllegalArgumentException("Incorrect seam");
        }
    }

    private void validateSeam(int seamValue, int prevSeamValue, int maxValue) {
        if (Math.abs(seamValue - prevSeamValue) > 1 || seamValue < 0 || seamValue >= maxValue) {
            throw new IllegalArgumentException();
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, height(), width());
        Picture newPicture = new Picture(this.width(), this.height() - 1);

        int prevSeam = seam[0];
        for (int x = 0; x < this.width(); x++) {
            validateSeam(seam[x], prevSeam, height());

            prevSeam = seam[x];
            for (int y = 0; y < this.height(); y++) {
                if (seam[x] == y) {
                    continue;
                }

                Color color = this.p.get(x, y);
                newPicture.set(x, seam[x] > y ? y : y - 1, color);
            }
        }

        this.p = newPicture;
    }

    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, width(), height());
        Picture newPicture = new Picture(this.width() - 1, this.height());

        int prevSeam = seam[0];
        for (int y = 0; y < this.height(); y++) {
            validateSeam(seam[y], prevSeam, width());

            prevSeam = seam[y];
            for (int x = 0; x < this.width(); x++) {
                if (seam[y] == x) {
                    continue;
                }

                Color color = this.p.get(x, y);
                newPicture.set(seam[y] > x ? x : x - 1, y, color);
            }
        }

        this.p = newPicture;
    }

    public static void main(String[] args) {

    }
}
