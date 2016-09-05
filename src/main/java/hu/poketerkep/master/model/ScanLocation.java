package hu.poketerkep.master.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.poketerkep.master.config.Constants;
import hu.poketerkep.shared.geo.Coordinate;

import java.time.Duration;
import java.time.Instant;

public class ScanLocation implements Comparable<ScanLocation> {
    private final long id;
    private final int priority;
    private final Coordinate coordinate;
    private Instant lastScanned;

    ScanLocation(long id, int priority, Coordinate coordinate, Instant lastScanned) {
        this.id = id;
        this.priority = priority;
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
        Instant priorityTime = Instant.now().minus(Duration.ofMinutes(Constants.PRIORITY_TIME_MINS));

        //If both is before priority time, then priority matters
        if (lastScanned.isBefore(priorityTime) && o.lastScanned.isBefore(priorityTime)) {

            //If both have the same priority, ID matters
            if (priority == o.priority) {
                return Long.compare(id, o.id);
            } else {
                return Integer.compare(priority, o.priority);
            }
        } else {
            return lastScanned.isBefore(o.lastScanned) ? -1 : 1;
        }
    }

    @JsonIgnore
    public long getId() {
        return id;
    }
}
