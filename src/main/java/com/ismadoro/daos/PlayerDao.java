package com.ismadoro.daos;

import com.ismadoro.entities.Player;

import java.util.List;

public interface PlayerDao {
    //Create
    Player addPlayer(Player player);

    //Read
    Player getSinglePlayer(int playerId);
    List<Player> getAllPlayers();

    //Update
    Player updatePlayer(Player player);

    //Delete
    boolean deletePlayer(int id);
}
