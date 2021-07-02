package com.ismadoro.services;

import com.ismadoro.daos.PlayerDao;
import com.ismadoro.dsa.RepeatSafeTrieTree;
import com.ismadoro.entities.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerServiceImpl implements PlayerService{

    private PlayerDao playerDao;
    private RepeatSafeTrieTree playerTree;
    private Map<String, String> playerUsernamePasswordMap;
    private Map<String, Integer> playerUsernameIdMap;

    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
        this.playerTree = new RepeatSafeTrieTree();
        this.playerUsernamePasswordMap = new HashMap<>();
        this.playerUsernameIdMap = new HashMap<>();

        List<Player> allPlayersInDatabase = this.playerDao.getAllPlayers();
        for (Player player : allPlayersInDatabase) {
            this.playerTree.addWord(player.getFullName(), player.getPlayerId());
            this.playerUsernamePasswordMap.put(player.getUsername(), player.getPassword());
            this.playerUsernameIdMap.put(player.getUsername(), player.getPlayerId());
        }
    }

    @Override
    public Player addPlayer(Player player) {
        Player addedPlayer = this.playerDao.addPlayer(player);
        this.playerTree.addWord(addedPlayer.getFullName(), addedPlayer.getPlayerId());
        this.playerUsernameIdMap.put(addedPlayer.getUsername(), addedPlayer.getPlayerId());
        this.playerUsernamePasswordMap.put(addedPlayer.getUsername(), addedPlayer.getPassword());
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
        Player previousPlayer = getSinglePlayer(player.getPlayerId());
        Player updatedPlayer = this.playerDao.updatePlayer(player);

        this.playerTree.updateWord(previousPlayer.getFullName(), player.getPlayerId(), updatedPlayer.getFullName());

        this.playerUsernameIdMap.remove(previousPlayer.getUsername());
        this.playerUsernameIdMap.put(updatedPlayer.getUsername(), updatedPlayer.getPlayerId());

        this.playerUsernamePasswordMap.remove(previousPlayer.getUsername());
        this.playerUsernamePasswordMap.put(updatedPlayer.getUsername(), updatedPlayer.getPassword());

        return updatedPlayer;
    }

    @Override
    public boolean deletePlayer(int playerId) {
        Player previousPlayer = getSinglePlayer(playerId);
        boolean result = this.playerDao.deletePlayer(playerId);

        this.playerTree.removeWord(previousPlayer.getFullName(), playerId);
        this.playerUsernameIdMap.remove(previousPlayer.getUsername());
        this.playerUsernamePasswordMap.remove(previousPlayer.getUsername());

        return result;
    }

    @Override
    public Player validateLogin(String username, String password) {
        String truePassword = this.playerUsernamePasswordMap.get(username);
        if (truePassword != null && truePassword.equals(password)) {
            return this.playerDao.getSinglePlayer(this.playerUsernameIdMap.get(username));
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> searchForPlayersByName(String fullName) {
        return this.playerTree.getAllIdsStartingWith(fullName);
    }
}
