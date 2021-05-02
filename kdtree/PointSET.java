import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> mySet;

    // construct an empty set of points
    public PointSET() {
        mySet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return mySet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return mySet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        mySet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return mySet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Iterator<Point2D> itr = mySet.iterator(); itr.hasNext();) {
            Point2D p = itr.next();
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for (Iterator<Point2D> itr = mySet.iterator(); itr.hasNext();) {
            Point2D p = itr.next();
            if (rect.contains(p)) {
                points.add(p);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (mySet.isEmpty()) {
            return null;
        }
        Double minDistance = Double.MAX_VALUE;
        Point2D neightbour = null;
        for (Iterator<Point2D> itr = mySet.iterator(); itr.hasNext();) {
            Point2D myPoint = itr.next();
            Double dist = p.distanceTo(myPoint);
            if (dist < minDistance) {
                minDistance = dist;
                neightbour = myPoint;
            }
        }
        return neightbour;
    }

    public static void main(String[] args) {
    }
    // unit testing of the methods
    // public static void main(String[] args) {
    // PointSET newSet = new PointSET();
    // try {
    // BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
    // StdDraw.setPenColor(StdDraw.BLACK);
    // StdDraw.setPenRadius();
    // StdDraw.setPenRadius(.01);
    // for (String s = null; (s = b.readLine()) != null;) {
    // String[] points = s.split(" ");
    // Point2D point = new Point2D(Double.parseDouble(points[0]),
    // Double.parseDouble(points[1]));
    // newSet.insert(point);
    // }
    // // draw set
    // newSet.draw();
    // // rectangle test
    // RectHV rectangle = new RectHV(0.0, 0.0, 0.5, 0.5);
    // StdDraw.setPenColor(StdDraw.GREEN);
    // rectangle.draw();
    // StdDraw.setPenColor(StdDraw.RED);
    // for (Point2D p : newSet.range(rectangle)) {
    // p.draw();
    // }
    // // nearest point test
    // Point2D target = new Point2D(0.7, 0.8);
    // StdDraw.setPenColor(StdDraw.PINK);
    // StdDraw.setPenRadius(.03);
    // target.draw();
    // Point2D nearestP = newSet.nearest(target);
    // StdDraw.setPenRadius(.01);
    // nearestP.draw();
    // StdOut.println(nearestP.toString());

    // } catch (IOException e) {
    // StdOut.println("IO Error");
    // }
    // }
}
