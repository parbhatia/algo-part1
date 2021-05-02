import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int count;
    private int boardsize;
    private WeightedQuickUnionUF unionFind;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        boardsize = n;
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[n][n];
        //create unionFind with two extra tiles for virtual top and virtual bottom tiles
        //n is top
        //n+1 is bottom
        unionFind = new WeightedQuickUnionUF(n * n + 2);
    }

    //checks if user has provided correct placements [1,n]
    private void checkBounds(int row, int col) {
        if (!(row >= 1 && col >= 1 && row <= boardsize && col <= boardsize)) {
            throw new IllegalArgumentException("Invalid bound" + "row: " + row + "col: " + col);
        }
    }

    //checks if indices are not below 0 or above boardSize [0,n-1]
    private boolean validRange(int row, int col) {
        return row >= 0 && col >= 0 && row < boardsize && col < boardsize;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);
        int r = row - 1;
        int c = col - 1;
        if (!grid[r][c]) {
            grid[r][c] = true;
            ++count;
            //check if top or bottom row
            if (row == 1) {
                unionFind.union(boardsize * boardsize, boardsize * r + c);
            }
            if (row == boardsize) {
                unionFind.union(boardsize * boardsize + 1, boardsize * r + c);
            }

            //connect newly opened site to four adjacent sides using union
            //top
            if (validRange(r - 1, c) && isOpen(row - 1, col)) {
                unionFind.union(boardsize * r + c, boardsize * (r - 1) + c);
            }
            //bottom
            if (validRange(r + 1, c) && isOpen(row + 1, col)) {
                unionFind.union(boardsize * r + c, boardsize * (r + 1) + c);
            }
            //left
            if (validRange(r, c - 1) && isOpen(row, col - 1)) {
                unionFind.union(boardsize * r + c, boardsize * r + (c - 1));
            }
            //right
            if (validRange(r, c + 1) && isOpen(row, col + 1)) {
                unionFind.union(boardsize * r + c, boardsize * r + (c + 1));
            }
        }
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        return unionFind.find(boardsize * boardsize) == unionFind
                .find(boardsize * (row - 1) + (col - 1));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        //check if bottom virtual site's parent is top virtual site
        return unionFind.find(boardsize * boardsize) == unionFind.find(boardsize * boardsize + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        StdOut.println("test");
    }
}
