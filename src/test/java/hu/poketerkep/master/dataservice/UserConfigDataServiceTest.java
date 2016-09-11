package hu.poketerkep.master.dataservice;

import hu.poketerkep.master.config.DynamoDBConfig;
import hu.poketerkep.shared.model.UserConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DynamoDBConfig.class, UserConfigDataService.class})
public class UserConfigDataServiceTest {

    @Autowired
    UserConfigDataService userConfigDataService;

    @Test
    public void test() throws Exception {
        UserConfig userConfig = new UserConfig("TEST", Instant.now().toEpochMilli(), false);

        //Save
        userConfigDataService.save(userConfig);

        //Fetch
        Optional<UserConfig> inDatabase = isInDatabase(userConfig, userConfigDataService.getAll());
        Assert.assertTrue("It should be in the databse", inDatabase.isPresent());


        //Delete
        userConfigDataService.delete(userConfig);
        Assert.assertFalse("It shouldn't be in the database", isInDatabase(userConfig, userConfigDataService.getAll()).isPresent());

    }

    @Test
    public void getUnused() throws Exception {
        UserConfig banned = new UserConfig("BANNED_TEST", Instant.now().minus(Duration.ofDays(1)).toEpochMilli(), true);
        UserConfig justUsed = new UserConfig("JUST_USED_TEST", Instant.now().toEpochMilli(), false);
        UserConfig ok = new UserConfig("OK_TEST", Instant.now().minus(Duration.ofDays(1)).toEpochMilli(), false);

        userConfigDataService.save(banned);
        userConfigDataService.save(justUsed);
        userConfigDataService.save(ok);

        Collection<UserConfig> unused = userConfigDataService.getAllUsable();

        Assert.assertFalse(isInDatabase(banned, unused).isPresent());
        Assert.assertFalse(isInDatabase(justUsed, unused).isPresent());
        Assert.assertTrue(isInDatabase(ok, unused).isPresent());

        //Clean up
        userConfigDataService.delete(banned);
        userConfigDataService.delete(justUsed);
        userConfigDataService.delete(ok);
    }

    private Optional<UserConfig> isInDatabase(UserConfig userConfig, Collection<UserConfig> userConfigs) {
        for (UserConfig p : userConfigs) {
            if (p.getUserName().equals(userConfig.getUserName())) {
                return Optional.of(p);
            }
        }

        return Optional.empty();
    }

}