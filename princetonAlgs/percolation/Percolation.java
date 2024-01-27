/* *****************************************************************************
 *  Name:              Rene Arevalo
 *  Coursera User ID:  123456
 *  Last modified:     1/11/2024
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final boolean[][] opened;
    private final int size;
    private int openSites = 0;
    private final WeightedQuickUnionUF percolates;
    private final int TOP = 0;
    private int BOTTOM;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        size = n;
        percolates = new WeightedQuickUnionUF(n * n + 2);
        opened = new boolean[n][n];
        BOTTOM = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException("Out of bounds");
        }
        opened[row - 1][col - 1] = true;
        openSites++;

        if (row == 1) {
            int p = getPercolateIndex(row, col);
            percolates.union(p, TOP);
        }
        if (row == size) {
            int p = getPercolateIndex(row, col);
            percolates.union(p, BOTTOM);
        }

        if (row > 1 && isOpen(row - 1, col)) {
            int p = getPercolateIndex(row, col);
            int q = getPercolateIndex(row - 1, col);
            percolates.union(p, q);
        }
        if (row < size && isOpen(row + 1, col)) {
            int p = getPercolateIndex(row, col);
            int q = getPercolateIndex(row + 1, col);
            percolates.union(p, q);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            int p = getPercolateIndex(row, col);
            int q = getPercolateIndex(row, col - 1);
            percolates.union(p, q);
        }
        if (col < size && isOpen(row, col + 1)) {
            int p = getPercolateIndex(row, col);
            int q = getPercolateIndex(row, col + 1);
            percolates.union(p, q);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException("Out of bounds");
        }
        return opened[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("Out of bounds");
        }
        int p = getPercolateIndex(row, col);
        return percolates.find(TOP) == percolates.find(p);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;

    }

    // does the system percolate?
    public boolean percolates() {
        return percolates.find(TOP) == percolates.find(BOTTOM);

    }

    private int getPercolateIndex(int row, int col) {
        row--;
        col--;
        return row * size + col + 1;
    }


    public static void main(String[] args) {
    }
}
