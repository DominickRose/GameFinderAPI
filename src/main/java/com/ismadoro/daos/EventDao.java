package com.ismadoro.daos;

import com.ismadoro.entities.Event;

import java.util.List;

public interface EventDao {
    //Create
    Event addEvent(Event event);

    //Read
    Event getSingleEvent(int eventId);
    List<Event> getAllEvents();

    //Update
    Event updateEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);
}
