package hu.poketerkep.master.geo;

import hu.poketerkep.master.model.ScanPolygon;
import hu.poketerkep.shared.geo.Coordinate;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FloodFillTest {
    @Test
    public void generate() throws Exception {

        // Create a concave polygon
        ScanPolygon scanPolygon = new ScanPolygon();

        Coordinate[] vertices = new Coordinate[6];
        vertices[0] = Coordinate.fromDegrees(47.0000, 19.0000);
        vertices[1] = Coordinate.fromDegrees(47.0000, 18.0090);
        vertices[2] = Coordinate.fromDegrees(47.0005, 18.0095);
        vertices[3] = Coordinate.fromDegrees(47.0010, 18.0090);
        vertices[4] = Coordinate.fromDegrees(47.0010, 19.0000);
        vertices[5] = Coordinate.fromDegrees(47.0005, 19.0005);

        scanPolygon.setVertices(vertices);

        ///////////////////////// Construct

        FloodFill floodFill = new FloodFill(vertices);

        {
            //Assert if every field is right
            assertEquals(6, getPrivateField("nvert").getInt(floodFill));

            double delta = 0.000001;
            assertEquals(47.001, getPrivateField("xMax").getDouble(floodFill), delta);
            assertEquals(19.0005, getPrivateField("yMax").getDouble(floodFill), delta);
            assertEquals(47.0, getPrivateField("xMin").getDouble(floodFill), delta);
            assertEquals(18.009, getPrivateField("yMin").getDouble(floodFill), delta);
        }

        ///////////////////////// isInPolygon
        {
            assertTrue(isInPolygon(floodFill, Coordinate.fromDegrees(47.00050, 18.00975)));
            assertTrue(isInPolygon(floodFill, Coordinate.fromDegrees(47.00012, 18.00925)));

            assertFalse(isInPolygon(floodFill, Coordinate.fromDegrees(47.0005, 18.00900)));
            assertFalse(isInPolygon(floodFill, Coordinate.fromDegrees(47.002, 18.0095)));
            assertFalse(isInPolygon(floodFill, Coordinate.fromDegrees(46.009, 18.0095)));
        }


        ///////////////////////// Generate
        {
            ArrayList<Coordinate> coordinates = floodFill.generate();
            assertEquals(1076, coordinates.size());
        }
    }

    private Field getPrivateField(String name) throws NoSuchFieldException {
        Class<? extends FloodFill> clazz = FloodFill.class;
        Field declaredField = clazz.getDeclaredField(name);
        declaredField.setAccessible(true);
        return declaredField;
    }

    private boolean isInPolygon(FloodFill floodFill, Coordinate coordinate) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FloodFill> clazz = FloodFill.class;
        Method isInPolygon = clazz.getDeclaredMethod("isInPolygon", Coordinate.class);
        isInPolygon.setAccessible(true);
        return (boolean) isInPolygon.invoke(floodFill, coordinate);
    }

}