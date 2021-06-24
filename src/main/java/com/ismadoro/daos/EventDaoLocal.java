package com.ismadoro.daos;

import com.ismadoro.entities.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDaoLocal implements EventDao {

    private static Map<Integer, Event> eventTable = new HashMap<>();
    private static int idMaker = 0;

    @Override
    public Event addEvent(Event event) {
        int key = ++EventDaoLocal.idMaker;
        event.setEventId(key);
        EventDaoLocal.eventTable.put(key, event);
        return event;
    }

    @Override
    public Event getSingleEvent(int eventId) {
        return EventDaoLocal.eventTable.get(eventId);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>(EventDaoLocal.eventTable.values());
        return events;
    }

    @Override
    public Event updateEvent(Event event) {
        EventDaoLocal.eventTable.put(event.getEventId(), event);
        return event;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        Event event = EventDaoLocal.eventTable.remove(eventId);
        if (event == null) {
            return false;
        } else {
            return true;
        }
    }
}
