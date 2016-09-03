package hu.poketerkep.master.model;

import hu.poketerkep.shared.geo.Coordinate;

import java.time.Instant;

public class ScanLocation implements Comparable<ScanLocation> {
    private long id;
    private Coordinate coordinate;
    private Instant lastScanned;

    public ScanLocation(long id, Coordinate coordinate, Instant lastScanned) {
        this.id = id;
        this.coordinate = coordinate;
        this.lastScanned = lastScanned;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Instant getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Instant lastScanned) {
        this.lastScanned = lastScanned;
    }

    @Override
    public int compareTo(ScanLocation o) {
        return Long.compare(id, o.id);
    }
}
