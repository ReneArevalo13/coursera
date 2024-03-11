import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {


    private boolean vertical = true;
    private boolean horizontal = !vertical;
    private Node root;
    private int size;


    private class Node {
        private Point2D point; // point to be inserted
        private Node leftBranch; // left branch of node
        private Node rightBranch; // right branch of node
        private boolean split; // true: vertical, false: horizontal

        public Node(Point2D point) {
            this.point = point;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null point");
        }
        root = insert(root, p, vertical);

    }

    private Node insert(Node head, Point2D p, boolean split) {
        // the case of the first insertion, should be split vertically
        if (head == null) {
            Node temp = new Node(p);
            temp.split = split;
            size++;
            return temp;
        }
        // handle both left and right cases depending on previous case;

        // split vertically
        if (split == true) {
            if (cmp(head, p, split) < 0) {
                head.leftBranch = insert(head.leftBranch, p, !split);
            }
            else if (cmp(head, p, split) >= 0) {
                head.rightBranch = insert(head.rightBranch, p, !split);
            }
        }
        // split horizontally
        if (split == false) {
            if (cmp(head, p, split) < 0) {
                head.leftBranch = insert(head.leftBranch, p, !split);
            }
            else if (cmp(head, p, split) >= 0) {
                head.rightBranch = insert(head.rightBranch, p, !split);
            }
        }
        return head;


    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null value");
        return contains(root, p, vertical);
    }

    private boolean contains(Node head, Point2D p, boolean split) {
        if (head == null) return false;
        if (head.point == p) return true;
        if (split == true) {
            if (cmp(head, p, split) < 0) {
                contains(head.leftBranch, p, !split);
            }
            else if (cmp(head, p, split) >= 0) {
                contains(head.rightBranch, p, !split);
            }
        }
        if (split == false) {
            if (cmp(head, p, split) < 0) {
                contains(head.leftBranch, p, !split);
            }
            else if (cmp(head, p, split) >= 0) {
                contains(head.rightBranch, p, !split);
            }
        }
        return false;
    }

    // draw all points to standard draw
    // public void draw() {
    //
    //
    // }

    // all points that are inside the rectangle (or on the boundary)
    // public Iterable<Point2D> range(RectHV rect) {
    //
    // }

    // a nearest neighbor in the set to point p; null if the set is empty
    // public Point2D nearest(Point2D p) {
    //
    // }

    /*
    Comparison method that will compare the x-coordinates if the split is vertical.
    Will compare the y-coordinates if the split is horizontal.
     */
    private int cmp(Node n, Point2D p, boolean split) {
        // same point
        if (n.point == p) return 0;
        // extract coordinates from the point in the root node
        double nodeX = n.point.x();
        double nodeY = n.point.y();
        // extract coordinates from point
        double pointX = p.x();
        double pointY = p.y();
        // case when the splitting is vertical: compare x
        if (split == true) {
            return Double.compare(pointX, nodeX);
        }
        // case when splitting is horizontal: compare y
        return Double.compare(pointY, nodeY);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        Queue<Point2D> points = new Queue<>();
        for (int i = 0; i < 5; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);

            points.enqueue(new Point2D(x, y));
        }
        KdTree kdtree = new KdTree();
        for (Point2D p : points) {
            kdtree.insert(p);
        }
        System.out.println(points);
        Point2D checkTrue = points.dequeue();
        Point2D checkFalse = new Point2D(0.23, 0.33);
        System.out.println(kdtree.contains(checkTrue));
        System.out.println(kdtree.contains(checkFalse));

    }
}

















