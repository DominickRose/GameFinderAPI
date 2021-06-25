package com.ismadoro.daos;

import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.ResourceNotFound;
import org.testng.Assert;

import org.testng.annotations.Test;

import java.util.List;

public class PlayerDaoTests {

    private static final PlayerDao playerDao = new PlayerDaoLocal();
    private static final Player testPlayer = new Player(0, "Test", "Player", "testPlayer", "test", true, "a@email.com", 0, "WA", "");
    private static final Player testPlayer2 = new Player(0, "Test2", "Player", "testPlayer2", "test", false, "b@email.com", 0, "UT", "");


    @Test(priority = 1)
    void testAddPlayer() {
        playerDao.addPlayer(testPlayer);
        Assert.assertNotEquals(testPlayer.getPlayerId(), 0);
    }

    @Test(priority = 1)
    void testAddPlayer2() {
        playerDao.addPlayer(testPlayer2);
        Assert.assertNotEquals(testPlayer2.getPlayerId(), 0);
        Assert.assertNotEquals(testPlayer.getPlayerId(), testPlayer2.getPlayerId());
    }

    @Test(priority = 2)
    void testAddDuplicatePlayer() {
        try {
            playerDao.addPlayer(testPlayer2);
            Assert.fail();
        } catch (DuplicateResourceException duplicateResourceException) {
            //Succeed
        }
    }

    @Test(priority = 2)
    void testGetSinglePlayer() {
        try {
            Player player = playerDao.getSinglePlayer(testPlayer.getPlayerId());
            Assert.assertEquals(player.getPlayerId(), testPlayer.getPlayerId());
        } catch (ResourceNotFound resourceNotFound) {
            Assert.fail();
        }
    }

    @Test(priority = 2)
    void testGetInvalidPlayer() {
        try {
            playerDao.getSinglePlayer(0);
            Assert.fail();
        } catch (ResourceNotFound resourceNotFound) {
            //Success
        }
    }

    @Test(priority = 3)
    void testGetAllPlayer() {
        List<Player> result = playerDao.getAllPlayers();
        Assert.assertTrue(result.size() >= 2);
    }

    @Test(priority = 4)
    void testUpdatePlayer() {
        testPlayer.setPassword("hello");
        Player result = playerDao.updatePlayer(testPlayer);
        Assert.assertEquals(result.getPassword(), testPlayer.getPassword());
    }

    @Test(priority = 4)
    void testUpdateUnregisteredPlayer() {
        Player unregisteredPlayer = new Player(0, "Unregistered", "Player", "unregistered", "player", true, "c@email.com", 0, "WA", "");
        try {
            playerDao.updatePlayer(unregisteredPlayer);
            Assert.fail();
        } catch (ResourceNotFound resourceNotFound) {
            //Succeed
        }
    }

    @Test(priority = 4)
    void testUpdateToExistingUsername() {
        testPlayer.setUsername(testPlayer2.getUsername());
        try {
            playerDao.updatePlayer(testPlayer);
            Assert.fail();
        } catch (DuplicateResourceException duplicateResourceException) {
            //Succeed
        }
    }

    @Test(priority = 5)
    void testDeletePlayer() {
        boolean deleted = playerDao.deletePlayer(testPlayer.getPlayerId());
        Assert.assertTrue(deleted);
    }

    @Test(priority = 5)
    void testInvalidDelete() {
        try {
            playerDao.deletePlayer(0);
            Assert.fail();
        } catch (ResourceNotFound resourceNotFound) {
            //Succeed
        }
    }
}
