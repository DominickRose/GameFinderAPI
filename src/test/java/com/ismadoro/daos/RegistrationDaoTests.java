package com.ismadoro.daos;

import com.ismadoro.entities.Event;
import com.ismadoro.entities.Player;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.UUID;

public class RegistrationDaoTests {
    private static RegistrationDao registrationDao = null;
    private static PlayerDao playerDao = null;
    private static EventDao eventDao = null;

    private Player testPlayer1;
    private Player testPlayer2;
    
    private Event testEvent1;
    private Event testEvent2;

    @BeforeClass
    void setup() {
        //Assign values for daos

        playerDao = new PlayerDaoLocal();

        testPlayer1 = new Player(0, "Player", "One", UUID.randomUUID().toString(), "test", true, "a@email.com", 1234567890, "WA", "Spokane");
        testPlayer2 = new Player(0, "Player", "Two", UUID.randomUUID().toString(), "test", true, "a@email.com", 1234567890, "WA", "Spokane");

        playerDao.addPlayer(testPlayer1);
        playerDao.addPlayer(testPlayer2);

        //Need working Event information before I can do this
    }

    @AfterClass
    void cleanup() {
        playerDao.deletePlayer(testPlayer1.getPlayerId());
        playerDao.deletePlayer(testPlayer2.getPlayerId());
    }
}
