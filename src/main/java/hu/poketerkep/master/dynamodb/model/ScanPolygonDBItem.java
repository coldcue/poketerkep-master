package hu.poketerkep.master.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.poketerkep.master.model.ScanPolygon;
import hu.poketerkep.shared.geo.Coordinate;

import java.io.IOException;

@DynamoDBTable(tableName = "scanPolygon")
public class ScanPolygonDBItem {
    private static ObjectMapper objectMapper = new ObjectMapper();
    @DynamoDBHashKey
    private String id;
    @DynamoDBAttribute
    private String vertices;

    public static ScanPolygonDBItem fromScanPolygon(ScanPolygon scanPolygon) {
        ScanPolygonDBItem scanPolygonDBItem = new ScanPolygonDBItem();

        scanPolygonDBItem.setId(scanPolygon.getId());
        try {
            scanPolygonDBItem.setVertices(objectMapper.writeValueAsString(scanPolygon.getVertices()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return scanPolygonDBItem;
    }

    public ScanPolygon toScanPolygon() {
        Coordinate[] coordinates;
        try {
            coordinates = objectMapper.readValue(vertices, Coordinate[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ScanPolygon scanPolygon = new ScanPolygon();

        scanPolygon.setId(id);
        scanPolygon.setVertices(coordinates);

        return scanPolygon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVertices() {
        return vertices;
    }

    public void setVertices(String vertices) {
        this.vertices = vertices;
    }
}
