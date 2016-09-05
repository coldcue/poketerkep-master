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
    public ResponseEntity<UserConfig> nextUser() {
        Optional<UserConfig> nextWorkingUser = userManagerService.getNextWorkingUser();

        if (nextWorkingUser.isPresent()) {
            return ResponseEntity.ok(nextWorkingUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @Override
    @PostMapping("/banUser")
    public ResponseEntity<UserConfig> banUser(@RequestBody UserConfig userConfig) {
        userConfig.setBanned(true);
        userConfig.setLastUsed(Instant.now().toEpochMilli());
        userConfigDataService.save(userConfig);
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
