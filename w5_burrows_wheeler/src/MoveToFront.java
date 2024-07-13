import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int RADIX = 256; // ASKII

    private static char[] initSequence() {
        char[] sequence = new char[RADIX];
        for (int i = 0; i < RADIX; i++) {
            sequence[i] = (char) i;
        }
        return sequence;
    }

    private static void moveToFront(final char[] sequence, char c, int i) {
        for (int j = i; j > 0; j--) {
            sequence[j] = sequence[j - 1];
        }
        sequence[0] = c;
    }

    private static int findCharacterIndex(final char[] sequence, char c) {
        int i;

        for (i = 0; i < sequence.length; i++) {
            if (sequence[i] == c) {
                return i;
            }
        }

        return sequence.length - 1;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] sequence = initSequence();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i = findCharacterIndex(sequence, c);
            BinaryStdOut.write((char) i);
            moveToFront(sequence, c, i);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] sequence = initSequence();
        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            char c = sequence[i];
            BinaryStdOut.write(c);
            moveToFront(sequence, c, i);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String key = args[0];
        switch (key) {
            case "-":
                encode();
                break;
            case "+":
                decode();
                break;
            default:
                throw new IllegalArgumentException("Unknown key: " + key);
        }
    }
}
