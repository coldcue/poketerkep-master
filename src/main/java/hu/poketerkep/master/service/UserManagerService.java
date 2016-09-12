package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.UserConfigDataService;
import hu.poketerkep.shared.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/**
 * This class is for caching users, so there will be less data usage
 */
@Service
public class UserManagerService {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final UserConfigDataService userConfigDataService;
    private final Queue<UserConfig> userConfigs = new PriorityQueue<>(UserConfig.LAST_USED_COMPARATOR);

    private Semaphore refreshPermit = new Semaphore(1);

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
        // If there are no users, fill the queue
        if (userConfigs.size() == 0) {
            refreshUsers();
        }

        Optional<UserConfig> optional = Optional.empty();
        synchronized (userConfigs) {
            optional = Optional.ofNullable(userConfigs.poll());
        }

        //Update last used
        if (optional.isPresent()) {
            UserConfig userConfig = optional.get();

            //Update last used time
            userConfig.setLastUsed(Instant.now().toEpochMilli());

            synchronized (userConfigs) {
                //Put back to the Queue
                userConfigs.add(userConfig);
            }
        }

        log.info("Getting user - userconfigs size:" + userConfigs.size());

        return optional;
    }

    /**
     * Refresh users every 8 hours
     */
    @Scheduled(fixedRate = 8 * 60 * 60 * 1000)
    @Async
    public void refreshUsers() {
        if (refreshPermit.tryAcquire()) {
            if (userConfigs.size() == 0) {
                synchronized (userConfigs) {
                    userConfigs.addAll(userConfigDataService.getAllUsable());
                }
                log.info("UserConfigs refreshed with " + userConfigs.size() + " users");
            }
            refreshPermit.release();
        } else {
            try {
                refreshPermit.acquire();
                refreshPermit.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Remove the user from the queue when it is banned
     *
     * @param userConfig
     */

    public synchronized void onBanned(UserConfig userConfig) {
        synchronized (userConfigs) {
            userConfigs.removeIf(o -> o.getUserName().equals(userConfig.getUserName()));
        }
    }
}
