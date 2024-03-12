import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {


    private static final double XMIN = 0.0;
    private static final double XMAX = 1.0;
    private static final double YMIN = 0.0;
    private static final double YMAX = 1.0;
    private boolean vertical = true;
    private Node root;
    private int size;


    private class Node {
        private Point2D point; // point to be inserted
        private Node leftBranch; // left branch of node
        private Node rightBranch; // right branch of node
        private boolean split; // true: vertical, false: horizontal
        private RectHV rect; // the axis-aligned rectangle corresponding to this node

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
            temp.rect = alignedRect(head, p, split, 0);
            size++;
            return temp;
        }
        // handle both left and right cases depending on previous case;
        // compare coordiantes
        int cmpValue = cmp(head, p, split);

        // split vertically
        if (split == true) {
            if (cmpValue < 0) {
                head.leftBranch = insert(head.leftBranch, p, !split);
                head.leftBranch.rect = alignedRect(head, p, split, cmpValue);
            }
            else if (cmpValue >= 0) {
                head.rightBranch = insert(head.rightBranch, p, !split);
                head.rightBranch.rect = alignedRect(head, p, split, cmpValue);
            }
        }
        // split horizontally
        if (split == false) {
            if (cmpValue < 0) {
                head.leftBranch = insert(head.leftBranch, p, !split);
                head.leftBranch.rect = alignedRect(head, p, split, cmpValue);
            }
            else if (cmpValue >= 0) {
                head.rightBranch = insert(head.rightBranch, p, !split);
                head.rightBranch.rect = alignedRect(head, p, split, cmpValue);
            }
        }

        return head;


    }

    private RectHV alignedRect(Node prevNode, Point2D p, boolean split, int cmpValue) {
        if (prevNode == null) return new RectHV(XMIN, YMIN, XMAX, YMAX);

        double prevXMIN = prevNode.rect.xmin();
        double prevXMAX = prevNode.rect.xmax();
        double prevYMIN = prevNode.rect.ymin();
        double prevYMAX = prevNode.rect.ymax();

        double prevX = prevNode.point.x();
        double prevY = prevNode.point.y();

        // split vertically
        if (split == true && cmpValue < 0) return new RectHV(prevXMIN, prevYMIN, prevX, prevYMAX);
        if (split == true && cmpValue >= 0) return new RectHV(prevX, prevYMIN, prevXMAX, prevYMAX);
        if (split == false && cmpValue < 0) return new RectHV(prevXMIN, prevYMIN, prevXMAX, prevY);
        return new RectHV(prevXMIN, prevY, prevXMAX, prevYMAX);

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
    public void draw() {


        draw(root);
    }

    private void draw(Node n) {

        if (n != null) {
            Point2D p = n.point;
            boolean split = n.split;
            double xmin = n.rect.xmin();
            double xmax = n.rect.xmax();
            double ymin = n.rect.ymin();
            double ymax = n.rect.ymax();
            StdDraw.setPenRadius(0.007);

            if (split == true) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(p.x(), ymin, p.x(), ymax);
            }
            if (split == false) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xmin, p.y(), xmax, p.y());
            }
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(p.x(), p.y());
            draw(n.leftBranch);
            draw(n.rightBranch);
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> pointsInRange = new Stack<>();
        range(rect, root, pointsInRange);
        return pointsInRange;
    }

    private void range(RectHV rect, Node node, Stack<Point2D> points) {
        if (node == null) return;

        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) {
                points.push(node.point);
            }
            range(rect, node.leftBranch, points);
            range(rect, node.rightBranch, points);
        }


    }

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

        // Queue<Point2D> points = new Queue<>();
        // for (int i = 0; i < 5; i++) {
        //     double x = StdRandom.uniform(0.0, 1.0);
        //     double y = StdRandom.uniform(0.0, 1.0);
        //
        //     points.enqueue(new Point2D(x, y));
        // }
        // KdTre===e kdtree = new KdTree();
        // for (Point2D p : points) {
        //     kdtree.insert(p);
        // }
        // System.out.println(points);
        // Point2D checkTrue = points.dequeue();
        // Point2D checkFalse = new Point2D(0.23, 0.33);
        // System.out.println(kdtree.contains(checkTrue));
        // System.out.println(kdtree.contains(checkFalse));

        Queue<Point2D> point2DQueue = new Queue<>();
        Point2D p0 = new Point2D(0.2, 0.3);
        point2DQueue.enqueue(p0);
        Point2D p1 = new Point2D(0.1, 0.5);
        point2DQueue.enqueue(p1);
        Point2D p2 = new Point2D(0.4, 0.2);
        point2DQueue.enqueue(p2);
        Point2D p3 = new Point2D(0.4, 0.5);
        point2DQueue.enqueue(p3);
        Point2D p4 = new Point2D(0.3, 0.3);
        point2DQueue.enqueue(p4);
        Point2D p5 = new Point2D(0.4, 0.4);
        point2DQueue.enqueue(p5);
        KdTree kdtree = new KdTree();
        for (Point2D p : point2DQueue) {
            kdtree.insert(p);
            // kdtree.draw();
        }
    }
}

















