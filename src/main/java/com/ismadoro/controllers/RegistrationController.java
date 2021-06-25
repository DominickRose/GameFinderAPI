package com.ismadoro.controllers;

import com.google.gson.Gson;
import com.ismadoro.entities.Registration;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.services.PlayerService;
import com.ismadoro.services.RegistrationService;

import io.javalin.http.Handler;
import java.util.List;

public class RegistrationController {

    private PlayerService playerService;
    private RegistrationService registrationService;

    //Once event services have been added we can add those to the constructor on this
    public RegistrationController(RegistrationService registrationService, PlayerService playerService) {
        this.playerService = playerService;
        this.registrationService = registrationService;
    }

    public Handler addRegistration = ctx -> {
        Gson gson = new Gson();
        Registration newRegistration = gson.fromJson(ctx.body(), Registration.class);
        Registration addedRegistration = this.registrationService.addRegistration(newRegistration);
        String registrationJson = gson.toJson(addedRegistration);
        ctx.result(registrationJson);
        ctx.status(201);
    };

    public Handler getAllRegistrations = ctx -> {
        List<Registration> allRegistrations = registrationService.getAllRegistrations();
        Gson gson = new Gson();
        String allRegistrationsJson = gson.toJson(allRegistrations);
        ctx.result(allRegistrationsJson);
        ctx.status(200);
    };

    public Handler getSingleRegistration = ctx -> {
        try {
            Registration registration = registrationService.getSingleRegistration(Integer.parseInt(ctx.pathParam("id")));
            Gson gson = new Gson();
            String registrationJson = gson.toJson(registration);
            ctx.result(registrationJson);
            ctx.status(200);
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
        }
    };

    public Handler updateRegistration = ctx -> {
        try {
            Gson gson = new Gson();
            Registration newRegistration = gson.fromJson(ctx.body(), Registration.class);
            newRegistration.setRegistrationId(Integer.parseInt(ctx.pathParam("id")));
            Registration registration = registrationService.updateRegistration(newRegistration);
            String registrationJson = gson.toJson(registration);
            ctx.result(registrationJson);
            ctx.status(200);
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
        }
    };

    public Handler deleteRegistration = ctx -> {
        try {
            boolean result = registrationService.deleteRegistration(Integer.parseInt(ctx.pathParam("id")));
            ctx.result("Successfully Deleted Registration");
            ctx.status(205);
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
        }
    };


    public Handler getAllPlayersWithConditions = ctx -> {
        //Intentionally left blank for right now.  Once Events are ready, I'll add a find by event feature using a query parameter
    };

    public Handler getAllEventsWithConditions = ctx -> {
        //Intentionally left blank for right now.  Once Events are ready, I'll add a find by player feature using a query parameter
    };
}
