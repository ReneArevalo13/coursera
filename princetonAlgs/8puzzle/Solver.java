import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode finalNode;
    private boolean solvable;
    private int minMoves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        int moves = 0;
        int twinMoves;
        boolean solved = false;
        boolean twinSolved = false;

        // create queues to hold the neighbors and twins
        Queue<Board> neighbors = new Queue<Board>();
        Queue<Board> twinNeighbors = new Queue<Board>();
        // create the minPQs for the initial board and twin board
        MinPQ<SearchNode> searchNodes = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinNodes = new MinPQ<SearchNode>();
        // initialize the first search node for the board and twin board
        SearchNode searchNode = new SearchNode(initial, moves, null);
        SearchNode twinSearchNode = new SearchNode(initial.twin(), moves, null);
        // insert the first search nodes into the minPQ
        searchNodes.insert(searchNode);
        twinNodes.insert(twinSearchNode);

        // A* search loop
        while (!solved && !twinSolved) {
            // current board
            SearchNode current = searchNodes.delMin();
            SearchNode previous = current.getPrevious();
            Board temp = current.getBoard();
            solved = temp.isGoal();
            // twin board
            SearchNode twinCurrent = twinNodes.delMin();
            SearchNode twinPrevious = twinCurrent.getPrevious();
            Board twinTemp = twinCurrent.getBoard();
            twinSolved = twinTemp.isGoal();

            for (Board b : temp.neighbors()) {
                neighbors.enqueue(b);
            }
            for (Board b : twinTemp.neighbors()) {
                twinNeighbors.enqueue(b);
            }

            while (neighbors.size() > 0) {
                Board board = neighbors.dequeue();
                // do not duplicate boards
                if (previous != null && previous.getBoard().equals(board)) {
                    // recall that this forgoes the rest of this iteration and continues to the next
                    continue;
                }
                // create new SearchNode for this neighbor board and add to minPQ
                SearchNode neighborNode = new SearchNode(board, current.getMoves() + 1, current);
                searchNodes.insert(neighborNode);
            }
            while (twinNeighbors.size() > 0) {
                Board board = twinNeighbors.dequeue();
                if (twinPrevious != null && twinPrevious.getBoard().equals(board)) {
                    continue;
                }
                SearchNode twinNeighborNode = new SearchNode(board, twinCurrent.getMoves() + 1,
                                                             twinCurrent);
                twinNodes.insert(twinNeighborNode);
            }
            moves = current.getMoves() + 1;
            twinMoves = twinCurrent.getMoves() + 1;
            finalNode = current;
        }
        solvable = !twinSolved;
        minMoves = moves - 1;

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<Board>();
        SearchNode lastNode = this.finalNode;
        if (this.isSolvable()) {
            while (lastNode.getPrevious() != null) {
                boards.push(lastNode.getBoard());
                lastNode = lastNode.getPrevious();
            }
            boards.push(lastNode.getBoard());
            return boards;
        }
        return null;
    }


    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode previous = null;
        private Board currentBoard = null;
        private int moves;
        private int priority = 0;

        public SearchNode(Board initial, int m, SearchNode prev) {
            previous = prev;
            currentBoard = initial;
            moves = m;
            // manhattan priority function
            priority = moves + currentBoard.manhattan();
        }

        public int getPriority() {
            return priority;
        }

        public Board getBoard() {
            return currentBoard;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int compareTo(SearchNode n) {
            return this.priority - n.priority;
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
