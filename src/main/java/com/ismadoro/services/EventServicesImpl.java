package com.ismadoro.services;

import com.ismadoro.daos.EventDao;
import com.ismadoro.dsa.RepeatSafeTrieTree;
import com.ismadoro.dsa.TrieTree;
import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidInput;
import com.ismadoro.exceptions.InvalidParamException;
import com.ismadoro.exceptions.ResourceNotFound;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EventServicesImpl implements EventServices {
    private EventDao eventDao = null;
    private TrieTree nameTree = new TrieTree();
    private RepeatSafeTrieTree placeTree = new RepeatSafeTrieTree();

    public EventServicesImpl(EventDao eventDao) {
        this.eventDao = eventDao;
        ArrayList<Event> allEventsInDatabase = (ArrayList<Event>) this.eventDao.getAllEvents();
        for (int i = 0; i < allEventsInDatabase.size(); ++i) {
            int id = allEventsInDatabase.get(i).getEventId();
            nameTree.addWord(nameTrimmer(id), id);
            placeTree.addWord(placeTrimmer(id), id);
        }
    }

    @Override
    public String nameTrimmer(int eventId) {
        String trimmed = "";
        try {
            String untrimmed = getEventById(eventId).getEventTitle();
            trimmed = untrimmed.replace(" ", "");
        } catch (NullPointerException nullPointerException) {
            trimmed = "invalid";
        }
        return trimmed;

    }

    @Override
    public String placeTrimmer(int eventId) {
        String trimmed = "";
        try {
            String untrimmed = getEventById(eventId).getState() + getEventById(eventId).getCity();
            trimmed = untrimmed.replace(" ", "");
        } catch (NullPointerException nullPointerException) {
            trimmed = "invalid";
        }
        return trimmed;
    }

    @Override
    public Event createEvent(Event event) {
        Event addedEvent = eventDao.addEvent(event);
        int key = addedEvent.getEventId();
        nameTree.addWord(nameTrimmer(key), key);
        placeTree.addWord(placeTrimmer(key), key);
        return addedEvent;
    }

    @Override
    public Event getEventById(int eventId) {
        return eventDao.getSingleEvent(eventId);
    }

    @Override
    public List<Event> getEventsBySearch(String title, String place, long time, String type, String skill) {
        //Pass in empty query exit
        if (title == null || place == null || time < 0 || type == null || skill == null)
            throw new InvalidInput("Please fill out all fields");
        if (title.equals("") && place.equals("") && time == 0 && type.equals("") && skill.equals(""))
            return new ArrayList<>();

        ArrayList<ArrayList<Integer>> resultsLists = new ArrayList<ArrayList<Integer>>(2);
        HashSet<Integer> seenIds = null;

        if (!title.equals("")) {
            ArrayList<Integer> nameResults = nameTree.getAllIdsStartingWith(title);
            resultsLists.add(nameResults);
            seenIds = new HashSet<Integer>(nameResults);
        }
        if (!place.equals("")) {
            ArrayList<Integer> placeResults = placeTree.getAllIdsStartingWith(place);
            resultsLists.add(placeResults);
            if (seenIds == null) seenIds = new HashSet<Integer>(placeResults);
        }
        ArrayList<Integer> commonIds = null;
        //NO title and no place
        if (resultsLists.size() == 0) {
            //Do nothing
        }
        //Either title or place
        else if (resultsLists.size() == 1) commonIds = resultsLists.get(0);
            //Both title and place
        else {
            commonIds = new ArrayList<Integer>();
            for (int i = 0; i < resultsLists.get(1).size(); ++i) {
                if (seenIds.contains(resultsLists.get(1).get(i))) {
                    commonIds.add(resultsLists.get(1).get(i));
                }
            }
        }
        //We have the commonIds between the two searches
        //If not common ids search was empty
        ArrayList<Event> allEvents = null;
        ArrayList<Event> events = new ArrayList<>();
        //Empty place and skill so search all events
        if (commonIds == null) {
            //Get all events if no time param
            if(time == 0) allEvents = (ArrayList<Event>) eventDao.getAllEvents();
            //Rely on time as the filter
            else allEvents = (ArrayList<Event>) eventDao.getEventsBefore(time);
        }
        else {
            //Get all commnon events
            allEvents = new ArrayList<>();
            for (int i = 0; i < commonIds.size(); ++i) {
                Event curEvent = eventDao.getSingleEvent(commonIds.get(i));
                if (curEvent != null) {
                    //Filter by time also since result set is so small
                    if(time == 0) allEvents.add(curEvent);
                    else if(curEvent.getEventDate() <= time) allEvents.add(curEvent);
                    //event is outside of the time
                }
            }
        }
        //Final filter by skill and type (Max 50 events at once)
        for (int i = 0; events.size() < 50 && i < allEvents.size(); ++i) {
            //Filter by type
            if (!type.equals("")) {
                //Skip if types dont match
                if (!allEvents.get(i).getEventType().equals(type))
                    continue;
            }
            //Filter by skill
            if (!skill.equals("")) {
                //Skip if skill doesnt match
                if (!allEvents.get(i).getSkillLevel().equals(skill))
                    continue;
            }
            events.add(allEvents.get(i));
        }
        return events;
    }

    @Override
    public Map<Integer, List<Event>> getEventsByUser(int ownerId) {
        return eventDao.getEventsByOwner(ownerId);
    }

    @Override
    public Event updateEvent(Event event) {
        int key = event.getEventId();
        if (getEventById(key) == null)
            throw new InvalidParameterException("This is not a valid event");
        String prevTitle = nameTrimmer(key);
        String prevPlace = placeTrimmer(key);
        Event updatedEvent = eventDao.updateEvent(event);
        String newTitle = nameTrimmer(key);
        String newPlace = placeTrimmer(key);
        nameTree.updateWord(prevTitle, newTitle);
        placeTree.updateWord(prevPlace, key, newPlace);
        return updatedEvent;
    }

    @Override
    public Boolean deleteEvent(int eventId) {
        if (getEventById(eventId) == null)
            throw new InvalidParamException("This is not a valid event");

        String title = nameTrimmer(eventId);
        String place = placeTrimmer(eventId);

        boolean deletedEvent = eventDao.deleteEvent(eventId);
        if (deletedEvent) {
            nameTree.removeWord(title);
            placeTree.removeWord(place, eventId);
        }
        return deletedEvent;
    }
}

