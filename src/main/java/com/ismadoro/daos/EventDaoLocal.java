package com.ismadoro.daos;

import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidInput;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDaoLocal implements EventDao {

    private static Map<Integer, Event> eventTable = new HashMap<>();
    private static int idMaker = 0;

    @Override
    public Event addEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
        int key = ++EventDaoLocal.idMaker;
        event.setEventId(key);
        EventDaoLocal.eventTable.put(key, event);
        return event;
    }

    @Override
    public Event getSingleEvent(int eventId) {
        if (eventId < 0)
            throw new InvalidInput("Invalid id for event");
        if (!eventTable.containsKey(eventId))
            throw new ResourceNotFound("No event with that id exists");
        return EventDaoLocal.eventTable.get(eventId);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>(EventDaoLocal.eventTable.values());
        return events;
    }

    @Override
    public Event updateEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
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
