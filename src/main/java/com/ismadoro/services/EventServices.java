package com.ismadoro.services;

import com.ismadoro.entities.Event;

import java.util.List;

public interface EventServices {

    Event createEvent(Event event);

    Event getEventById(int eventId);

    List<Event> getSeveralEvents();

    List<Event> getEventsByTitle(String title);

    Event updateEvent(Event event);

    Boolean deleteEvent(int eventId);

}
