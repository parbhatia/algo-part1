import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final int CAPACITY = 5;
    private int numOfSegments;
    private LineSegment[] segmentArr = new LineSegment[CAPACITY];

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        //check null arg
        if (points == null) {
            throw new IllegalArgumentException("no points array");
        }
        //check null points
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null point encountered");
            }
        }
        //clone points and sort
        Point[] myPoints = new Point[points.length];
        for (int i = 0; i < points.length; ++i) {
            myPoints[i] = points[i];
        }
        Arrays.sort(myPoints);

        //check duplicates
        for (int i = 0; i < myPoints.length - 1; ++i) {
            if (myPoints[i].compareTo(myPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("duplicate points encountered");
            }
        }
        for (int i = 0; i < myPoints.length - 3; ++i) {
            Point p = myPoints[i];
            for (int j = i + 1; j < myPoints.length - 2; ++j) {
                Point q = myPoints[j];
                double slopePQ = p.slopeTo(q);
                for (int k = j + 1; k < myPoints.length - 1; ++k) {
                    Point r = myPoints[k];
                    double slopePR = p.slopeTo(r);
                    if (slopePQ != slopePR) {
                        continue;
                    }
                    for (int l = k + 1; l < myPoints.length; ++l) {
                        Point s = myPoints[l];
                        double slopePS = p.slopeTo(s);
                        if (slopePS == slopePQ) {
                            if (segmentArr.length == numOfSegments) {
                                resizeSegArray();
                            }
                            // add line segment
                            LineSegment newSeg = new LineSegment(p, s);
                            segmentArr[numOfSegments] = newSeg;
                            ++numOfSegments;
                        }
                    }
                }
            }
        }
    }

    private void resizeSegArray() {
        LineSegment[] temp = new LineSegment[segmentArr.length * 2];
        for (int i = 0; i < segmentArr.length; ++i) {
            temp[i] = segmentArr[i];
        }
        segmentArr = temp;
    }

    public int numberOfSegments() {
        return numOfSegments;
    }

    public LineSegment[] segments() {
        //remove null values from array
        LineSegment[] cloned = new LineSegment[numOfSegments];
        for (int i = 0; i < numOfSegments; ++i) {
            cloned[i] = segmentArr[i];
        }
        return cloned;

    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
