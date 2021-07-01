package com.ismadoro.daos;

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
    private TrieTree trieTree = new TrieTree();

    @Override
    public String nameTrimmer(int eventId) {
        String untrimmed = getSingleEvent(eventId).getEventTitle();
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
        trieTree.addWord(nameTrimmer(key), key);
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
            idList = trieTree.getAllIdsStartingWith(title);
            for (int id : idList) {
                eventList.add(getSingleEvent(id));
            }
        }

        return eventList;
    }

    @Override
    public Event updateEvent(Event event) {
        if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0)
            throw new InvalidInput("Event_date and max_events must be greater than 0");
        if (event.getCity() == null || event.getState() == null || event.getDescription() == null || event.getSkillLevel() == null || event.getEventTitle() == null || event.getEventType() == null)
            throw new InvalidInput("One or more Event fields were invalid or null");
        String prevWord = nameTrimmer(event.getEventId());
        eventTable.put(event.getEventId(), event);
        String newWord = nameTrimmer(event.getEventId());
        trieTree.updateWord(prevWord, newWord);
        return event;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        String word = nameTrimmer(eventId);
        Event result = eventTable.remove(eventId);
        if (result == null) {
            return false;
        } else {
            trieTree.removeWord(word);
            return true;
        }
    }
}
