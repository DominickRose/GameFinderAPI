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
//    private static RegistrationDao registrationDao = new RegistrationDaoPostgres();

    private static PlayerDao playerDao = new PlayerDaoLocal();
//    private static PlayerDao playerDao = new PlayerDaoPostgres();

    private static EventDao eventDao = new EventDaoLocal();
//    private static EventDao eventDao = new EventDaoPostgres();


    private static final Player testPlayer1 = new Player(0, "Player", "One", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "Spokane", "");;
    private static final Player testPlayer2 = new Player(0, "Player", "Two", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "Spokane", "");;

    //Once Events are finished, initialize these
    private static Event testEvent1 = null;
    private static Event testEvent2 = null;

    private final Registration testRegistration = new Registration(0, 0, 0); //Edit this to be testEvent1 id once that's completed
    private final Registration testRegistration2 = new Registration(0, 0, 0); //Edit this to be testEvent1 id once that's completed

    @BeforeClass
    void setup() {
        playerDao.addPlayer(testPlayer1);
        playerDao.addPlayer(testPlayer2);

        testEvent1 = new Event(testPlayer1.getPlayerId(), 0, 1, "NYC", "NY", "Hello", "Beginner", "Test", "4-on-4", 20);
        testEvent2 = new Event(testPlayer2.getPlayerId(), 0, 1, "NYC", "NY", "Hello", "Beginner", "Test", "4-on-4", 20);

        eventDao.addEvent(testEvent1); //Refactor these once complete
        eventDao.addEvent(testEvent2);

        testRegistration.setPlayerId(testPlayer1.getPlayerId());
        testRegistration.setEventId(testEvent1.getEventId());

        testRegistration2.setPlayerId(testPlayer2.getPlayerId());
        testRegistration2.setEventId(testEvent2.getEventId());
    }

    @AfterClass
    void cleanup() {
        playerDao.deletePlayer(testPlayer1.getPlayerId());
        playerDao.deletePlayer(testPlayer2.getPlayerId());

        eventDao.deleteEvent(testEvent1.getEventId());
        eventDao.deleteEvent(testEvent2.getEventId());
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
