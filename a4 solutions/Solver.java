import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private MinPQ<SearchNode> mpq;
    private int initialPriority;
    private boolean isSolvable = true;
    private SearchNode goalNode;

    private class SearchNode implements Comparable<SearchNode> {
        private int moves;
        private SearchNode prevNode;
        private Board board;
        private int priority;

        public SearchNode(Board b, SearchNode prevN) {
            board = b;
            prevNode = prevN;
            if (prevNode == null) {
                moves = 0;
            }
            else {
                moves += prevN.moves + 1;
            }
            priority = board.manhattan() + moves;
        }

        public void trace(Stack<Board> s) {

            s.push(this.board);
            if (prevNode != null) {
                prevNode.trace(s);
            }
        }

        public int compareTo(SearchNode o) {
            return Integer.compare(this.priority, o.priority);
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null argument");
        }
        mpq = new MinPQ<SearchNode>();
        initialPriority = initial.manhattan();
        SearchNode initialNode = new SearchNode(initial, null);
        mpq.insert(initialNode);
        while (isSolvable) {
            SearchNode minNode = mpq.delMin();
            if (minNode.board.isGoal()) {
                goalNode = minNode;
                break;
            }

            if (minNode.board.twin().isGoal()) {
                isSolvable = false;
            }

            Iterable<Board> neighbours = minNode.board.neighbors();
            for (Board neighbour : neighbours) {
                if (minNode.prevNode == null || !neighbour.equals(minNode.prevNode.board)) {
                    SearchNode newNode = new SearchNode(neighbour, minNode);
                    mpq.insert(newNode);
                }

            }


        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        else {
            return initialPriority;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        else {
            Stack<Board> nodeStack = new Stack<Board>();
            //initiate traceback
            goalNode.trace(nodeStack);
            return nodeStack;
        }
    }

    // test client (see below)
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
