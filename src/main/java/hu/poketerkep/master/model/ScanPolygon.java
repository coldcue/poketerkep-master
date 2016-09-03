package hu.poketerkep.master.model;


import hu.poketerkep.shared.geo.Coordinate;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class ScanPolygon {
    private String id;
    private Coordinate[] vertices;
    private int threadCount;

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

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public Collection<ScanLocation> getScanLocations() {
        SortedSet<ScanLocation> scanLocations = new TreeSet<>();

        return scanLocations;
    }
}
