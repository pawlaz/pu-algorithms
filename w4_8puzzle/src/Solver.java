import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Deque;
import java.util.LinkedList;

public class Solver {
    private final SearchNode solution;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode prev;
        private final int moves;
        private final int priority;

        public SearchNode(final Board board, final SearchNode prevNode) {
            this.board = board;
            this.prev = prevNode;
            this.moves = prevNode == null ? 0 : prevNode.moves + 1;
            this.priority = board.manhattan() + moves;
        }

        public boolean isGoal() {
            return this.board.isGoal();
        }

        public void addNeighboursToQueue(final MinPQ<SearchNode> queue) {
            for (Board neighbor : board.neighbors()) {
                if (prev != null && neighbor.equals(prev.board)) {
                    continue;
                }

                SearchNode node = new SearchNode(neighbor, this);
                queue.insert(node);
            }
        }

        public int compareTo(final SearchNode that) {
            return this.priority - that.priority;
        }
    }

    public Solver(final Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("The initial board can't be null");
        }
        this.solution = findSolution(initial);
    }

    private SearchNode findSolution(final Board initialBoard) {
        MinPQ<SearchNode> solutionPQ = new MinPQ<SearchNode>();
        solutionPQ.insert(new SearchNode(initialBoard, null));

        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        twinPQ.insert(new SearchNode(initialBoard.twin(), null));

        while (true) {
            SearchNode currentNode = solutionPQ.delMin();
            SearchNode twinNode = twinPQ.delMin();

            if (currentNode.isGoal()) {
                return currentNode;
            }

            if (twinNode.isGoal()) {
                return null;
            }

            currentNode.addNeighboursToQueue(solutionPQ);
            twinNode.addNeighboursToQueue(twinPQ);
        }
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        if (isSolvable()) {
            return solution.moves;
        }

        return -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        Deque<Board> solutionNodes = new LinkedList<>();
        SearchNode node = solution;
        while (node != null) {
            solutionNodes.addFirst(node.board);
            node = node.prev;
        }

        return solutionNodes;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
