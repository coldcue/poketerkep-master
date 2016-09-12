package hu.poketerkep.master.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.poketerkep.shared.config.Constants;
import hu.poketerkep.shared.geo.Coordinate;

import java.time.Duration;
import java.time.Instant;

public class ScanLocation implements Comparable<ScanLocation> {
    private final long id;
    private final int priority;
    private final Coordinate coordinate;
    private Instant lastScanned;
    private Instant compareNow;

    public ScanLocation(long id, int priority, Coordinate coordinate, Instant lastScanned) {
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

    public void setCompareNow(Instant compareNow) {
        this.compareNow = compareNow;
    }

    @Override
    public int compareTo(ScanLocation o) {
        Instant priorityTime = compareNow.minus(Duration.ofMinutes(Constants.PRIORITY_TIME_MINS));

        //If both is before priority time or same lastScanned, then priority matters
        if (lastScanned.isBefore(priorityTime) && o.lastScanned.isBefore(priorityTime)
                || lastScanned == o.lastScanned) {

            //If both have the same priority, ID matters
            if (priority == o.priority) {
                return Long.compare(id, o.id);
            }
            //If one has higher priority, priority matters
            else {
                return Integer.compare(priority, o.priority);
            }
        }
        //If they are not before priority time
        else {
            return lastScanned.isBefore(o.lastScanned) ? -1 : 1;
        }
    }

    @JsonIgnore
    public long getId() {
        return id;
    }
}
