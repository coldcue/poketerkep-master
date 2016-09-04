package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.ScanPolygonDataService;
import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.model.ScanPolygon;
import hu.poketerkep.master.tools.ScanPolygonGenerator;
import hu.poketerkep.shared.geo.Coordinate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

public class ScanServiceTest {
    @Test
    public void getNextScanLocations() throws Exception {
        // Create mock
        ScanPolygonDataService mock = Mockito.mock(ScanPolygonDataService.class);
        ScanPolygon poly1 = ScanPolygonGenerator.generateConcanveTest();
        ScanPolygon poly2 = new ScanPolygon();
        poly2.setId("TESTSTST");
        poly2.setVertices(new Coordinate[]{
                Coordinate.fromDegrees(47.51, 19.04),
                Coordinate.fromDegrees(47.52, 19.07),
                Coordinate.fromDegrees(47.49, 19.05)});

        Mockito.when(mock.getAll()).thenReturn(Arrays.asList(poly1, poly2));

        ScanService scanService = new ScanService(mock);

        // Test
        Collection<ScanLocation> nextScanLocations = scanService.getNextScanLocations(100);

        Assert.assertEquals(100, nextScanLocations.size());
    }
}