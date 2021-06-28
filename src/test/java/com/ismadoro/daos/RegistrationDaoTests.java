package com.ismadoro.daos;

import com.ismadoro.entities.Event;
import com.ismadoro.entities.Player;
import com.ismadoro.entities.Registration;
import com.ismadoro.exceptions.ResourceNotFound;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

public class RegistrationDaoTests {
    private static RegistrationDao registrationDao = new RegistrationDaoLocal();
    private static PlayerDao playerDao = new PlayerDaoLocal();
    private static EventDao eventDao = null; //Initialize once finished

    private static final Player testPlayer1 = new Player(0, "Player", "One", UUID.randomUUID().toString(), "test", true, "a@email.com", "1234567890", "WA", "Spokane");;
    private static final Player testPlayer2 = new Player(0, "Player", "Two", UUID.randomUUID().toString(), "test", true, "a@email.com", "1234567890", "WA", "Spokane");;

    //Once Events are finished, initialize these
    private static final Event testEvent1 = null;
    private static final Event testEvent2 = null;

    private final Registration testRegistration = new Registration(0, testPlayer1.getPlayerId(), 1); //Edit this to be testEvent1 id once that's completed
    private final Registration testRegistration2 = new Registration(0, testPlayer2.getPlayerId(), 2); //Edit this to be testEvent1 id once that's completed

    @BeforeClass
    void setup() {
        playerDao.addPlayer(testPlayer1);
        playerDao.addPlayer(testPlayer2);

        eventDao.addPlayer(testEvent1); //Refactor these once complete
        eventDao.addPlayer(testEvent2);
    }

    @AfterClass
    void cleanup() {
        playerDao.deletePlayer(testPlayer1.getPlayerId());
        playerDao.deletePlayer(testPlayer2.getPlayerId());

//        eventDao.deletePlayer(testEvent1.getEventId());
//        eventDao.deletePlayer(testEvent2.getEventId());
    }

    @Test(priority = 1)
    void testAddRegistration1() {
        Registration result = registrationDao.addRegistration(testRegistration);
        Assert.assertNotEquals(result.getRegistrationId(), 0);
    }

    @Test(priority = 2)
    void testAddRegistration2() {
        Registration result = registrationDao.addRegistration(testRegistration2);
        Assert.assertNotEquals(result.getRegistrationId(), testRegistration.getRegistrationId());
    }

    @Test(priority = 3)
    void testGetAllRegistrations(){
        List<Registration> registrationList = registrationDao.getAllRegistrations();
        Assert.assertTrue(registrationList.size() >= 2);
    }

    @Test(priority = 4)
    void testGetSingleRegistration() {
        Registration result = registrationDao.getSingleRegistation(testRegistration.getRegistrationId());
        Assert.assertEquals(result.getRegistrationId(), testRegistration.getRegistrationId());
    }

    @Test(priority = 4)
    void testGetSingleRegistration2() {
        Registration result = registrationDao.getSingleRegistation(testRegistration2.getRegistrationId());
        Assert.assertEquals(result.getRegistrationId(), testRegistration2.getRegistrationId());
    }

    @Test(priority = 4)
    void testGetInvalidRegistration() {
        try {
            registrationDao.getSingleRegistation(0);
            Assert.fail();
        } catch (ResourceNotFound resourceNotFound) {
            //Succeed
        }
    }

    @Test(priority = 5)
    void testUpdateSingleRegistration() {
        testRegistration.setPlayerId(testPlayer2.getPlayerId());
        Registration result = registrationDao.updateRegistration(testRegistration);
        Assert.assertEquals(result.getPlayerId(), testPlayer2.getPlayerId());
    }

    @Test(priority = 6)
    void testUpdateInvalidRegistration() {
        Registration invalid = new Registration(0, 0, 0);
        try {
            registrationDao.updateRegistration(invalid);
            Assert.fail();
        } catch (ResourceNotFound resourceNotFound) {
            //Succeed
        }
    }

    @Test(priority = 7)
    void testDeleteRegistration() {
        boolean result = registrationDao.deleteRegistration(testRegistration2.getRegistrationId());
        Assert.assertTrue(result);
    }

    @Test(priority = 8)
    void testInvalidDelete() {
        try {
            registrationDao.deleteRegistration(testRegistration2.getRegistrationId());
            Assert.fail();
        } catch (ResourceNotFound resourceNotFound) {
            //Succeed
        }
    }
}
