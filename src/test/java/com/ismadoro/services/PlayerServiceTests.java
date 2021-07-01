package com.ismadoro.services;

import com.ismadoro.daos.PlayerDao;
import com.ismadoro.dsa.RepeatSafeTrieTree;
import com.ismadoro.entities.Player;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerServiceTests {

    @Mock
    final PlayerDao mockPlayerDao = Mockito.mock(PlayerDao.class);
    PlayerService playerService = null;

    @BeforeMethod
    void playerDaoSetup() {
        Player testPlayer1 = new Player(1, "Test", "Play", "test1", "test", true, "a@email.com", "1234567890", "WA", "", "");
        Player testPlayer2 = new Player(2, "Test", "Playe", "test2", "test2", true, "a@email.com", "1234567890", "UT", "", "");
        Player testPlayer3 = new Player(3, "Test", "Player", "test3", "test3", true, "a@email.com", "1234567890", "WA", "", "");
        List<Player> mockList = new ArrayList<>();
        mockList.add(testPlayer1);
        mockList.add(testPlayer2);
        mockList.add(testPlayer3);

        Mockito.when(mockPlayerDao.getAllPlayers()).thenReturn(mockList);
        Mockito.when(mockPlayerDao.getSinglePlayer(1)).thenReturn(testPlayer1);
        Mockito.when(mockPlayerDao.getSinglePlayer(2)).thenReturn(testPlayer2);
        Mockito.when(mockPlayerDao.getSinglePlayer(3)).thenReturn(testPlayer3);
        playerService = new PlayerServiceImpl(mockPlayerDao);
    }

    @Test(priority = 1)
    void testValidLogin() {
        Player result = playerService.validateLogin("test1", "test");
        Assert.assertEquals(result.getPlayerId(), 1);
    }

    @Test(priority = 1)
    void testValidLogin2() {
        Player result = playerService.validateLogin("test2", "test2");
        Assert.assertEquals(result.getPlayerId(), 2);
    }

    @Test(priority = 1)
    void testValidLogin3() {
        Player result = playerService.validateLogin("test3", "test3");
        Assert.assertEquals(result.getPlayerId(), 3);
    }

    @Test(priority = 2)
    void testInvalidLogin() {
        Player result = playerService.validateLogin("invalid","invalid");
        Assert.assertNull(result);
    }

    @Test(priority = 2)
    void testValidLoginInvalidPassword() {
        Player result = playerService.validateLogin("test1", "test2");
        Assert.assertNull(result);
    }

    @Test(priority = 4)
    void testGetMostNames() {
        List<Integer> searchResults = playerService.searchForPlayersByName("TestPlay");
        Assert.assertEquals(searchResults.size(), 3);
    }

    @Test(priority = 4)
    void testGetMiddleNames() {
        List<Integer> searchResults = playerService.searchForPlayersByName("TestPlaye");
        Assert.assertEquals(searchResults.size(), 2);
    }

    @Test(priority = 4)
    void testGetLeastNames() {
        List<Integer> searchResults = playerService.searchForPlayersByName("TestPlayer");
        Assert.assertEquals(searchResults.size(), 1);
    }
}
