package com.ismadoro.daos;

import com.ismadoro.entities.Event;

import java.util.List;
import java.util.Map;

public interface EventDao {

    //Create
    Event addEvent(Event event);

    //Read
    Event getSingleEvent(int eventId);

    List<Event> getAllEvents();

    Map<Integer, List<Event>> getEventsByOwner(int ownerId);

    //Update
    Event updateEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);

    List<Event> getEventsBefore(long time);
}
