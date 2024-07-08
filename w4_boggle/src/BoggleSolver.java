import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BoggleSolver {
    private static final int RADIX = 26;
    private final Node root = new Node(); // trie root

    private static class Node {
        private String value;
        private Node[] next = new Node[RADIX];
    }

    private class WordsBuilder {
        private final BoggleBoard board;
        private final int[] flatBoard;
        private final int verticesCount;
        private final Map<Integer, List<Integer>> graph;
        private Set<String> words = null;

        private WordsBuilder(final BoggleBoard board) {
            this.board = board;
            this.verticesCount = board.rows() * board.cols();
            this.flatBoard = new int[verticesCount];
            this.graph = new HashMap<>();

            // build graph
            for (int i = 0; i < board.rows(); i++) {
                for (int j = 0; j < board.cols(); j++) {
                    int index = flatIndex(i, j);
                    flatBoard[index] = charIndex(board.getLetter(i, j));
                    graph.put(index, new LinkedList<>());

                    // check all possible neighbours
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int m = j - 1; m <= j + 1; m++) {
                            if (k == i && m == j) {
                                continue;
                            }

                            if (k >= 0 && m >= 0 && k < board.rows() && m < board.cols()) {
                                int neigbourIndex = flatIndex(k, m);
                                graph.get(index).add(neigbourIndex);
                            }
                        }
                    }
                }
            }
        }

        private int flatIndex(int row, int col) {
            return row * board.cols() + col;
        }

        private void dfs(int s, int t, final Node x, final boolean[] marked) {
            Node current = x;

            // special Qu case (2 chars treated as 1)
            if (current != null && flatBoard[s] == charIndex('Q')) {
                current = current.next[charIndex('U')];
            }

            if (current == null) {
                return; // no prefix found, we can stop the search
            }

            marked[s] = true;
            if (s == t) { // found path from s to t
                if (current.value != null) {
                    words.add(current.value);
                }
            }
            else {
                for (int v : graph.get(s)) {
                    if (!marked[v]) {
                        dfs(v, t, current.next[flatBoard[v]], marked);
                    }
                }
            }
            // all paths are explored, return to pool
            marked[s] = false;
        }

        private Set<String> findAllWords() {
            if (words != null) {
                return words;
            }

            words = new HashSet<>();
            // run dfs to find all possible words
            for (int i = 0; i < verticesCount; i++) {
                for (int j = 0; j < verticesCount; j++) {
                    if (i == j) {
                        continue;
                    }

                    // dfs find all paths from i to j
                    boolean[] marked = new boolean[verticesCount];
                    dfs(i, j, root.next[flatBoard[i]], marked);
                }
            }
            return words;
        }
    }

    public BoggleSolver(final String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("The dictionary can't be null");
        }
        for (String word : dictionary) {
            if (word.length() >= 3) {
                put(root, word, 0);
            }
        }
    }

    private int charIndex(char c) {
        return c - 'A'; // only A-Z are expected
    }

    private Node put(Node x, final String word, int d) {
        if (x == null) {
            x = new Node();
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

    private boolean contains(final String word) {
        Node x = get(root, word, 0);
        return x != null && x.value != null;
    }


    public Iterable<String> getAllValidWords(final BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("Board can't be null");
        }
        WordsBuilder wb = new WordsBuilder(board);
        return wb.findAllWords();
    }

    public int scoreOf(final String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word can't be null");
        }
        if (!contains(word) || word.length() < 3) {
            return 0;
        }

        switch (word.length()) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
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
