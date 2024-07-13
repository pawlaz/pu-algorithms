import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final Integer[] indices;

    // circular suffix array of s
    public CircularSuffixArray(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("String can't be null");
        }
        this.indices = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (i1, i2) -> {
            for (int i = 0; i < s.length(); i++) {
                int index1 = (i1 + i) % s.length();
                int index2 = (i2 + i) % s.length();
                int result = s.charAt(index1) - s.charAt(index2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        });
    }

    // length of s
    public int length() {
        return indices.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.indices.length) {
            throw new IllegalArgumentException("Incorrect index: " + i);
        }
        return this.indices[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);
        for (int i = 0; i < csa.length(); i++) {
            StdOut.print(csa.index(i) + " ");
        }
        StdOut.println("");
    }
}
