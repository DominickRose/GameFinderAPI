package com.ismadoro.daos;

import com.ismadoro.entities.Event;

import java.util.List;

public interface EventDao {

    String nameTrimmer(int eventId);

    //Create
    Event addEvent(Event event);

    //Read
    Event getSingleEvent(int eventId);
    List<Event> getAllEvents();

    List<Event> getEventsByTitle(String title);

    //Update
    Event updateEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);
}
