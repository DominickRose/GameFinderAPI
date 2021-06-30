package com.ismadoro.services;

import com.ismadoro.daos.PlayerDao;
import com.ismadoro.dsa.RepeatSafeTrieTree;
import com.ismadoro.entities.Player;

import java.util.List;

public class PlayerServiceImpl implements PlayerService{

    private PlayerDao playerDao;
    private RepeatSafeTrieTree playerTree;

    public PlayerServiceImpl(PlayerDao playerDao, RepeatSafeTrieTree playerTree) {
        this.playerDao = playerDao;
        this.playerTree = playerTree;
    }

    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public Player addPlayer(Player player) {
        Player addedPlayer = this.playerDao.addPlayer(player);
        this.playerTree.addWord(addedPlayer.getFullName(), addedPlayer.getPlayerId());
        return addedPlayer;
    }

    @Override
    public Player getSinglePlayer(int playerId) {
        return this.playerDao.getSinglePlayer(playerId);
    }

    @Override
    public List<Player> getAllPlayers() {
        return this.playerDao.getAllPlayers();
    }

    @Override
    public Player updatePlayer(Player player) {
        String previousName = getSinglePlayer(player.getPlayerId()).getFullName();
        Player updatedPlayer = this.playerDao.updatePlayer(player);
        this.playerTree.updateWord(previousName, player.getPlayerId(), updatedPlayer.getFullName());
        return updatedPlayer;
    }

    @Override
    public boolean deletePlayer(int playerId) {
        String name = getSinglePlayer(playerId).getFullName();
        boolean result = this.playerDao.deletePlayer(playerId);
        this.playerTree.removeWord(name, playerId);
        return result;
    }

    @Override
    public Player validateLogin(String username, String password) {
        List<Player> allPlayers = this.playerDao.getAllPlayers();
        Player toReturn = null;
        for (Player player : allPlayers) {
            if (player.getUsername().equals(username) && player.getPassword().equals(password)) {
                toReturn = player;
            }
        }
        return toReturn;
    }

    @Override
    public List<Integer> searchForPlayersByName(String fullName) {
        return this.playerTree.getAllIdsStartingWith(fullName);
    }
}
