package com.ismadoro.daos;

import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.List;

public interface PlayerDao {
    //Create
    Player addPlayer(Player player);

    //Read
    Player getSinglePlayer(int playerId) throws ResourceNotFound;
    List<Player> getAllPlayers();

    //Update
    Player updatePlayer(Player player);

    //Delete
    boolean deletePlayer(int id) throws ResourceNotFound;
}
