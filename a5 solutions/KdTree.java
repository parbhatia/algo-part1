import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private enum Axis {
        VERT, HORIZ
    }

    private enum InsertDirection {
        LEFT, RIGHT
    }

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private Axis axis;

        private Node(Point2D p, Axis axis, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.axis = axis;
        }

        // prints point p, then recursively calls sub nodes to print their points
        private void draw() {

            if (lb != null) {
                // draw line
                if (axis == Axis.HORIZ) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.setPenRadius(.005);
                    StdDraw.line(lb.p.x(), 0, lb.p.x(), p.y());
                } else {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.setPenRadius(.005);
                    StdDraw.line(0, lb.p.y(), p.x(), lb.p.y());
                }
                // draw point
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.02);
                lb.draw();
            }
            if (rt != null) {
                // draw line
                if (axis == Axis.HORIZ) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.setPenRadius(.005);
                    StdDraw.line(rt.p.x(), p.y(), rt.p.x(), 1);
                } else {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.setPenRadius(.005);
                    StdDraw.line(p.x(), rt.p.y(), 1, rt.p.y());
                }
                // draw point
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.02);
                rt.draw();
            }
            if (p != null) {
                p.draw();
            }
        }
    }

    private Node root;
    private Integer size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null && size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private Axis invertAxis(Axis axis) {
        if (axis == Axis.HORIZ)
            return Axis.VERT;
        else
            return Axis.HORIZ;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            // create unit square
            RectHV rect = new RectHV(0, 0, 1, 1);
            root = new Node(p, Axis.VERT, rect, null, null);
            ++size;
        } else if (contains(p)) {
            // only add if point is not already in tree
            return;
        } else {
            Node curr = root;
            Node prev = null;
            InsertDirection insertDir = null;
            while (curr != null) {
                prev = curr;
                // compare x axis
                if (curr.axis == Axis.VERT) {
                    if (p.x() < curr.p.x()) {
                        // go left
                        curr = curr.lb;
                        insertDir = InsertDirection.LEFT;
                    } else {
                        // go right
                        curr = curr.rt;
                        insertDir = InsertDirection.RIGHT;
                    }
                }
                // compare y axis
                else if (curr.axis == Axis.HORIZ) {
                    if (p.y() < curr.p.y()) {
                        // go left
                        curr = curr.lb;
                        insertDir = InsertDirection.LEFT;
                    } else {
                        // go right
                        curr = curr.rt;
                        insertDir = InsertDirection.RIGHT;
                    }
                }
            }
            // prev Node is parent of new Node to insert
            // invert prev Node's axis and add rectangle based on rules
            Axis axis = prev.axis;
            Axis newAxis = invertAxis(axis);
            RectHV newRect;
            Node newNode;
            if (axis == Axis.VERT) {
                // new node's x coordinate depends on prev's x coordinate
                if (insertDir == InsertDirection.LEFT) {
                    // insert left
                    newRect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.p.x(), prev.rect.ymax());
                    newNode = new Node(p, newAxis, newRect, null, null);
                    prev.lb = newNode;
                } else {
                    // insert right
                    newRect = new RectHV(prev.p.x(), prev.rect.ymin(), prev.rect.xmax(), prev.rect.ymax());
                    newNode = new Node(p, newAxis, newRect, null, null);
                    prev.rt = newNode;
                }
                ++size;
            } else if (axis == Axis.HORIZ) {
                // new node's y coordinate depends on prev's y coordinate
                if (insertDir == InsertDirection.LEFT) {
                    // insert left
                    newRect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.rect.xmax(), prev.p.y());
                    newNode = new Node(p, newAxis, newRect, null, null);
                    prev.lb = newNode;
                } else {
                    // insert right
                    newRect = new RectHV(prev.rect.xmin(), prev.p.y(), prev.rect.xmax(), prev.rect.ymax());
                    newNode = new Node(p, newAxis, newRect, null, null);
                    prev.rt = newNode;
                }
                ++size;
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node curr = root;
        while (curr != null) {
            if (curr.p.equals(p)) {
                return true;
            } else {
                // check direction in which to recurse
                if ((curr.axis == Axis.VERT && p.x() < curr.p.x()) || (curr.axis == Axis.HORIZ && p.y() < curr.p.y())) {
                    // go left
                    curr = curr.lb;
                } else {
                    // go right
                    curr = curr.rt;
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        if (root != null) {
            // draw line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(.005);
            StdDraw.line(root.p.x(), 0, root.p.x(), 1);
            // draw point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.02);
            root.draw();
        }
    }

    private void addPoints(Node n, RectHV targetRect, LinkedList<Point2D> points) {
        if (n == null) {
            return;
        }
        // check if rectangle intersects node's rectangle
        if (targetRect.intersects(n.rect)) {
            if (targetRect.contains(n.p)) {
                // add point
                points.add(n.p);
            }
            // recurse on subtrees
            addPoints(n.lb, targetRect, points);
            addPoints(n.rt, targetRect, points);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        // ArrayList<Point2D> points = new ArrayList<Point2D>();
        LinkedList<Point2D> points = new LinkedList<Point2D>();
        addPoints(root, rect, points);
        return points;
    }

    private Point2D findNearest(Point2D queryp, Point2D closestp, Node node) {
        if (node == null) {
            return closestp;
        }
        Double distanceBetweenMyRect = node.rect.distanceTo(queryp);
        Double smallestDistance = closestp.distanceTo(queryp);
        // note: distanceBetweenMyrect will be 0 if point is contained inside rectangle

        // we only recurse if point is contained in rectangle meaning
        // (distanceBetweenMyRect = 0) or
        // if distance from rectangle's perimeter and query point is smaller,
        // in which case a possible point might exist in subtrees
        if (distanceBetweenMyRect < smallestDistance) {
            Double distanceBetweenMyPoint = node.p.distanceTo(queryp);
            if (distanceBetweenMyPoint < smallestDistance) {
                closestp = node.p;
            }
            // recurse based on which side queryp is to node
            if ((node.axis == Axis.VERT && queryp.x() < node.p.x())
                    || (node.axis == Axis.HORIZ && queryp.y() < node.p.y())) {
                // go left
                closestp = findNearest(queryp, closestp, node.lb);
                closestp = findNearest(queryp, closestp, node.rt);
            } else {
                // go right
                closestp = findNearest(queryp, closestp, node.rt);
                closestp = findNearest(queryp, closestp, node.lb);

            }
        }
        return closestp;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else if (isEmpty()) {
            return null;
        } else {
            return findNearest(p, root.p, root);
        }
    }

    // // unit testing of the methods
    public static void main(String[] args) {
        // KdTree newSet = new KdTree();
        // try {
        // BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        // StdDraw.setPenColor(StdDraw.BLACK);
        // // StdDraw.setPenRadius();
        // StdDraw.setPenRadius(.01);
        // for (String s = null; (s = b.readLine()) != null;) {
        // String[] points = s.split(" ");
        // Point2D point = new Point2D(Double.parseDouble(points[0]),
        // Double.parseDouble(points[1]));
        // newSet.insert(point);
        // }
        // // draw set
        // newSet.draw();
        // // // rectangle test
        // // RectHV rectangle = new RectHV(0.0, 0.0, 0.5, 0.5);
        // // StdDraw.setPenColor(StdDraw.GREEN);
        // // rectangle.draw();
        // // StdDraw.setPenColor(StdDraw.RED);
        // // for (Point2D p : newSet.range(rectangle)) {
        // // p.draw();
        // // }
        // // // nearest point test
        // // Point2D target = new Point2D(0.7, 0.8);
        // // StdDraw.setPenColor(StdDraw.PINK);
        // // StdDraw.setPenRadius(.03);
        // // target.draw();
        // // Point2D nearestP = newSet.nearest(target);
        // // StdDraw.setPenRadius(.01);
        // // nearestP.draw();
        // // StdOut.println(nearestP.toString());

        // } catch (IOException e) {
        // StdOut.println("IO Error");
        // }
    }
}
