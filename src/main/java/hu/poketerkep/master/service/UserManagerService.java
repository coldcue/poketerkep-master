package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.UserConfigDataService;
import hu.poketerkep.shared.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class is for caching users, so there will be less data usage
 */
@Service
public class UserManagerService {

    private final UserConfigDataService userConfigDataService;
    private Queue<UserConfig> userConfigs = new PriorityBlockingQueue<>(1000, UserConfig.LAST_USED_COMPARATOR);

    @Autowired
    public UserManagerService(UserConfigDataService userConfigDataService) {
        this.userConfigDataService = userConfigDataService;
    }

    /**
     * Get a new user from the database
     *
     * @return
     */
    public Optional<UserConfig> getNextWorkingUser() {
        if (userConfigs.size() == 0) {
            fillQueue();
        }

        Optional<UserConfig> optional = Optional.ofNullable(userConfigs.poll());

        //Update last used
        if (optional.isPresent()) {
            UserConfig userConfig = optional.get();
            userConfig.setLastUsed(Instant.now().toEpochMilli());
            userConfigDataService.save(userConfig);

            //Put back to the Queue
            userConfigs.offer(userConfig);
        }

        return optional;
    }

    private synchronized void fillQueue() {
        if (userConfigs.size() == 0) {
            userConfigs.addAll(userConfigDataService.getAllUsable());
        }
    }

    public void onBanned(UserConfig userConfig) {
        userConfigs.removeIf(o -> o.getUserName().equals(userConfig.getUserName()));
    }
}
