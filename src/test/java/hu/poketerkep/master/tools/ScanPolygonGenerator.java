package hu.poketerkep.master.tools;

import hu.poketerkep.master.model.ScanPolygon;
import hu.poketerkep.shared.geo.Coordinate;

public class ScanPolygonGenerator {
    public static ScanPolygon generateConcanveTest() {
        // Create a concave polygon
        ScanPolygon scanPolygon = new ScanPolygon();

        Coordinate[] vertices = new Coordinate[6];
        vertices[0] = Coordinate.fromDegrees(47.0000, 19.0000);
        vertices[1] = Coordinate.fromDegrees(47.0000, 18.0090);
        vertices[2] = Coordinate.fromDegrees(47.0005, 18.0095);
        vertices[3] = Coordinate.fromDegrees(47.0010, 18.0090);
        vertices[4] = Coordinate.fromDegrees(47.0010, 19.0000);
        vertices[5] = Coordinate.fromDegrees(47.0005, 19.0005);

        scanPolygon.setVertices(vertices);
        scanPolygon.setId("TEST");

        return scanPolygon;
    }
}
