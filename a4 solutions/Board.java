import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] myTiles;
    private final int boardSize;

    public Board(int[][] tiles) {
        boardSize = tiles.length;
        myTiles = createTiles(tiles);
    }

    //returns a deep copied set of 2D tiles
    private int[][] createTiles(int[][] oldTiles) {
        int dim = oldTiles.length;
        int[][] newTiles = new int[dim][dim];
        for (int i = 0; i < oldTiles.length; ++i) {
            for (int j = 0; j < oldTiles[i].length; ++j) {
                newTiles[i][j] = oldTiles[i][j];
            }
        }
        return newTiles;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(boardSize + "\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                s.append(String.format("%2d ", myTiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return boardSize;

    }

    // number of tiles out of place
    public int hamming() {
        int out = 0;
        for (int i = 0; i < myTiles.length; ++i) {
            for (int j = 0; j < myTiles[i].length; ++j) {
                //0th tile
                if (i == boardSize - 1 && j == boardSize - 1) {
                    continue;
                }
                if (((boardSize * i) + j + 1) != myTiles[i][j]) {
                    ++out;
                }
            }
        }
        return out;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int totalDist = 0;
        for (int i = 0; i < myTiles.length; ++i) {
            for (int j = 0; j < myTiles[i].length; ++j) {
                int tileNum = myTiles[i][j];
                //skip if we encounter 0 tile
                if (tileNum == 0) {
                    continue;
                }
                double tmp = (double) tileNum / boardSize;
                //find correct idices for tile
                int rowIdx = (int) Math.ceil(tmp) - 1;
                int colIdx = tileNum - (boardSize * rowIdx) - 1;
                int tileDist = Math.abs(rowIdx - i) + Math.abs(colIdx - j);
                totalDist += tileDist;
            }
        }
        return totalDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0 && hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != dimension()) return false;
        return Arrays.deepEquals(this.myTiles, that.myTiles);
    }

    private Boolean outOfBounds(int idx) {
        return idx < 0 || idx >= boardSize;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> boardStack = new Stack<Board>();
        //get pos of blank square
        int bi = 0;
        int bj = 0;
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < myTiles[i].length; ++j) {
                if (myTiles[i][j] == 0) {
                    bi = i;
                    bj = j;
                }
            }
        }
        //check over 4 possible positions neigbours can be
        if (!outOfBounds(bi - 1)) {
            int[][] newTiles = createTiles(myTiles);
            int tmp = newTiles[bi - 1][bj];
            newTiles[bi - 1][bj] = 0;
            newTiles[bi][bj] = tmp;
            Board newBoard = new Board(createTiles(newTiles));
            boardStack.push(newBoard);
        }
        if (!outOfBounds(bi + 1)) {
            int[][] newTiles = createTiles(myTiles);
            int tmp = newTiles[bi + 1][bj];
            newTiles[bi + 1][bj] = 0;
            newTiles[bi][bj] = tmp;
            Board newBoard = new Board(createTiles(newTiles));
            boardStack.push(newBoard);
        }
        if (!outOfBounds(bj - 1)) {
            int[][] newTiles = createTiles(myTiles);
            int tmp = newTiles[bi][bj - 1];
            newTiles[bi][bj - 1] = 0;
            newTiles[bi][bj] = tmp;
            Board newBoard = new Board(createTiles(newTiles));
            boardStack.push(newBoard);
        }
        if (!outOfBounds(bj + 1)) {
            int[][] newTiles = createTiles(myTiles);
            int tmp = newTiles[bi][bj + 1];
            newTiles[bi][bj + 1] = 0;
            newTiles[bi][bj] = tmp;
            Board newBoard = new Board(createTiles(newTiles));
            boardStack.push(newBoard);
        }

        return boardStack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles = createTiles(myTiles);
        //exchange
        if (newTiles[0][0] == 0 || newTiles[0][1] == 0) {
            int tmp = newTiles[1][1];
            newTiles[1][1] = newTiles[1][0];
            newTiles[1][0] = tmp;
        }
        //we know 0,0 and 0,1 are clear, so swap those
        else {
            int tmp = newTiles[0][1];
            newTiles[0][1] = newTiles[0][0];
            newTiles[0][0] = tmp;
        }
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int dim = 3;
        int[][] sampleTiles = new int[dim][dim];
        for (int i = 0; i < sampleTiles.length; ++i) {
            for (int j = 0; j < sampleTiles[i].length; ++j) {
                sampleTiles[i][j] = dim * i + j + 2;
            }
        }
        sampleTiles[dim - 1][dim - 1] = 0;
        Board sampleBoard = new Board(sampleTiles);
        StdOut.println("Hamming:" + sampleBoard.hamming());
        StdOut.println("Manhattan:" + sampleBoard.manhattan());
        StdOut.println("Board:");
        StdOut.println(sampleBoard.toString());
        StdOut.println("Sample Neighbours:");
        Iterable<Board> sampleNeighbours = sampleBoard.neighbors();
        for (Board b :
                sampleNeighbours) {
            StdOut.println(b.toString());
        }
        StdOut.println("Sample Twin:");
        StdOut.println(sampleBoard.twin());

    }
}


