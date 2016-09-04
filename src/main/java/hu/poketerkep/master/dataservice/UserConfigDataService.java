package hu.poketerkep.master.dataservice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import hu.poketerkep.master.config.Constants;
import hu.poketerkep.master.dynamodb.model.UserConfigDBItem;
import hu.poketerkep.shared.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data service for user configs
 */
@Service
public class UserConfigDataService implements DataService<UserConfig> {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public UserConfigDataService(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }


    /**
     * Create a User Config
     *
     * @param userConfig user config
     */
    @Override
    public void save(UserConfig userConfig) {
        dynamoDBMapper.save(UserConfigDBItem.fromUserConfig(userConfig));
    }


    /**
     * Delete a user config
     *
     * @param userConfig user config
     */
    @Override
    public void delete(UserConfig userConfig) {
        dynamoDBMapper.delete(UserConfigDBItem.fromUserConfig(userConfig));
    }

    @Override
    public Collection<UserConfig> getAll() {
        return dynamoDBMapper.scan(UserConfigDBItem.class, new DynamoDBScanExpression()).stream()
                .map(UserConfigDBItem::toUserConfig)
                .collect(Collectors.toList());
    }

    /**
     * Get unused and not banned users
     *
     * @param limit how many
     * @return a collection of users
     */
    public Collection<UserConfig> getUnused(int limit) {
        long time = Instant.now().minusSeconds(Constants.UNUSED_USER_TIME_SECONDS).toEpochMilli();

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":time", new AttributeValue().withN(Long.toString(time)));
        eav.put(":banned", new AttributeValue().withN("0"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("lastUsed < :time and banned = :banned")
                .withExpressionAttributeValues(eav)
                .withLimit(limit);

        return dynamoDBMapper.scan(UserConfigDBItem.class, scanExpression).stream()
                .map(UserConfigDBItem::toUserConfig)
                .collect(Collectors.toList());
    }

}
