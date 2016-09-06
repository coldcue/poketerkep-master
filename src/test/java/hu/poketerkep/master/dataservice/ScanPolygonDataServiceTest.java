package hu.poketerkep.master.dataservice;

import hu.poketerkep.master.config.DynamoDBConfig;
import hu.poketerkep.master.model.ScanPolygon;
import hu.poketerkep.master.tools.ScanPolygonGenerator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Optional;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DynamoDBConfig.class, ScanPolygonDataService.class})
public class ScanPolygonDataServiceTest {

    @Autowired
    private ScanPolygonDataService scanPolygonDataService;

    @Test
    public void test() throws Exception {
        ScanPolygon scanPolygon = ScanPolygonGenerator.generateConcanveTest();

        //Save
        scanPolygonDataService.save(scanPolygon);

        //Fetch
        Optional<ScanPolygon> inDatabase = isInDatabase(scanPolygon, scanPolygonDataService.getAll());
        Assert.assertTrue("It should be in the databse", inDatabase.isPresent());

        ScanPolygon p = inDatabase.get();
        Assert.assertArrayEquals("Vertices should be the same", scanPolygon.getVertices(), p.getVertices());

        //Delete
        scanPolygonDataService.delete(scanPolygon);
        Assert.assertFalse("It shouldn't be in the database", isInDatabase(scanPolygon, scanPolygonDataService.getAll()).isPresent());

    }

    private Optional<ScanPolygon> isInDatabase(ScanPolygon scanPolygon, Collection<ScanPolygon> scanPolygons) {
        for (ScanPolygon p : scanPolygons) {
            if (p.getId().equals(scanPolygon.getId())) {
                return Optional.of(p);
            }
        }

        return Optional.empty();
    }

}