package com.ismadoro.daos;

import com.ismadoro.entities.Registration;

import java.util.List;

public interface RegistrationDao {
    //Create
    Registration addPlayer(Registration player);

    //Read
    Registration getSinglePlayer(int playerId);
    List<Registration> getAllPlayers();

    //Update
    Registration updatePlayer(Registration player);

    //Delete
    boolean deletePlayer(int id);
}
