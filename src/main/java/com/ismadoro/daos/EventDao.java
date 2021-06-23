package com.ismadoro.daos;

import com.ismadoro.entities.Event;

import java.util.List;

public interface EventDao {
    //Create
    Event addPlayer(Event player);

    //Read
    Event getSinglePlayer(int playerId);
    List<Event> getAllPlayers();

    //Update
    Event updatePlayer(Event player);

    //Delete
    boolean deletePlayer(int id);
}
