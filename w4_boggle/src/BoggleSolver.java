import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {
    private static final int RADIX = 26;
    private final Set<String> words = new TreeSet<>();
    private Node root = new Node(); // trie root

    private static class Node {
        private String value;
        private Node[] next = new Node[RADIX];
    }

    public BoggleSolver(final String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("The dictionary can't be null");
        }
        for (String word : dictionary) {
            this.words.add(word);
            put(root, word, 0);
        }
    }

    private int charIndex(char c) {
        return c - 'A'; // only A-Z are expected
    }

    private Node put(final Node x, final String word, int d) {
        if (x == null) {
            return new Node();
        }

        if (word.length() == d) {
            x.value = word;
            return x;
        }

        int index = charIndex(word.charAt(d));
        x.next[index] = put(x.next[index], word, d + 1);
        return x;
    }

    private Node get(final Node x, final String word, int d) {
        if (x == null) {
            return null;
        }
        if (word.length() == d) {
            return x;
        }
        return get(x.next[charIndex(word.charAt(d))], word, d + 1);
    }

    public Iterable<String> getAllValidWords(final BoggleBoard board) {
        return null;
    }

    public int scoreOf(final String word) {
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
