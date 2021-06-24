package com.ismadoro.services;

import com.ismadoro.entities.Player;

import java.util.List;

public interface PlayerService {
    //Create
    public Player addPlayer(Player player);

    //Read
    public Player getSinglePlayer(int playerId);
    public List<Player> getAllPlayers();

    //Update
    public Player updatePlayer(Player player);

    //Delete
    public boolean deletePlayer(int playerId);

    //Other
    public Player validateLogin(String username, String password);
}
