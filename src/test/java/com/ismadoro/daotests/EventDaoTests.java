package com.ismadoro.daotests;

import com.ismadoro.daos.EventDao;
import com.ismadoro.daos.EventDaoLocal;
import com.ismadoro.entities.Event;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class EventDaoTests {

    static EventDao eventDao = new EventDaoLocal();
    static Event testEvent = new Event(0, 0, "April 1", "Chicago", "Illinois", "", "easy", "Fun Times", "game", 3);

    @Test(priority = 1)
    void addEvent() {
        Event event = eventDao.addEvent(testEvent);
        Assert.assertNotEquals(event.getEventId(), 0);
    }

    @Test(priority = 2)
    void getEventById() {
        Event event = eventDao.getSingleEvent(testEvent.getEventId());
        Assert.assertEquals(event.getEventTitle(), "Fun Times");
    }

    @Test(priority = 3)
    void updateEvent() {
        testEvent.setType("War");
        eventDao.updateEvent(testEvent);
        Event event = eventDao.getSingleEvent(testEvent.getEventId());
        Assert.assertNotEquals(event.getEventTitle(), "Game");
    }

    @Test(priority = 4)
    void deleteEvent() {
        boolean result = eventDao.deleteEvent(testEvent.getEventId());
        Assert.assertTrue(result);
    }

    @Test(priority = 5, dependsOnMethods = "addEvent")
    void getAllEvents() {
        Event event1 = new Event(0, 0, "April 1", "Washington", "District of Colombia", "", "easy", "Hard Times", "game", 5);
        Event event2 = new Event(0, 0, "April 1", "New York", "New York State", "", "easy", "Loud Times", "game", 10);
        Event event3 = new Event(0, 0, "April 1", "Atlanta", "Georgia", "", "easy", "Fresh Times", "game", 15);
        eventDao.addEvent(event1);
        eventDao.addEvent(event2);
        eventDao.addEvent(event3);
        List<Event> events = eventDao.getAllEvents();
        Assert.assertTrue(events.size() >= 3);
        eventDao.deleteEvent(event1.getEventId());
        eventDao.deleteEvent(event2.getEventId());
        eventDao.deleteEvent(event3.getEventId());
    }
}
