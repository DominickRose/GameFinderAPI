package com.ismadoro.daos;

import com.ismadoro.entities.Event;

import java.util.List;
import java.util.Map;

public interface EventDao {

    String nameTrimmer(int eventId);

    String placeTrimmer(int eventId);

    //Create
    Event addEvent(Event event);

    //Read
    Event getSingleEvent(int eventId);

    List<Event> getAllEvents();

    Map<Integer, List<Event>> getEventsByOwner(int ownerId);

    List<Event> getEventsByTitle(String title);

    List<Event> getEventsByPlace(String place);

    List<Event> getEventsByTime(long time);

    //Update
    Event updateEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);
}
