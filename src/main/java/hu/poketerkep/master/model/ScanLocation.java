package hu.poketerkep.master.model;

import hu.poketerkep.shared.geo.Coordinate;

import java.time.Instant;

public class ScanLocation implements Comparable<ScanLocation> {
    private final long id;
    private final Coordinate coordinate;
    private Instant lastScanned;

    public ScanLocation(long id, Coordinate coordinate, Instant lastScanned) {
        this.id = id;
        this.coordinate = coordinate;
        this.lastScanned = lastScanned;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Instant getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Instant lastScanned) {
        this.lastScanned = lastScanned;
    }

    @Override
    public int compareTo(ScanLocation o) {
        if (lastScanned.equals(o.lastScanned)) return Long.compare(id, o.id);
        return lastScanned.isBefore(o.lastScanned) ? 1 : -1;
    }

    public long getId() {
        return id;
    }
}
