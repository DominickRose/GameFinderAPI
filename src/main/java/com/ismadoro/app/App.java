package com.ismadoro.app;


import com.ismadoro.controllers.EventController;
import com.ismadoro.daos.EventDao;
import com.ismadoro.daos.EventDaoLocal;
import com.ismadoro.daos.EventDaoPostgres;
import com.ismadoro.services.EventServices;
import com.ismadoro.services.EventServicesImpl;

import com.ismadoro.controllers.PlayerController;
import com.ismadoro.daos.PlayerDao;
import com.ismadoro.daos.PlayerDaoLocal;
import com.ismadoro.services.PlayerService;
import com.ismadoro.services.PlayerServiceImpl;

import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.enableDevLogging();
        });
        EventDao eventDao = new EventDaoPostgres();
        EventServices eventServices = new EventServicesImpl(eventDao);
        EventController eventController = new EventController(eventServices);

        //get /events
        app.get("/events", eventController.getAllEvents);

        //get /events/5
        app.get("/events/:id", eventController.getEventById);

        //post /event
        app.post("/events", eventController.createEvent);

        //put /event
        app.put("/events/:id", eventController.updateEvent);

        //delete /event
        app.delete("/events/:id", eventController.deleteEvent);


        PlayerDao playerDao = new PlayerDaoLocal();
        PlayerService playerService = new PlayerServiceImpl(playerDao);
        PlayerController playerController = new PlayerController(playerService);

        //Create a new player
        //Return 201 and JSON representation of added object on success
        app.post("/players", playerController.addNewPlayer);

        //Get all players
        //Return 200 and JSON array of objects on success
        app.get("/players", playerController.getAllPlayers);

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

        app.start();

    }
}
