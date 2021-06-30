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

    //Takes a username and password and returns a Player object from the database who matches those credentials
    //Returns null if no player with the given credentials exists
    public Player validateLogin(String username, String password);

    public List<Integer> searchForPlayersByName(String fullName);
}
