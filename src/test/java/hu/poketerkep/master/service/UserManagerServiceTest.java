package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.UserConfigDataService;
import hu.poketerkep.shared.model.RandomUserConfigGenerator;
import hu.poketerkep.shared.model.UserConfig;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserManagerServiceTest {

    @Test
    public void testNormalUsage() throws Exception {
        UserConfigDataService mock = mock(UserConfigDataService.class);
        when(mock.getAllUsable()).thenReturn(RandomUserConfigGenerator.generateN(100));

        UserManagerService userManagerService = new UserManagerService(mock);

        for (int i = 0; i < 200; i++) {
            assertTrue(userManagerService.getNextWorkingUser(10).isPresent());
        }
    }

    @Test
    public void testNoUsers() throws Exception {
        UserConfigDataService mock = mock(UserConfigDataService.class);
        when(mock.getAllUsable()).thenReturn(Collections.emptyList());

        UserManagerService userManagerService = new UserManagerService(mock);

        assertFalse(userManagerService.getNextWorkingUser(10).isPresent());
    }

    @Test
    public void testOrder() throws Exception {
        UserConfig uc0 = RandomUserConfigGenerator.generateWorking();
        uc0.setLastUsed(0);

        UserConfig uc100 = RandomUserConfigGenerator.generateWorking();
        uc100.setLastUsed(100);

        UserConfig uc101 = RandomUserConfigGenerator.generateWorking();
        uc101.setLastUsed(101);

        UserConfig uc102 = RandomUserConfigGenerator.generateWorking();
        uc102.setLastUsed(102);


        UserConfigDataService mock = mock(UserConfigDataService.class);
        when(mock.getAllUsable()).thenReturn(Arrays.asList(uc0, uc100, uc102, uc101));

        UserManagerService userManagerService = new UserManagerService(mock);

        assertEquals(uc0, userManagerService.getNextWorkingUser(1).get().iterator().next());
        Thread.sleep(100);
        assertEquals(uc100, userManagerService.getNextWorkingUser(1).get().iterator().next());
        Thread.sleep(100);
        assertEquals(uc101, userManagerService.getNextWorkingUser(1).get().iterator().next());
        Thread.sleep(100);
        assertEquals(uc102, userManagerService.getNextWorkingUser(1).get().iterator().next());
        Thread.sleep(100);

        assertEquals(uc0, userManagerService.getNextWorkingUser(1).get().iterator().next());
        assertEquals(uc100, userManagerService.getNextWorkingUser(1).get().iterator().next());
        assertEquals(uc101, userManagerService.getNextWorkingUser(1).get().iterator().next());
        assertEquals(uc102, userManagerService.getNextWorkingUser(1).get().iterator().next());
    }

    @Test
    public void testOnBanned() throws Exception {
        UserConfig a = RandomUserConfigGenerator.generateWorking();
        a.setUserName("a");

        UserConfig b = RandomUserConfigGenerator.generateWorking();
        b.setUserName("b");


        UserConfigDataService mock = mock(UserConfigDataService.class);
        when(mock.getAllUsable()).thenReturn(Arrays.asList(a, b));

        UserManagerService userManagerService = new UserManagerService(mock);

        userManagerService.getNextWorkingUser(1);

        userManagerService.onBanned(a);

        Thread.sleep(100);
        assertEquals(b, userManagerService.getNextWorkingUser(1).get().iterator().next());
        Thread.sleep(100);
        assertEquals(b, userManagerService.getNextWorkingUser(1).get().iterator().next());

    }

}