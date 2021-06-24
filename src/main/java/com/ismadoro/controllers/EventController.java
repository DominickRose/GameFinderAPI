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

    public Handler createBook = ctx -> {
        Gson gson = new Gson();
        Event event = gson.fromJson(ctx.body(), Event.class);
        this.eventServices.createEvent(event);
        String eventJSON = gson.toJson(event);
        ctx.result(eventJSON);
        ctx.status(201);
    };

    public Handler getBookById = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Event event = this.eventServices.getEventById(id);
        Gson gson = new Gson();
        String eventJSON = gson.toJson(event);
        ctx.result(eventJSON);
        ctx.status(200);
    };

}
