package hu.poketerkep.master.dynamodb.model;

import org.junit.Test;

public class ScanPolygonDBItemTest {

    @Test(expected = RuntimeException.class)
    public void toScanPolygon() throws Exception {
        ScanPolygonDBItem scanPolygonDBItem = new ScanPolygonDBItem();
        scanPolygonDBItem.setId("asd");
        scanPolygonDBItem.setVertices("not valid json");

        scanPolygonDBItem.toScanPolygon();
    }
}