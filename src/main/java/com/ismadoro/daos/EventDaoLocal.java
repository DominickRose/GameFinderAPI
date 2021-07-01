package com.ismadoro.daos;

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

    private static Map<Integer, Event> eventTable = new HashMap<>();
    private static int idMaker = 0;
    private TrieTree nameTree = new TrieTree();
    private RepeatSafeTrieTree placeTree = new RepeatSafeTrieTree();

    @Override
    public String nameTrimmer(int eventId) {
        String untrimmed = getSingleEvent(eventId).getEventTitle();
        String trimmed = untrimmed.replace(" ", "");
        return trimmed;
    }

    @Override
    public String placeTrimmer(int eventId) {
        String untrimmed = getSingleEvent(eventId).getState() + getSingleEvent(eventId).getCity();
        String trimmed = untrimmed.replace(" ", "");
        return trimmed;
    }

    @Override
    public Event addEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
        int key = ++EventDaoLocal.idMaker;
        event.setEventId(key);
        eventTable.put(key, event);
        nameTree.addWord(nameTrimmer(key), key);
        placeTree.addWord(placeTrimmer(key), key);
        return event;
    }

    @Override
    public Event getSingleEvent(int eventId) {
        if (eventId < 0)
            throw new InvalidInput("Invalid id for event");
        if (!eventTable.containsKey(eventId))
            throw new ResourceNotFound("No event with that id exists");
        return eventTable.get(eventId);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>(eventTable.values());
        return events;
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        List<Event> eventList = new ArrayList<>();
        List<Integer> idList;

        if (title != null) {
            idList = nameTree.getAllIdsStartingWith(title);
            for (int id : idList) {
                eventList.add(getSingleEvent(id));
            }
        }

        return eventList;
    }

    @Override
    public List<Event> getEventsByPlace(String place) {
        List<Event> eventList = new ArrayList<>();
        List<Integer> idList;

        if (place != null) {
            idList = placeTree.getAllIdsStartingWith(place);
            for (int id : idList) {
                eventList.add(getSingleEvent(id));
            }
        }

        return eventList;
    }

    @Override
    public List<Event> getEventsByTime(long time) {
        return null;
    }

    @Override
    public Event updateEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
        int eventId = event.getEventId();
        String prevTitle = nameTrimmer(event.getEventId());
        String prevPlace = placeTrimmer(event.getEventId());
        eventTable.put(event.getEventId(), event);
        String newTitle = nameTrimmer(event.getEventId());
        String newPlace = placeTrimmer(event.getEventId());
        nameTree.updateWord(prevTitle, newTitle);
        placeTree.updateWord(prevPlace, eventId, newPlace);
        return event;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        String title = nameTrimmer(eventId);
        String place = placeTrimmer(eventId);
        Event result = eventTable.remove(eventId);
        if (result == null) {
            return false;
        } else {
            nameTree.removeWord(title);
            placeTree.removeWord(place, eventId);
            return true;
        }
    }
}
