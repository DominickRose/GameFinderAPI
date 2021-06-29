package com.ismadoro.services;

import com.ismadoro.daos.EventDao;
import com.ismadoro.entities.Event;

import java.util.ArrayList;
import java.util.List;

public class EventServicesImpl implements EventServices {
    private EventDao eventDao = null;

    public EventServicesImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event createEvent(Event event) {
        return eventDao.addEvent(event);
    }

    @Override
    public Event getEventById(int eventId) {
        return eventDao.getSingleEvent(eventId);
    }

    @Override
    public List<Event> getSeveralEvents() {
        return eventDao.getAllEvents();
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        List<Event> events = this.eventDao.getAllEvents();
        List<Event> filteredEvents = new ArrayList<>();

        for (Event e : events) {
            if (e.getEventTitle().contains(title)) {
                filteredEvents.add(e);
            }
        }
        return filteredEvents;
    }

    @Override
    public Event updateEvent(Event event) {
        return eventDao.updateEvent(event);
    }

    @Override
    public Boolean deleteEvent(int eventId) {
        return eventDao.deleteEvent(eventId);
    }
}
