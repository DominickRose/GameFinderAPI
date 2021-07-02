package com.ismadoro.services;

import com.ismadoro.daos.EventDao;
import com.ismadoro.entities.Event;

import java.util.List;
import java.util.Map;

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
        return eventDao.getEventsByTitle(title);
    }

    @Override
    public List<Event> getEventsByPlace(String place) {
        return eventDao.getEventsByPlace(place);
    }

    @Override
    public List<Event> getEventsByTime(long time) {
        return eventDao.getEventsByTime(time);
    }

    @Override
    public Map<Integer, List<Event>> getEventsByUser(int ownerId) {
        return eventDao.getEventsByOwner(ownerId);
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
