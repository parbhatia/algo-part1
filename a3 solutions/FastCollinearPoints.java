import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private final int CAPACITY = 5;
    private int numOfSegments;
    private LineSegment[] segmentArr = new LineSegment[CAPACITY];

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
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
        Point[] myPoints = points.clone();
        Arrays.sort(myPoints);

        //check duplicates
        for (int i = 0; i < myPoints.length - 1; ++i) {
            if (myPoints[i].compareTo(myPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("duplicate points encountered");
            }
        }

        int n = myPoints.length;

        for (int i = 0; i < n; ++i) {
            Point p = myPoints[i];
            Point[] pointsSortedBySlope = myPoints.clone();
            //sort based on p's slope with other points
            Arrays.sort(pointsSortedBySlope, p.slopeOrder());

            int start = 1;
            int stop = 1;
            while (stop < n) {
                double extractedSlope = p.slopeTo(pointsSortedBySlope[start]);
                //lets be fancy
                do {
                    ++stop;
                } while (stop < n
                        && Double.compare(extractedSlope, p.slopeTo(pointsSortedBySlope[stop]))
                        == 0);
                //check for maximal line segment
                if (stop - start >= 3 && p.compareTo(pointsSortedBySlope[start]) < 0) {
                    LineSegment newSeg = new LineSegment(p, pointsSortedBySlope[stop - 1]);
                    if (segmentArr.length == numOfSegments) {
                        resizeSegArray();
                    }
                    segmentArr[numOfSegments] = newSeg;
                    ++numOfSegments;
                }
                //confusing bit: we reset start pos each loop, since we feed stop consecutively in the
                //   do while loop
                start = stop;
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

    public int numberOfSegments()        // the number of line segments
    {
        return numOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
