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

        Instant now = Instant.now();

        ScanLocation s0 = new ScanLocation(0, 0, Coordinate.fromDegrees(1, 1), now.minus(Duration.ofMinutes(20)));
        ScanLocation s1 = new ScanLocation(0, 1, Coordinate.fromDegrees(1, 1), now.minus(Duration.ofMinutes(20)));
        ScanLocation s2 = new ScanLocation(1, 1, Coordinate.fromDegrees(1, 1), now.minus(Duration.ofMinutes(20)));
        ScanLocation s3 = new ScanLocation(2, 0, Coordinate.fromDegrees(1, 1), now);

        ArrayList<ScanLocation> scanLocations = new ArrayList<>();
        scanLocations.addAll(Arrays.asList(s3, s1, s2, s0));

        scanLocations.forEach(scanLocation -> scanLocation.setCompareNow(now));
        Collections.sort(scanLocations);

        Assert.assertEquals(s0, scanLocations.get(0));
        Assert.assertEquals(s1, scanLocations.get(1));
        Assert.assertEquals(s2, scanLocations.get(2));
        Assert.assertEquals(s3, scanLocations.get(3));
    }

    @Test
    public void testContract() throws Exception {
        ScanLocation A = new ScanLocation(0, 0, Coordinate.fromDegrees(1, 1), Instant.now().minus(Duration.ofMinutes(20)));
        ScanLocation B = new ScanLocation(0, 1, Coordinate.fromDegrees(1, 1), Instant.now().minus(Duration.ofMinutes(20)));
        ScanLocation C = new ScanLocation(1, 0, Coordinate.fromDegrees(1, 1), Instant.now());

        Instant now = Instant.now();
        A.setCompareNow(now);
        B.setCompareNow(now);
        C.setCompareNow(now);

        //A should be less than B
        Assert.assertEquals(-1, A.compareTo(B));
        //A should be less than C
        Assert.assertEquals(-1, A.compareTo(C));
        //B should be less than C
        Assert.assertEquals(-1, B.compareTo(C));

        //All should be equal
        Assert.assertEquals(0, A.compareTo(A));
        Assert.assertEquals(0, B.compareTo(B));
        Assert.assertEquals(0, C.compareTo(C));

        //B should be greater than A
        Assert.assertEquals(1, B.compareTo(A));
        //C should be greater than A
        Assert.assertEquals(1, C.compareTo(A));
        //C should be greater than B
        Assert.assertEquals(1, C.compareTo(B));
    }

}