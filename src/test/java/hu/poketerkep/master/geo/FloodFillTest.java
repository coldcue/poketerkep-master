package hu.poketerkep.master.geo;

import hu.poketerkep.master.model.ScanPolygon;
import hu.poketerkep.master.tools.ScanPolygonGenerator;
import hu.poketerkep.shared.geo.Coordinate;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import static org.junit.Assert.*;

public class FloodFillTest {
    @Test
    public void generate() throws Exception {

        ///////////////////////// Construct

        ScanPolygon scanPolygon = ScanPolygonGenerator.generateConcanveTest();
        FloodFill floodFill = new FloodFill(scanPolygon.getVertices());

        {
            //Assert if every field is right
            assertEquals(6, getPrivateField("nvert").getInt(floodFill));

            double delta = 0.000001;
            assertEquals(47.001, getPrivateField("yMax").getDouble(floodFill), delta);
            assertEquals(19.0005, getPrivateField("xMax").getDouble(floodFill), delta);
            assertEquals(47.0, getPrivateField("yMin").getDouble(floodFill), delta);
            assertEquals(18.009, getPrivateField("xMin").getDouble(floodFill), delta);
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
            Collection<Coordinate> coordinates = floodFill.generate();
            assertEquals(760, coordinates.size());

            //Check if every point has a minimal distance
            for (Coordinate a : coordinates) {
                for (Coordinate b : coordinates) {
                    if (!a.equals(b)) {
                        assertTrue("Every point should have a minimum distance of 98.99 - 0.99 (98) meters", a.getDistance(b) > 0.098);
                    }
                }
            }
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