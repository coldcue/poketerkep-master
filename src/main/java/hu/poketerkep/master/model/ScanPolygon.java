package hu.poketerkep.master.model;


import hu.poketerkep.master.geo.FloodFill;
import hu.poketerkep.shared.geo.Coordinate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class ScanPolygon {
    private String id;
    private Coordinate[] vertices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinate[] getVertices() {
        return vertices;
    }

    public void setVertices(Coordinate[] vertices) {
        this.vertices = vertices;
    }

    public Collection<ScanLocation> generateScanLocations() {
        SortedSet<ScanLocation> scanLocations = new TreeSet<>();

        FloodFill floodFill = new FloodFill(vertices);
        ArrayList<Coordinate> coordinates = floodFill.generate();

        long id = 0;

        for (Coordinate coordinate : coordinates) {
            scanLocations.add(new ScanLocation(id++, coordinate, Instant.MIN));
        }

        return scanLocations;
    }
}
