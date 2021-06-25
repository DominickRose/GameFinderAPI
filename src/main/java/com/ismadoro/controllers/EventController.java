package com.ismadoro.controllers;

import com.google.gson.Gson;
import com.ismadoro.entities.Event;
import com.ismadoro.services.EventServices;
import io.javalin.http.Handler;

import java.util.List;


public class EventController {

    private EventServices eventServices;

    public EventController(EventServices eventServices) {
        this.eventServices = eventServices;
    }

    public Handler getAllEvents = ctx -> {
        String title = ctx.queryParam("titlecontains");
        List<Event> events;
        events = this.eventServices.getSeveralEvents();

        Gson gson = new Gson();
        String eventJSON = gson.toJson(events);
        ctx.result(eventJSON);
        ctx.status(200);
    };

    public Handler createEvent = ctx -> {
        Gson gson = new Gson();
        Event event = gson.fromJson(ctx.body(), Event.class);
        this.eventServices.createEvent(event);
        String eventJSON = gson.toJson(event);
        ctx.result(eventJSON);
        ctx.status(201);
    };

    public Handler getEventById = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Event event = this.eventServices.getEventById(id);
        Gson gson = new Gson();
        String eventJSON = gson.toJson(event);
        ctx.result(eventJSON);
        ctx.status(200);
    };

    public Handler deleteEvent = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean result = this.eventServices.deleteEvent(id);
        ctx.result("Successfully deleted resource");
        ctx.status(205);
    };

    public Handler updateEvent = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Gson gson = new Gson();
        Event event = gson.fromJson(ctx.body(), Event.class);
        event.setEventId(id);
        event = eventServices.updateEvent(event);
        String eventJson = gson.toJson(event);
        ctx.result(eventJson);
        ctx.status(200);

    };

}
