package com.ismadoro.controllers;

import com.google.gson.*;
import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.services.PlayerService;
import io.javalin.http.Handler;

import java.util.List;

public class PlayerController {

    private PlayerService playerService;
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    public Handler addNewPlayer = ctx -> {
        try {
            Gson gson = new Gson();
            Player player = gson.fromJson(ctx.body(), Player.class);
            player = this.playerService.addPlayer(player);
            String playerJson = gson.toJson(player);
            ctx.result(playerJson);
            ctx.status(201);
        } catch (DuplicateResourceException duplicateResourceException) {
            ctx.result(duplicateResourceException.message);
            ctx.status(422);
        } catch (JsonSyntaxException | NullPointerException e) {
            ctx.result("Invalid JSON Body");
            ctx.status(400);
        }
    };

    public Handler getAllPlayers = ctx -> {
        List<Player> allPlayers = playerService.getAllPlayers();
        Gson gson = new Gson();
        String allPlayersJson = gson.toJson(allPlayers);
        ctx.result(allPlayersJson);
        ctx.status(200);
    };

    public Handler getSinglePlayer = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Player player = playerService.getSinglePlayer(id);
            Gson gson = new Gson();
            String playerJson = gson.toJson(player);
            ctx.result(playerJson);
            ctx.status(200);
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
        } catch (NumberFormatException e) {
            ctx.result("Invalid ID in path param");
            ctx.status(400);
        }
    };

    public Handler deleteSinglePlayer = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            boolean result = playerService.deletePlayer(id);
            ctx.result("Successfully deleted resource");
            ctx.status(205);
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
        } catch (NumberFormatException e) {
            ctx.result("Invalid ID in path param");
            ctx.status(400);
        }
    };

    public Handler updatePlayer = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Gson gson = new Gson();
            Player player = gson.fromJson(ctx.body(), Player.class);
            player.setPlayerId(id);
            player = playerService.updatePlayer(player);
            String playerJson = gson.toJson(player);
            ctx.result(playerJson);
            ctx.status(200);
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
        } catch (DuplicateResourceException duplicateResourceException) {
            ctx.result(duplicateResourceException.message);
            ctx.status(422);
        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            ctx.result("Invalid request");
            ctx.status(400);
        }
    };

    public Handler login = ctx -> {
        JsonElement jelement = JsonParser.parseString(ctx.body());
        if (!jelement.isJsonNull()) {
            JsonObject jsonObject = jelement.getAsJsonObject();
            JsonElement username = jsonObject.get("username");
            JsonElement password = jsonObject.get("password");
            if (username == null || password == null) {
                ctx.result("Invalid JSON body.  Must contain username and password");
                ctx.status(400);
            } else {
                Player player = playerService.validateLogin(username.getAsString(), password.getAsString());
                if (player == null) {
                    ctx.result("No user matches those login credentials");
                    ctx.status(422);
                } else {
                    Gson gson = new Gson();
                    String playerJson = gson.toJson(player);
                    ctx.result(playerJson);
                    ctx.status(200);
                }
            }
        }
        else {
            ctx.result("Invalid JSON body");
            ctx.status(400);
        }
    };
}
