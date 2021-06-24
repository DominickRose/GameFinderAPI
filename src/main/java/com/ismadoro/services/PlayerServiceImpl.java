package com.ismadoro.services;

import com.ismadoro.daos.PlayerDao;
import com.ismadoro.entities.Player;

import java.util.List;

public class PlayerServiceImpl implements PlayerService{

    private PlayerDao playerDao;

    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public Player addPlayer(Player player) {
        return this.playerDao.addPlayer(player);
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
        return this.playerDao.updatePlayer(player);
    }

    @Override
    public boolean deletePlayer(int playerId) {
        return this.playerDao.deletePlayer(playerId);
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
}
