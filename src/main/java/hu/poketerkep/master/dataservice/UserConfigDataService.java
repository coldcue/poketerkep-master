package hu.poketerkep.master.dataservice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import hu.poketerkep.master.dynamodb.model.UserConfigDBItem;
import hu.poketerkep.shared.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Data service for user configs
 */
@Service
public class UserConfigDataService {

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
    public void save(UserConfig userConfig) {
        dynamoDBMapper.save(UserConfigDBItem.fromUserConfig(userConfig));
    }


    /**
     * Delete a user config
     *
     * @param userConfig user config
     */
    public void delete(UserConfig userConfig) {
        dynamoDBMapper.delete(UserConfigDBItem.fromUserConfig(userConfig));
    }

}
