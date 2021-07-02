package com.ismadoro.services;

import com.ismadoro.entities.Event;

import java.util.List;
import java.util.Map;

public interface EventServices {

    Event createEvent(Event event);

    Event getEventById(int eventId);

    List<Event> getSeveralEvents();

    List<Event> getEventsByTitle(String title);

    List<Event> getEventsByPlace(String place);

    List<Event> getEventsByTime(long time);

    Map<Integer, List<Event>> getEventsByUser(int ownerId);

    Event updateEvent(Event event);

    Boolean deleteEvent(int eventId);

}
