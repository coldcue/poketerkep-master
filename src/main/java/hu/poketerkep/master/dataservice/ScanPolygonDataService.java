package hu.poketerkep.master.dataservice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import hu.poketerkep.master.dynamodb.model.ScanPolygonDBItem;
import hu.poketerkep.master.model.ScanPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ScanPolygonDataService implements DataService<ScanPolygon> {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public ScanPolygonDataService(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public void save(ScanPolygon scanPolygon) {
        dynamoDBMapper.save(ScanPolygonDBItem.fromScanPolygon(scanPolygon));
    }

    @Override
    public void delete(ScanPolygon scanPolygon) {
        dynamoDBMapper.delete(ScanPolygonDBItem.fromScanPolygon(scanPolygon));
    }

    @Override
    public Collection<ScanPolygon> getAll() {
        return dynamoDBMapper.scan(ScanPolygonDBItem.class, new DynamoDBScanExpression()).stream()
                .map(ScanPolygonDBItem::toScanPolygon)
                .collect(Collectors.toList());
    }

}
