package com.ismadoro.app;

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

        PlayerDao playerDao = new PlayerDaoLocal();
        PlayerService playerService = new PlayerServiceImpl(playerDao);
        PlayerController playerController = new PlayerController(playerService);

        //Create a new player
        app.post("/players", playerController.addNewPlayer);

        //Get all players
        app.get("/players", playerController.getAllPlayers);

        //Get single player
        app.get("/players/:id", playerController.getSinglePlayer);

        //Delete single player
        app.delete("/players/:id", playerController.deleteSinglePlayer);

        //Update a player
        app.put("/players/:id", playerController.updatePlayer);

        //Login
        app.post("/players/login", playerController.login);

        app.start();
    }
}
