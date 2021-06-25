package com.ismadoro.app;

import com.ismadoro.controllers.PlayerController;
import com.ismadoro.controllers.RegistrationController;
import com.ismadoro.daos.PlayerDao;
import com.ismadoro.daos.PlayerDaoLocal;
import com.ismadoro.daos.RegistrationDao;
import com.ismadoro.daos.RegistrationDaoLocal;
import com.ismadoro.services.PlayerService;
import com.ismadoro.services.PlayerServiceImpl;
import com.ismadoro.services.RegistrationService;
import com.ismadoro.services.RegistrationServiceImpl;
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

        RegistrationDao registrationDao = new RegistrationDaoLocal();
        RegistrationService registrationService = new RegistrationServiceImpl(registrationDao);
        RegistrationController registrationController = new RegistrationController(registrationService, playerService);

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



        //Create a new registration
        //Return 201 and JSON representation of added registration
        app.post("/registrations", registrationController.addRegistration);

        //Get all registrations
        //Return 200 and JSON array of all registrations
        app.get("/registrations", registrationController.getAllRegistrations);

        //Get a single registration
        //Return 200 and JSON representation of the retrieved object
        //Return 404 for invalid ID
        app.get("/registration/:id", registrationController.getSingleRegistration);

        //Update the registration with the given ID
        //Return 200 and JSON for updated object on successful update
        //Return 404 if the specified object does not exist
        app.put("/registration/:id", registrationController.updateRegistration);

        //Delete the registration with the given ID
        //Return 205 on successful delete
        //Return 404 if the ID is invalid
        app.delete("/registration/:id", registrationController.deleteRegistration);


        app.start();
    }
}
