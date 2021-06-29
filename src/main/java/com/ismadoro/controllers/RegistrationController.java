package com.ismadoro.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ismadoro.entities.Event;
import com.ismadoro.entities.Player;
import com.ismadoro.entities.Registration;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.services.EventServices;
import com.ismadoro.services.PlayerService;
import com.ismadoro.services.RegistrationService;

import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;

public class RegistrationController {

    private PlayerService playerService;
    private EventServices eventServices;
    private RegistrationService registrationService;

    //Once event services have been added we can add those to the constructor on this
    public RegistrationController(RegistrationService registrationService, PlayerService playerService, EventServices eventService) {
        this.playerService = playerService;
        this.registrationService = registrationService;
        this.eventServices = eventService;
    }

    public Handler addRegistration = ctx -> {
        try {
            Gson gson = new Gson();
            Registration newRegistration = gson.fromJson(ctx.body(), Registration.class);
            Registration addedRegistration = this.registrationService.addRegistration(newRegistration);
            String registrationJson = gson.toJson(addedRegistration);
            ctx.result(registrationJson);
            ctx.status(201);
        } catch (JsonSyntaxException | NullPointerException e) {
            ctx.result("Invalid JSON Body");
            ctx.status(400);
        }
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
        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            ctx.result("Invalid request");
            ctx.status(400);
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
        } catch (NumberFormatException e) {
            ctx.result("Invalid ID in path param");
            ctx.status(400);
        }
    };


    public Handler getAllPlayersWithConditions = ctx -> {
        try {
            String event = ctx.queryParam("eventId");

            if (event != null) {
                List<Player> allPlayers = new ArrayList<>();
                List<Integer> allPlayerIds = registrationService.getAllPlayersForEvent(Integer.parseInt(event));
                for (Integer playerId : allPlayerIds) {
                    allPlayers.add(playerService.getSinglePlayer(playerId));
                }
                Gson gson = new Gson();
                String allPlayersJson = gson.toJson(allPlayers);

                ctx.result(allPlayersJson);
                ctx.status(200);
            } else {
                List<Player> allPlayers = playerService.getAllPlayers();
                Gson gson = new Gson();
                String allPlayersJson = gson.toJson(allPlayers);
                ctx.result(allPlayersJson);
                ctx.status(200);
            }
        } catch (NumberFormatException e) {
            ctx.result("Invalid search parameter, must be integer");
            ctx.status(400);
        }
    };

    public Handler getAllEventsWithConditions = ctx -> {
        try {
            String player = ctx.queryParam("playerId");
            String title = ctx.queryParam("titlecontains");

            if (title != null) { //If the user passes in the titlename query parameter
                List<Event> events = this.eventServices.getEventsByTitle(title);
                Gson gson = new Gson();
                String eventJSON = gson.toJson(events);
                ctx.result(eventJSON);
                ctx.status(200);
                ctx.contentType("application/json");
            } else if (player != null) { //If the user passes in the playerId query parameter
                List<Event> allEvents = new ArrayList<>();
                List<Integer> allEventIds = registrationService.getAllEventsForPlayer(Integer.parseInt(player));
                for (Integer eventId : allEventIds) {
                    allEvents.add(eventServices.getEventById(eventId));
                }
                Gson gson = new Gson();
                String allPlayersJson = gson.toJson(allEvents);

                ctx.result(allPlayersJson);
                ctx.status(200);
            } else {
                List<Event> events = this.eventServices.getSeveralEvents();
                Gson gson = new Gson();
                String eventJSON = gson.toJson(events);
                ctx.result(eventJSON);
                ctx.status(200);
            }
        } catch (NumberFormatException e) {
            ctx.result("Invalid search parameter, must be integer");
            ctx.status(400);
        }    };
}
