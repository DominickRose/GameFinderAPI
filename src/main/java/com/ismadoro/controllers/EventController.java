package com.ismadoro.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidJson;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.services.EventServices;
import io.javalin.http.Handler;

import java.util.List;


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
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
        }
    };

    public Handler getAllEvents = ctx -> {
        try {
            String title = ctx.queryParam("titlecontains");
            List<Event> events = this.eventServices.getSeveralEvents();
            if(title != null){
                events = this.eventServices.getSeveralEvents();
            }else{
                events = this.eventServices.getEventsByTitle(title);
            }
            Gson gson = new Gson();
            String eventJSON = gson.toJson(events);
            ctx.result(eventJSON);
            ctx.status(200);
            ctx.contentType("application/json");
        } catch (JsonSyntaxException jsonSyntaxException) {
            ctx.result(jsonSyntaxException.getMessage());
            ctx.status(400);
            throw new InvalidJson("Received malformed JSON");
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
