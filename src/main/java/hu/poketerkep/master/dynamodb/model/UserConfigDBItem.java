package hu.poketerkep.master.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import hu.poketerkep.shared.model.UserConfig;

@DynamoDBTable(tableName = "userConfig")
public class UserConfigDBItem {
    @DynamoDBHashKey
    private String userName;
    @DynamoDBAttribute
    private long lastUsed;
    @DynamoDBAttribute
    private int banned;

    public static UserConfigDBItem fromUserConfig(UserConfig userConfig) {
        UserConfigDBItem item = new UserConfigDBItem();

        item.userName = userConfig.getUserName();
        item.lastUsed = userConfig.getLastUsed();
        item.banned = userConfig.isBanned() ? 1 : 0;

        return item;
    }

    public UserConfig toUserConfig() {
        return new UserConfig(userName, lastUsed, banned == 1);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }
}
