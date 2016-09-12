package hu.poketerkep.master.api;

import hu.poketerkep.master.dataservice.UserConfigDataService;
import hu.poketerkep.master.service.UserManagerService;
import hu.poketerkep.shared.api.UserAPIEndpoint;
import hu.poketerkep.shared.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController implements UserAPIEndpoint {

    private final UserManagerService userManagerService;
    private final UserConfigDataService userConfigDataService;

    @Autowired
    public UserController(UserManagerService userManagerService, UserConfigDataService userConfigDataService) {
        this.userManagerService = userManagerService;
        this.userConfigDataService = userConfigDataService;
    }

    @GetMapping("/nextUser")
    public ResponseEntity<UserConfig[]> nextUser(@RequestParam int limit) {
        Optional<Collection<UserConfig>> nextWorkingUser = userManagerService.getNextWorkingUser(limit);

        if (nextWorkingUser.isPresent()) {
            UserConfig[] userConfigs = nextWorkingUser.get().stream()
                    .toArray(UserConfig[]::new);

            return ResponseEntity.ok(userConfigs);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    @PostMapping("/banUser")
    public ResponseEntity<UserConfig> banUser(@RequestBody UserConfig userConfig) {
        userConfig.setBanned(true);
        userConfig.setLastUsed(Instant.now().toEpochMilli());
        userConfigDataService.save(userConfig);
        // Remove user from queue
        userManagerService.onBanned(userConfig);
        return ResponseEntity.ok(userConfig);
    }

    @Override
    @PostMapping("/releaseUser")
    public ResponseEntity<UserConfig> releaseUser(@RequestBody UserConfig userConfig) {
        userConfig.setLastUsed(Instant.now().toEpochMilli());
        userConfigDataService.save(userConfig);
        return ResponseEntity.ok(userConfig);
    }

    @Override
    @PostMapping("/addUser")
    public ResponseEntity<UserConfig> addUser(@RequestBody UserConfig userConfig) {
        userConfigDataService.save(userConfig);
        return ResponseEntity.ok(userConfig);
    }
}
