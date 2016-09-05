package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.UserConfigDataService;
import hu.poketerkep.shared.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This class is for caching users, so there will be less data usage
 */
@Service
public class UserManagerService {

    private final UserConfigDataService userConfigDataService;
    private final int capacity = 100;
    private Queue<UserConfig> userConfigs = new ArrayBlockingQueue<>(capacity);

    @Autowired
    public UserManagerService(UserConfigDataService userConfigDataService) {
        this.userConfigDataService = userConfigDataService;
    }

    /**
     * Get a new user from the database
     *
     * @return
     */
    public synchronized Optional<UserConfig> getNextWorkingUser() {
        if (userConfigs.size() == 0) {
            fillQueue();
        }
        return Optional.ofNullable(userConfigs.poll());
    }

    private synchronized void fillQueue() {
        if (userConfigs.size() == 0) {
            userConfigs.addAll(userConfigDataService.getUnused(capacity));
        }
    }
}
