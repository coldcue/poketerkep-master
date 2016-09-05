package hu.poketerkep.master.model;

import hu.poketerkep.shared.geo.Coordinate;
import junit.framework.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class ScanLocationTest {

    @Test
    public void compareTo() throws Exception {

        ScanLocation s0 = new ScanLocation(0, 0, Coordinate.fromDegrees(1, 1), Instant.now().minus(Duration.ofMinutes(20)));
        ScanLocation s1 = new ScanLocation(0, 1, Coordinate.fromDegrees(1, 1), Instant.now().minus(Duration.ofMinutes(20)));
        ScanLocation s2 = new ScanLocation(1, 1, Coordinate.fromDegrees(1, 1), Instant.now().minus(Duration.ofMinutes(20)));
        ScanLocation s3 = new ScanLocation(1, 0, Coordinate.fromDegrees(1, 1), Instant.now());

        ArrayList<ScanLocation> scanLocations = new ArrayList<>();
        scanLocations.addAll(Arrays.asList(s3, s1, s2, s0));
        Collections.sort(scanLocations);

        Assert.assertEquals(s0, scanLocations.get(0));
        Assert.assertEquals(s1, scanLocations.get(1));
        Assert.assertEquals(s2, scanLocations.get(2));
        Assert.assertEquals(s3, scanLocations.get(3));
    }

}