package com.ismadoro.services;

import com.ismadoro.entities.Event;

import java.util.List;
import java.util.Map;

public interface EventServices {

    String nameTrimmer(int eventId);

    String placeTrimmer(int eventId);

    Event createEvent(Event event);

    Event getEventById(int eventId);

    List<Event> getEventsBySearch (String title, String place, long time, String type, String skill);

    Map<Integer, List<Event>> getEventsByUser(int ownerId);

    Event updateEvent(Event event);

    Boolean deleteEvent(int eventId);

}
