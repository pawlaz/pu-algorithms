import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Iterable<Board> solution;
    private int moves = 0;

    public Solver(final Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("The initial board can't be null");
        }
        this.solution = findSolution(initial);
    }

    private int prioritiesCompare(final Board b1, final Board b2) {
        return Integer.compare(b1.manhattan() + moves, b2.manhattan() + moves);
    }

    private Iterable<Board> findSolution(final Board initialBoard) {
        MinPQ<Board> minPQ = new MinPQ<Board>(this::prioritiesCompare);
        minPQ.insert(initialBoard);

        MinPQ<Board> twinMinPQ = new MinPQ<Board>(this::prioritiesCompare);
        twinMinPQ.insert(initialBoard.twin());

        Queue<Board> currentSolution = new Queue<>();

        Board minBoard = null;
        Board twinMinBoard = null;
        Board prevBoard = null;
        Board twinPrevBoard = null;
        do {
            this.moves++;
            if (minBoard != null) {
                prevBoard = minBoard;
            }
            minBoard = minPQ.delMin();
            for (Board bn : minBoard.neighbors()) {
                if (prevBoard != null && prevBoard.equals(bn)) {
                    continue;
                }
                minPQ.insert(bn);
            }
            currentSolution.enqueue(minBoard);

            if (twinMinBoard != null) {
                twinPrevBoard = twinMinBoard;
            }
            twinMinBoard = twinMinPQ.delMin();
            for (Board bn : twinMinBoard.neighbors()) {
                if (twinPrevBoard != null && twinPrevBoard.equals(bn)) {
                    continue;
                }
                twinMinPQ.insert(bn);
            }
        } while (!minBoard.isGoal() && !twinMinBoard.isGoal());

        if (minBoard.isGoal()) {
            return currentSolution;
        }

        return null;
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        if (isSolvable()) {
            return moves;
        }

        return -1;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
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
