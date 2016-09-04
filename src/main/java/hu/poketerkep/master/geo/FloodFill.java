package hu.poketerkep.master.geo;

import com.google.common.math.DoubleMath;
import hu.poketerkep.shared.geo.Coordinate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import static hu.poketerkep.shared.geo.Direction.*;

public class FloodFill {
    private static final double DISTANCE = 0.0989; // 2 * 70 * sqrt(2) m
    private final Coordinate[] vertices;
    private final int nvert;
    private final double[] vertx;
    private final double[] verty;
    private double xMax = Double.MIN_VALUE,
            xMin = Double.MAX_VALUE,
            yMin = Double.MAX_VALUE,
            yMax = Double.MIN_VALUE;

    public FloodFill(Coordinate[] vertices) {
        this.vertices = vertices;

        //Search min/max values
        for (Coordinate vertex : vertices) {
            double x = vertex.getLatitude();
            double y = vertex.getLongitude();


            //X
            if (x > xMax) xMax = x;
            if (x < xMin) xMin = x;

            //Y
            if (y > yMax) yMax = y;
            if (y < yMin) yMin = y;
        }

        // Convert coordinates into two arrays
        nvert = vertices.length;
        vertx = new double[nvert];
        verty = new double[nvert];

        for (int i = 0; i < nvert; i++) {
            vertx[i] = vertices[i].getLatitude();
            verty[i] = vertices[i].getLongitude();
        }
    }

    /**
     * Generate points with flood fill algorithm
     *
     * @return the points
     */
    public ArrayList<Coordinate> generate() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Deque<Coordinate> queue = new ArrayDeque<>();

        // Start with the fisrt vertex
        queue.add(vertices[0]);

        long count = 0;
        boolean first = true;

        // While the queue is not empty
        while (queue.size() != 0) {
            Coordinate coordinate = queue.poll();

            // Check if its in the polygon and not already added
            if ((!isCoordinateAlreadyAdded(coordinates, coordinate) && isInPolygon(coordinate)) || first) {

                if (first) {
                    //If first
                    first = false;
                } else {
                    //Add it to the coordinates
                    coordinates.add(coordinate);
                }

                //Add coordinates in every direction
                queue.add(coordinate.getNew(DISTANCE, NORTH.getAngle()));
                queue.add(coordinate.getNew(DISTANCE, EAST.getAngle()));
                queue.add(coordinate.getNew(DISTANCE, SOUTH.getAngle()));
                queue.add(coordinate.getNew(DISTANCE, WEST.getAngle()));
            }
        }

        return coordinates;
    }

    /**
     * Check if this coordinate is not already added
     *
     * @param coordinates
     * @param coordinate
     * @return
     */
    private boolean isCoordinateAlreadyAdded(ArrayList<Coordinate> coordinates, Coordinate coordinate) {
        for (Coordinate c : coordinates) {
            double tolerance = 0.0000001;
            if (DoubleMath.fuzzyEquals(c.getLatitude(), coordinate.getLatitude(), tolerance)
                    && DoubleMath.fuzzyEquals(c.getLongitude(), coordinate.getLongitude(), tolerance)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determine if a coordinate is in a polygon
     *
     * @param coordinate coordinate
     * @return true if the coordinate is in the polygon
     */
    private boolean isInPolygon(Coordinate coordinate) {
        double testx = coordinate.getLatitude();
        double testy = coordinate.getLongitude();

        return !(testx < xMin || testx > xMax || testy < yMin || testy > yMax) && pnpoly(testx, testy);
    }


    /**
     * Determines if a point is in a polygon
     * http://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
     *
     * @param testx X- and y-coordinate of the test point.
     * @param testy X- and y-coordinate of the test point.
     * @return Is it inside the polygon
     */
    public boolean pnpoly(double testx, double testy) {
        boolean c = false;
        int i, j;
        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            if (((verty[i] > testy) != (verty[j] > testy)) &&
                    (testx < (vertx[j] - vertx[i]) * (testy - verty[i]) / (verty[j] - verty[i]) + vertx[i]))
                c = !c;
        }
        return c;
    }

}
