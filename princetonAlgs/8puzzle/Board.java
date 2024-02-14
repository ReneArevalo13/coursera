public class Board {

    private int[] board;
    private int width;
    private int offset = 1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        if (tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("Not an NxN grid.");
        }
        int width = tiles.length;
        board = new int[width * width];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                board[xyToArrayPos(i, j)] = tiles[i][j];
            }
        }
    }

    /**
     * Helper method to convert a point on the grid (x,y) into a position on the
     * array. Grid is NxN and array is of size N^2.
     *
     * @param x row position
     * @param y column position
     * @return the corresponding array position
     */
    private int xyToArrayPos(int x, int y) {
        return (x * width) + y;
    }

    // string representation of this board
    public String toString() {

    }

    // board dimension n
    public int dimension() {

    }

    // number of tiles out of place
    public int hamming() {

    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

    }

    // is this board the goal board?
    public boolean isGoal() {

    }

    // does this board equal y?
    public boolean equals(Object y) {

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
