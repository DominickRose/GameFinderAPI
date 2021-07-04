package com.ismadoro.daos;

import com.ismadoro.dsa.IndexableHeap;
import com.ismadoro.dsa.RepeatSafeTrieNode;
import com.ismadoro.dsa.RepeatSafeTrieTree;
import com.ismadoro.dsa.TrieTree;
import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidInput;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDaoLocal implements EventDao {

    //    private static Map<Integer, Event> eventTable = new HashMap<>();
    private static IndexableHeap eventTable = new IndexableHeap();
    private static int idMaker = 0;

    @Override
    public Event addEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
        int key = ++EventDaoLocal.idMaker;
        event.setEventId(key);
        eventTable.addEvent(event);
        return event;
    }

    @Override
    public Event getSingleEvent(int eventId) {
        if (eventId < 0)
            throw new InvalidInput("Invalid id for event");
        Event event = eventTable.getEventByID(eventId);
        if (event == null)
            throw new ResourceNotFound("No event with that id exists");
        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < eventTable.getSize(); ++i)
            events.add(eventTable.getEventByIndex(i));
        return events;
    }

    @Override
    public Map<Integer, List<Event>> getEventsByOwner(int ownerId) {
        List<Event> events = new ArrayList<>();
        Map<Integer, List<Event>> ownedEvents = new HashMap<>();

        for (Event event : getAllEvents()) {
            if (event.getOwnerId() == ownerId) {
                events.add(event);
            }
        }
        ownedEvents.put(ownerId, events);
        return ownedEvents;
    }

    @Override
    public Event updateEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
        int eventId = event.getEventId();
        eventTable.updateEventByID(eventId, event);
        return event;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        return eventTable.removeEventByID(eventId);
    }

    @Override
    public List<Event> getEventsBefore(long time) {
        return eventTable.getElementsBefore(time);
    }
}
