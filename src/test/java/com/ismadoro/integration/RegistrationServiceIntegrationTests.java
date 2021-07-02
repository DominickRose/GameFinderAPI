package com.ismadoro.integration;

import com.ismadoro.daos.*;
import com.ismadoro.entities.Event;
import com.ismadoro.entities.Player;
import com.ismadoro.entities.Registration;
import com.ismadoro.services.RegistrationService;
import com.ismadoro.services.RegistrationServiceImpl;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.UUID;

public class RegistrationServiceIntegrationTests {

    private RegistrationDao registrationDao;
    private PlayerDao playerDao;
    private EventDao eventDao;

    private RegistrationService registrationService;

    private Player testPlayer;
    private Player testPlayer2;
    private Event testEvent;

    private Registration testRegistration;
    private Registration testRegistration2;

    @BeforeClass
    @Parameters({"database"})
    void setup(String database) {
        if (database.equals("postgres")) {
            playerDao = new PlayerDaoPostgres();
            eventDao = new EventDaoPostgres();
            registrationDao = new RegistrationDaoPostgres();
        }
        else {
            playerDao = new PlayerDaoLocal();
            eventDao = new EventDaoLocal();
            registrationDao = new RegistrationDaoLocal();
        }

        testPlayer = new Player(0, "Test", "Play", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "", "");
        testPlayer2 = new Player(0, "Test", "Play", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "", "");
        playerDao.addPlayer(testPlayer);
        playerDao.addPlayer(testPlayer2);

        testEvent = new Event(testPlayer.getPlayerId(), 0, 1111, "NYC", "WA", "FDS", "Beginner", "fjkdshjkfhsdhfsdahfashf", "4-on-4", 20);
        eventDao.addEvent(testEvent);

        registrationService = new RegistrationServiceImpl(registrationDao);
    }

    @AfterClass
    void cleanup() {
        playerDao.deletePlayer(testPlayer.getPlayerId());
        playerDao.deletePlayer(testPlayer2.getPlayerId());
        eventDao.deleteEvent(testEvent.getEventId());
    }

    @Test(priority = 1)
    void testAddingToMap() {
        testRegistration = registrationService.addRegistration(new Registration(0, testPlayer.getPlayerId(), testEvent.getEventId()));
        Assert.assertTrue(registrationService.isPlayerRegisteredForEvent(testRegistration.getPlayerId(), testRegistration.getEventId())); //Check event was added
        testRegistration2 = registrationService.addRegistration(new Registration(0, testPlayer2.getPlayerId(), testEvent.getEventId()));

        Assert.assertTrue(registrationService.isPlayerRegisteredForEvent(testRegistration.getPlayerId(), testRegistration.getEventId())); //Check first event still present
        Assert.assertTrue(registrationService.isPlayerRegisteredForEvent(testRegistration2.getPlayerId(), testRegistration2.getEventId())); //Check second event added
    }

    @Test(priority = 1, dependsOnMethods = {"testAddingToMap"})
    void testDeletingFromMap() {
        registrationService.deleteRegistration(testRegistration.getRegistrationId());
        Assert.assertTrue(registrationService.isPlayerRegisteredForEvent(testRegistration2.getPlayerId(), testRegistration2.getEventId())); //Check second event added
        Assert.assertFalse(registrationService.isPlayerRegisteredForEvent(testRegistration.getPlayerId(), testRegistration.getEventId())); //Check first event still present

        registrationService.deleteRegistration(testRegistration2.getRegistrationId());
        Assert.assertFalse(registrationService.isPlayerRegisteredForEvent(testRegistration2.getPlayerId(), testRegistration2.getEventId())); //Check second event added
    }
}
