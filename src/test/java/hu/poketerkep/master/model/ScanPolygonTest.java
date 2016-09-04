package hu.poketerkep.master.model;

import hu.poketerkep.master.tools.ScanPolygonGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;


public class ScanPolygonTest {

    @Test
    public void generateScanLocations() throws Exception {
        ScanPolygon scanPolygon = ScanPolygonGenerator.generateConcanveTest();

        Collection<ScanLocation> scanLocations = scanPolygon.generateScanLocations();

        Assert.assertEquals(760, scanLocations.size());
    }

}