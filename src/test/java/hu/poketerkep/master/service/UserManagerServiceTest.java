package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.UserConfigDataService;
import hu.poketerkep.shared.model.RandomUserConfigGenerator;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserManagerServiceTest {

    @Test
    public void testNormalUsage() throws Exception {
        UserConfigDataService mock = mock(UserConfigDataService.class);
        when(mock.getUnused(100)).thenReturn(RandomUserConfigGenerator.generateN(100));

        UserManagerService userManagerService = new UserManagerService(mock);

        for (int i = 0; i < 200; i++) {
            assertTrue(userManagerService.getNextWorkingUser().isPresent());
        }
    }

    @Test
    public void testNoUsers() throws Exception {
        UserConfigDataService mock = mock(UserConfigDataService.class);
        when(mock.getUnused(100)).thenReturn(Collections.emptyList());

        UserManagerService userManagerService = new UserManagerService(mock);

        assertFalse(userManagerService.getNextWorkingUser().isPresent());
    }

}