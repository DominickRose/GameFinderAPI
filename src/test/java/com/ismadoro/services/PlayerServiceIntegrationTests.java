package com.ismadoro.services;

import com.ismadoro.daos.PlayerDao;
import com.ismadoro.daos.PlayerDaoLocal;
import com.ismadoro.daos.PlayerDaoPostgres;
import com.ismadoro.entities.Player;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class PlayerServiceIntegrationTests {
    PlayerDao playerDao = new PlayerDaoLocal();
    PlayerService playerService = new PlayerServiceImpl(playerDao);

    @Test(priority = 1)
    void testCorrectAdd() {
        Player testPlayer = new Player(0, "Test", "Play", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "", "");
        playerService.addPlayer(testPlayer);
        Player returnedPlayer = playerService.validateLogin(testPlayer.getUsername(), testPlayer.getPassword());
        Assert.assertEquals(returnedPlayer.getPlayerId(), testPlayer.getPlayerId());
    }

    @Test(priority = 2, dependsOnMethods = {"testCorrectAdd", "testCorrectDelete"})
    void testCorrectUpdate() {
        Player testPlayer = new Player(0, "Test", "Play", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "", "");
        playerService.addPlayer(testPlayer);

        String previousUsername = testPlayer.getUsername();
        String previousPassword = testPlayer.getPassword();
        Player testPlayer2 = new Player(testPlayer.getPlayerId(), "Test", "Play", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "", "");
        Player newPlayer = playerService.updatePlayer(testPlayer2);
        Assert.assertNull(playerService.validateLogin(previousUsername, previousPassword));

        Assert.assertEquals(playerService.validateLogin(newPlayer.getUsername(), newPlayer.getPassword()).getPlayerId(), newPlayer.getPlayerId());
        playerService.deletePlayer(newPlayer.getPlayerId());
    }

    @Test(priority = 2, dependsOnMethods = {"testCorrectAdd"})
    void testCorrectDelete() {
        Player testPlayer = new Player(0, "Test", "Play", UUID.randomUUID().toString().substring(0, 20), "test", true, "a@email.com", "1234567890", "WA", "", "");
        playerService.addPlayer(testPlayer);

        String previousUsername = testPlayer.getUsername();
        String previousPassword = testPlayer.getPassword();
        playerService.deletePlayer(testPlayer.getPlayerId());
        Assert.assertNull(playerService.validateLogin(previousUsername, previousPassword));
    }
}
