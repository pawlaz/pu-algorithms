import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int RADIX = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        while (!BinaryStdIn.isEmpty()) {
            String s = BinaryStdIn.readString();
            CircularSuffixArray csa = new CircularSuffixArray(s);

            for (int i = 0; i < csa.length(); i++) {
                if (csa.index(i) == 0) {
                    BinaryStdOut.write(i);
                    break;
                }
            }
            for (int i = 0; i < csa.length(); i++) {
                int index = csa.index(i) - 1;
                if (index < 0) {
                    index = s.length() - 1;
                }
                BinaryStdOut.write(s.charAt(index));
            }
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static char[] buildSorted(final char[] t) {
        char[] sorted = new char[t.length];
        int[] counts = new int[RADIX + 1];

        for (int i = 0; i < t.length; i++) {
            counts[t[i] + 1]++;
        }
        for (int i = 0; i < RADIX; i++) {
            counts[i + 1] += counts[i];
        }
        for (int i = 0; i < t.length; i++) {
            sorted[counts[t[i]]++] = t[i];
        }

        return sorted;
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        while (!BinaryStdIn.isEmpty()) {
            int first = BinaryStdIn.readInt();
            String s = BinaryStdIn.readString();

            char[] t = s.toCharArray();
            int[] next = new int[t.length];
            char[] sorted = buildSorted(t);
            int[] counts = new int[RADIX + 1];

            for (int i = 0; i < t.length; i++) {
                counts[sorted[i] + 1]++;
            }
            for (int i = 0; i < RADIX; i++) {
                counts[i + 1] += counts[i];
            }
            for (int i = 0; i < t.length; i++) {
                int index = counts[t[i]]++;
                next[index] = i;
            }

            for (int i = 0, index = first; i < t.length; i++, index = next[index]) {
                BinaryStdOut.write(sorted[index]);
            }
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        String key = args[0];
        switch (key) {
            case "-":
                transform();
                break;
            case "+":
                inverseTransform();
                break;
            default:
                throw new IllegalArgumentException("Unknown key: " + key);
        }
    }
}
