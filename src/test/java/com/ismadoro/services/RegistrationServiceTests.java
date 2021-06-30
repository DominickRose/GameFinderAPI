package com.ismadoro.services;

import com.ismadoro.daos.RegistrationDao;
import com.ismadoro.entities.Registration;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class RegistrationServiceTests {

    final RegistrationDao registrationDao = Mockito.mock(RegistrationDao.class);
    RegistrationService registrationService = null;

    @BeforeMethod
    void mockSetup() {
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(new Registration(1, 1, 1));
        registrationList.add(new Registration(2, 1, 2));
        registrationList.add(new Registration(4, 1, 3));
        registrationList.add(new Registration(3, 2, 1));
        registrationList.add(new Registration(5, 2, 2));

        Mockito.when(registrationDao.getAllRegistrations()).thenReturn(registrationList);
        registrationService = new RegistrationServiceImpl(registrationDao);
    }

    @Test(priority = 1)
    void testGetPlayer1Events() {
        List<Integer> result = registrationService.getAllEventsForPlayer(1);
        Assert.assertEquals(result.size(), 3);
    }

    @Test(priority = 1)
    void testGetPlayer2Events() {
        List<Integer> result = registrationService.getAllEventsForPlayer(2);
        Assert.assertEquals(result.size(), 2);
    }

    @Test(priority = 1)
    void testGetEmptyRegistrationEvents() {
        List<Integer> result = registrationService.getAllEventsForPlayer(3);
        Assert.assertEquals(result.size(), 0);
    }

    @Test(priority = 2)
    void testGetEvent1Players() {
        List<Integer> result = registrationService.getAllPlayersForEvent(1);
        Assert.assertEquals(result.size(), 2);
    }

    @Test(priority = 2)
    void testGetEvent3Players() {
        List<Integer> result = registrationService.getAllPlayersForEvent(3);
        Assert.assertEquals(result.size(), 1);
    }

    @Test(priority = 2)
    void testGetEmptyEvent() {
        List<Integer> results = registrationService.getAllPlayersForEvent(0);
        Assert.assertEquals(results.size(), 0);
    }
}
