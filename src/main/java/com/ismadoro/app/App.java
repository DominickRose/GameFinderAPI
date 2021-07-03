package com.ismadoro.app;


import com.ismadoro.controllers.EventController;

import com.ismadoro.daos.*;
import com.ismadoro.controllers.PlayerController;
import com.ismadoro.controllers.RegistrationController;

import com.ismadoro.services.PlayerService;
import com.ismadoro.services.PlayerServiceImpl;
import com.ismadoro.services.RegistrationService;
import com.ismadoro.services.RegistrationServiceImpl;
import com.ismadoro.services.EventServices;
import com.ismadoro.services.EventServicesImpl;

import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableCorsForAllOrigins();
        });

        EventDao eventDao = new EventDaoPostgres();
        EventServices eventServices = new EventServicesImpl(eventDao);
        EventController eventController = new EventController(eventServices);

        PlayerDao playerDao = new PlayerDaoPostgres();
        PlayerService playerService = new PlayerServiceImpl(playerDao);
        PlayerController playerController = new PlayerController(playerService);

        RegistrationDao registrationDao = new RegistrationDaoPostgres();
        RegistrationService registrationService = new RegistrationServiceImpl(registrationDao);
        RegistrationController registrationController = new RegistrationController(registrationService, playerService, eventServices);

        //get /events
        //Use the query parameter playerId= to search for all events a single player is registered for
        //Use the query parameter titlecontains= to filter for events by title
        app.get("/events", registrationController.getAllEventsWithConditions);

        //get /events/5
        app.get("/events/:id", eventController.getEventById);

        //get /events/user/1
        app.get("/events/user/:id", eventController.getEventsByOwnerId);

        //post /events
        app.post("/events", eventController.createEvent);

        //post /events/search
        app.post("/events/search", eventController.getEventBySearch);

        //put /events
        app.put("/events/:id", eventController.updateEvent);

        //delete /events
        app.delete("/events/:id", eventController.deleteEvent);


        //Create a new player
        //Return 201 and JSON representation of added object on success
        app.post("/players", playerController.addNewPlayer);

        //Get all players
        //Return 200 and JSON array of objects on success
        //Use the query parameter eventId= to get all players registered for the given event
        //Use the query parameter name= to get all players whose names start as given
        app.get("/players", registrationController.getAllPlayersWithConditions);

        //Get single player
        //Return 200 and JSON representation of object on success
        //Return 404 for invalid ID
        app.get("/players/:id", playerController.getSinglePlayer);

        //Delete single player
        //Return 205 for successful delete
        //Return 404 for invalid ID
        app.delete("/players/:id", playerController.deleteSinglePlayer);

        //Update a player
        //Return 200 and JSON representation of object on success
        //Return 404 for invalid ID
        app.put("/players/:id", playerController.updatePlayer);

        //Login
        //Return 200 and JSON representation of associated Player object on success
        //Return 400 for request body that lacks "username" and "password"
        //Return 422 for invalid credentials
        app.post("/players/login", playerController.login);


        //Create a new registration
        //Return 201 and JSON representation of added registration
        app.post("/registrations", registrationController.addRegistration);

        //Get all registrations
        //Return 200 and JSON array of all registrations
        app.get("/registrations", registrationController.getAllRegistrations);

        //Get a single registration
        //Return 200 and JSON representation of the retrieved object
        //Return 404 for invalid ID
        app.get("/registrations/:id", registrationController.getSingleRegistration);

        //Check if a player is registered for a specific event
        //Return 200 and a JSON indicating whether or not the player is registered
        //Return 400 if path parameters are invalid
        app.get("/registrations/:playerId/:eventId", registrationController.isPlayerRegisteredForEvent);

        //Update the registration with the given ID
        //Return 200 and JSON for updated object on successful update
        //Return 404 if the specified object does not exist
        app.put("/registrations/:id", registrationController.updateRegistration);

        //Delete the registration with the given ID
        //Return 205 on successful delete
        //Return 404 if the ID is invalid
        app.delete("/registrations/:id", registrationController.deleteRegistration);

        //Delete the registration associated with the given Player and Event IDs
        //Return 205 on successful delete
        //Return 404 if no player matches those contenst
        //Return 400 if path parameters are non numeric
        app.delete("/registrations/:playerId/:eventId", registrationController.deleteRegistrationByContents);


        app.start();

    }
}
