package com.ismadoro.controllers;

import com.google.gson.*;
import com.ismadoro.entities.Event;
import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.InvalidJson;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.services.EventServices;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EventController {

    private EventServices eventServices;

    public EventController(EventServices eventServices) {
        this.eventServices = eventServices;
    }

    public Handler createEvent = ctx -> {
        try {
            Gson gson = new Gson();
            Event event = gson.fromJson(ctx.body(), Event.class);
            this.eventServices.createEvent(event);
            String eventJSON = gson.toJson(event);
            ctx.result(eventJSON);
            ctx.status(201);
        } catch (NumberFormatException numberFormatException) {
            ctx.result(numberFormatException.getMessage());
            ctx.status(400);
            throw new NumberFormatException("Invalid value for int");
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        }
    };

    public Handler getEventBySearch = ctx -> {
        try {
            JsonElement jElement = JsonParser.parseString(ctx.body());
            List<Event> events = new ArrayList<>();
            Gson gson = new Gson();

            if (!jElement.isJsonNull()) {
                JsonObject jObject = jElement.getAsJsonObject();

                JsonElement title = jObject.get("eventTitle");
                JsonElement place = jObject.get("eventState");
                JsonElement time = jObject.get("eventTime");
                JsonElement type = jObject.get("eventType");
                JsonElement skill = jObject.get("eventSkill");


                if (!title.isJsonNull()) {
                    String eventTitle = title.getAsString();
                    events = eventServices.getEventsByTitle(eventTitle);
                }
                if (!place.isJsonNull()) {
                    String eventPlace = place.getAsString();
                    events = eventServices.getEventsByPlace(eventPlace);
                }
                if (!time.isJsonNull()) {
                    long eventTime = time.getAsLong();
                    events = eventServices.getEventsByTime(eventTime);
                }
                if (!type.isJsonNull()) {
                    String eventType = type.getAsString();
                }
                if (!skill.isJsonNull()) {
                    String eventSkill = skill.getAsString();
                }


                String eventJSON = gson.toJson(events);
                ctx.result(eventJSON);
                ctx.status(200);
                ctx.contentType("application/json");
            } else {
                String eventJSON = gson.toJson(events);
                ctx.result(eventJSON);
                ctx.status(200);
            }
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result("Resource not found");
            ctx.status(404);
        }
    };

    public Handler getEventsByOwnerId = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            Map<Integer, List<Event>> owned = this.eventServices.getEventsByUser(id);
            List<Event> ownedEvents = owned.get(id);

            Gson gson = new Gson();
            String eventJSON = gson.toJson(ownedEvents);
            ctx.result(eventJSON);
            ctx.status(200);
            ctx.contentType("application/json");

        } catch (NumberFormatException numberFormatException) {
            ctx.result(numberFormatException.getMessage());
            ctx.status(400);
            throw new NumberFormatException("Failed to convert a String into an int");
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
            throw new ResourceNotFound("Did not receive a valid input");
        }
    };

    public Handler getEventById = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            Event event = this.eventServices.getEventById(id);
            if (event == null) {
                ctx.status(404);
                throw new ResourceNotFound("Event could not be found");
            } else {
                Gson gson = new Gson();
                String eventJSON = gson.toJson(event);
                ctx.result(eventJSON);
                ctx.status(200);
            }
        } catch (NumberFormatException numberFormatException) {
            ctx.result(numberFormatException.getMessage());
            ctx.status(400);
            throw new NumberFormatException("Failed to convert a String into an int");
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.message);
            ctx.status(404);
            throw new ResourceNotFound("Did not receive a valid input");
        }
    };

    public Handler deleteEvent = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean result = this.eventServices.deleteEvent(id);
            if (result) {
                ctx.result("Successfully deleted event");
                ctx.status(205);
            } else {
                ctx.status(404);
                throw new ResourceNotFound("Event could not be found");
            }

        } catch (NumberFormatException numberFormatException) {
            ctx.result(numberFormatException.getMessage());
            ctx.status(400);
            throw new NumberFormatException("Failed to convert a String into an int");
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        }
    };

    public Handler updateEvent = ctx -> {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Gson gson = new Gson();
            Event event = gson.fromJson(ctx.body(), Event.class);
            event.setEventId(id);
            event = eventServices.updateEvent(event);
            String eventJson = gson.toJson(event);
            ctx.result(eventJson);
            ctx.status(200);
        } catch (NumberFormatException numberFormatException) {
            ctx.result(numberFormatException.getMessage());
            ctx.status(400);
            throw new NumberFormatException("Failed to convert a String into an int");
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        } catch (ResourceNotFound resourceNotFound) {
            ctx.result(resourceNotFound.getMessage());
            ctx.status(404);
            throw new ResourceNotFound("Did not receive a valid input");
        }
    };

}
