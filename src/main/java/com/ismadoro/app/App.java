package com.ismadoro.app;

import com.ismadoro.controllers.EventController;
import com.ismadoro.daos.EventDao;
import com.ismadoro.daos.EventDaoLocal;
import com.ismadoro.daos.EventDaoPostgres;
import com.ismadoro.services.EventServices;
import com.ismadoro.services.EventServicesImpl;
import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {
        Javalin app = Javalin.create();
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

        app.start();

    }
}
