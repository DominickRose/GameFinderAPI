package com.ismadoro.daos;

import com.ismadoro.entities.Event;
import com.ismadoro.entities.Player;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.UUID;

public class RegistrationDaoTests {
    private static RegistrationDao registrationDao = null;
    private static PlayerDao playerDao = new PlayerDaoLocal();
    private static EventDao eventDao = null; //Initialize once finished

    private static final Player testPlayer1 = new Player(0, "Player", "One", UUID.randomUUID().toString(), "test", true, "a@email.com", 1234567890, "WA", "Spokane");;
    private static final Player testPlayer2 = new Player(0, "Player", "Two", UUID.randomUUID().toString(), "test", true, "a@email.com", 1234567890, "WA", "Spokane");;

    //Once Events are finished, initialize these
    private static final Event testEvent1 = null;
    private static final Event testEvent2 = null;

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

    
}
