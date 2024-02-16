import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

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

        width = tiles.length;
        board = new int[width * width];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                board[xyToArrayPos(i, j)] = tiles[i][j];
            }
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(width + "\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                int p = board[xyToArrayPos(i, j)];
                s.append(String.format("%2d ", p));
            }
            s.append("\n");
        }

        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return width;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < board.length; i++) {
            if (board[i] != i + offset && board[i] != 0) {
                hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        int x0, y0, x1, y1;

        // go through the points and see who is out of position (hamming)
        for (int i = 0; i < board.length; i++) {
            if (board[i] != i + offset && board[i] != 0) {
                // target grid point
                x0 = xyFromArrayPos(i)[0];
                y0 = xyFromArrayPos(i)[1];
                // grid point from associated current board
                x1 = xyFromArrayPos(board[i] - offset)[0];
                y1 = xyFromArrayPos(board[i] - offset)[1];
                manhattan += (Math.abs(x0 - x1) + Math.abs(y0 - y1));
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + offset) {
                return false;
            }
        }
        return true;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < board.length; i++) {
            if (this.board[i] != that.board[i]) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // Queue to hold the boards
        Queue<Board> neighbors = new Queue<Board>();
        int emptyTileIndex; // array index of the empty tile
        int emptyTileRow, emptyTileCol; // row and col of the grid rep
        int north = 0, east = 0, south = 0, west = 0;
        ArrayList<Integer> tilesToSwap = new ArrayList<Integer>();
        /*
        Locate the empty position. Identify what moves it can make.
        Add those tiles that can be swapped to the ArrayList
         */
        for (emptyTileIndex = 0; emptyTileIndex < board.length; emptyTileIndex++) {
            if (board[emptyTileIndex] == 0) {
                emptyTileRow = xyFromArrayPos(emptyTileIndex)[0];
                emptyTileCol = xyFromArrayPos(emptyTileIndex)[1];
                // now look at all the possible moves NESW
                // store as the array position
                if (withinBounds(emptyTileRow - 1, emptyTileCol)) {
                    north = xyToArrayPos(emptyTileRow - 1, emptyTileCol);
                    tilesToSwap.add(north);
                }
                if (withinBounds(emptyTileRow, emptyTileCol + 1)) {
                    east = xyToArrayPos(emptyTileRow, emptyTileCol + 1);
                    tilesToSwap.add(east);
                }
                if (withinBounds(emptyTileRow + 1, emptyTileCol)) {
                    south = xyToArrayPos(emptyTileRow + 1, emptyTileCol);
                    tilesToSwap.add(south);
                }
                if (withinBounds(emptyTileRow, emptyTileCol - 1)) {
                    west = xyToArrayPos(emptyTileRow, emptyTileCol - 1);
                    tilesToSwap.add(west);
                }
                break;
            }

        }
        // create the variant boards with the swaps found in tileToSwap
        for (int i = 0; i < tilesToSwap.size(); i++) {
            int[] temp1d = board.clone();
            int[][] temp2d;
            swapElements(temp1d, emptyTileIndex, tilesToSwap.get(i));
            // change back to the 2d representation
            temp2d = arrayToMatrix(temp1d, width);
            Board newBoard = new Board(temp2d);
            neighbors.enqueue(newBoard);
        }
        return neighbors;


    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int row = 0, col = 0;
        int north = 0, east = 0, south = 0, west = 0;
        int[][] twin2d;
        int[] twin1d = board.clone();

        // check for the non-zero tile and then exchange
        for (int i = 0; i < twin1d.length; i++) {
            if (twin1d[i] != 0) {
                row = xyFromArrayPos(i)[0];
                col = xyFromArrayPos(i)[1];

                if (withinBounds(row - 1, col)) {
                    north = xyToArrayPos(row - 1, col);
                    if (twin1d[north] != 0) {
                        swapElements(twin1d, i, north);
                        break;
                    }

                }
                if (withinBounds(row, col + 1)) {
                    east = xyToArrayPos(row, col + 1);
                    if (twin1d[east] != 0) {
                        swapElements(twin1d, i, east);
                        break;
                    }

                }
                if (withinBounds(row + 1, col)) {
                    south = xyToArrayPos(row + 1, col);
                    if (twin1d[south] != 0) {
                        swapElements(twin1d, i, south);
                        break;
                    }
                }
                if (withinBounds(row, col - 1)) {
                    west = xyToArrayPos(row, col - 1);
                    if (twin1d[west] != 0) {
                        swapElements(twin1d, i, west);
                        break;
                    }
                }
            }
        }
        twin2d = arrayToMatrix(twin1d, width);
        return new Board(twin2d);
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

    /**
     * return an array [row,col] that represents the grid position of the tile
     * derived from the array position.
     *
     * @param i the array index of the tile
     * @return an array [row, col]
     */
    private int[] xyFromArrayPos(int i) {
        int[] coordinate = new int[2];
        coordinate[0] = i / width; // row
        coordinate[1] = i % width; // col
        return coordinate;
    }

    /**
     * Check whether the tile is within the bounds of the board.
     *
     * @param row row position
     * @param col col position
     * @return true if the (row, col) is within the bounds of the board.
     */
    private boolean withinBounds(int row, int col) {
        if (row < 0 || row >= width || col < 0 || col >= width) {
            return false;
        }
        return true;
    }

    /**
     * Swap a pair of elements in the array.
     *
     * @param arr array that is getting modified
     * @param a   index of first element to be swapped
     * @param b   index of second element to be swapped
     * @return the array after the elements have been swapped
     */
    private int[] swapElements(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
        return arr;
    }

    /**
     * Convert the array representation to a matrix representation
     *
     * @param arr       array representation of the board
     * @param dimension dimension of the matrix
     * @return A 2d representation of the board, NxN.
     */
    private int[][] arrayToMatrix(int[] arr, int dimension) {
        int[][] matrix = new int[dimension][dimension];
        for (int i = 0; i < arr.length; i++) {
            int row = xyFromArrayPos(i)[0];
            int col = xyFromArrayPos(i)[1];
            matrix[row][col] = arr[i];
        }
        return matrix;
    }

    private static int[][] arrayToBoard(int[] arr, int dimension) {
        int[][] test = new int[dimension][dimension];
        int index = 0;
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test.length; j++) {
                test[i][j] = arr[index];
                index++;
            }
        }
        return test;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int rank = 3;
        int[][] test, goalBoard;
        int[] numbers = { 8, 1, 3, 4, 0, 2, 7, 6, 5 };
        int[] goal = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
        test = arrayToBoard(numbers, rank);
        goalBoard = arrayToBoard(goal, rank);
        Board board = new Board(test);
        Board targetBoard = new Board(goalBoard);
        System.out.println(board.toString());
        // check methods
        System.out.println("Is it the goal board? " + board.isGoal());
        System.out.println("Hamming: " + board.hamming());
        System.out.println("Manhattan distance is: " + board.manhattan());

        Iterable<Board> neighbors = board.neighbors();
        int neighborNumber = 1;
        for (Board b : neighbors) {
            System.out.println("Neighbor " + neighborNumber);
            System.out.println(b);
            neighborNumber++;
        }
        Board twin = board.twin();
        System.out.println("Twin");
        System.out.println(twin.toString());
        System.out.println("Is it the goal board? " + twin.isGoal());
        System.out.println("Hamming: " + twin.hamming());
        System.out.println("Manhattan distance is: " + twin.manhattan());


        // System.out.println(targetBoard.toString());

    }

}
