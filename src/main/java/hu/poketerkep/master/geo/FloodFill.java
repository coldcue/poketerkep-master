package hu.poketerkep.master.geo;

import com.google.common.math.DoubleMath;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.geo.Direction;

import java.util.ArrayList;
import java.util.HashSet;

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
            double x = vertex.getLongitude();
            double y = vertex.getLatitude();


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
            vertx[i] = vertices[i].getLongitude();
            verty[i] = vertices[i].getLatitude();
        }
    }

    /**
     * Generate points with flood fill algorithm
     *
     * @return the points
     */
    public HashSet<Coordinate> generate() {
        HashSet<Coordinate> coordinates = new HashSet<>();

        Coordinate lastCoordinate = Coordinate.fromDegrees(yMin, xMin);

        while (true) {
            //Go from west to east
            Coordinate coordinate = lastCoordinate.getNew(DISTANCE, Direction.EAST.getAngle());

            // If its in the polygon, add it
            if (isInPolygon(coordinate)) {
                coordinates.add(coordinate);
            }
            // If its not in the bounding box
            else {
                //Check if it is too East
                if (coordinate.getLongitude() > xMax) {
                    // Create a new row up North
                    coordinate = Coordinate.fromDegrees(coordinate.getLatitude(), xMin) // The highest and most western coordinate
                            .getNew(DISTANCE, Direction.NORTH.getAngle()); // A new norther

                    //We have reached yMax
                    if (coordinate.getLatitude() > yMax) {
                        break;
                    }
                }
            }

            lastCoordinate = coordinate;
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
        double testy = coordinate.getLatitude();
        double testx = coordinate.getLongitude();

        return pnpoly(testx, testy);
    }

    /**
     * Check if its in the bounding box
     *
     * @return
     */
    private boolean isInBoundingBox(Coordinate coordinate) {
        double testy = coordinate.getLatitude();
        double testx = coordinate.getLongitude();

        return testx < xMin || testx > xMax || testy < yMin || testy > yMax;
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
