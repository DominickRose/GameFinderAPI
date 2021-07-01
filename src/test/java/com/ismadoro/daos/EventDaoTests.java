package com.ismadoro.daos;

import com.ismadoro.daos.EventDao;
import com.ismadoro.daos.EventDaoLocal;
import com.ismadoro.daos.EventDaoPostgres;
import com.ismadoro.entities.Event;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

public class EventDaoTests {

    static EventDao eventDao = new EventDaoLocal();
//        static EventDao eventDao = new EventDaoPostgres();
    static Event testEvent = new Event(0, 0, 111, "Chicago", "Illinois", "Lalalala", "easy", "Fun Times", "game", 3);

    @AfterMethod
    void GetTestEventCopy() {
        testEvent.setEventDate(111);
        testEvent.setCity("Chicago");
        testEvent.setState("Illinois");
        testEvent.setDescription("Lalalala");
        testEvent.setSkillLevel("easy");
        testEvent.setEventTitle("Fun Times");
        testEvent.setEventType("game");
        testEvent.setMaxPlayers(3);
    }


    @Test(priority = 1)
    void addEventPass() {
        Event event = eventDao.addEvent(testEvent);
        Assert.assertNotEquals(event.getEventId(), 0);
    }

    @Test(priority = 2)
    void addEventNoEventDateFail() {
        try {
            testEvent.setEventDate(0);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 3)
    void addEventNoCityFail() {
        try {
            testEvent.setCity(null);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 4)
    void addEventNoStateFail() {
        try {
            testEvent.setState(null);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 5)
    void addEventNoDescriptionFail() {
        try {
            testEvent.setDescription(null);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 6)
    void addEventNoSkillLevelFail() {
        try {
            testEvent.setSkillLevel(null);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 7)
    void addEventNoEventTitleFail() {
        try {
            testEvent.setEventTitle(null);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 8)
    void addEventNoEventTypeFail() {
        try {
            testEvent.setEventType(null);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 9)
    void addEventNoMaxPlayersFail() {
        try {
            testEvent.setMaxPlayers(0);
            Event event = eventDao.addEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 10)
    void getEventByIdPass() {
        Event event = eventDao.getSingleEvent(testEvent.getEventId());
        Assert.assertEquals(event.getEventTitle(), "Fun Times");
    }


    @Test(priority = 11)
    void getEventByIdFail() {
        try {
            Event event = eventDao.getSingleEvent(-1);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 12)
    void updateEventPass() {
        testEvent.setEventType("War");
        Event event = eventDao.updateEvent(testEvent);
        Assert.assertNotEquals(event.getEventTitle(), "Game");
    }


    @Test(priority = 13)
    void updateEventNoEventDateFail() {
        try {
            testEvent.setEventDate(0);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 14)
    void updateEventNoCityFail() {
        try {
            testEvent.setCity(null);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 15)
    void updateEventNoStateFail() {
        try {
            testEvent.setState(null);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 16)
    void updateEventNoDescriptionFail() {
        try {
            testEvent.setDescription(null);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 17)
    void updateEventNoSkillLevelFail() {
        try {
            testEvent.setSkillLevel(null);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 18)
    void updateEventNoEventTitleFail() {
        try {
            testEvent.setEventTitle(null);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 19)
    void updateEventNoEventTypeFail() {
        try {
            testEvent.setEventType(null);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 20)
    void updateEventNoMaxPlayersFail() {
        try {
            testEvent.setMaxPlayers(0);
            Event event = eventDao.updateEvent(testEvent);
            Assert.assertNull(event);
        } catch (RuntimeException e) {
        }
    }

    @Test(priority = 21)
    void deleteEventPass() {
        Event delEvent = new Event(0, 0, 222, "Washington", "District of Colombia", "", "easy", "Hard Times", "game", 5);
        Event event = eventDao.addEvent(delEvent);
        boolean result = eventDao.deleteEvent(event.getEventId());
        Assert.assertTrue(result);
    }

    @Test(priority = 22, dependsOnMethods = "addEventPass")
    void getAllEventsPass() {
        Event event1 = new Event(0, 0, 222, "Washington", "District of Colombia", "", "easy", "Hard Times", "game", 5);
        Event event2 = new Event(0, 0, 333, "New York", "New York State", "", "easy", "Loud Times", "game", 10);
        Event event3 = new Event(0, 0, 444, "Atlanta", "Georgia", "", "easy", "Fresh Times", "game", 15);
        eventDao.addEvent(event1);
        eventDao.addEvent(event2);
        eventDao.addEvent(event3);
        List<Event> events = eventDao.getAllEvents();
        Assert.assertTrue(events.size() >= 3);
        eventDao.deleteEvent(event1.getEventId());
        eventDao.deleteEvent(event2.getEventId());
        eventDao.deleteEvent(event3.getEventId());
    }

    @Test(priority = 23, dependsOnMethods = "addEventPass")
    void getEventsByTitlePass() {
        Event event1 = new Event(0, 0, 222, "Washington", "District of Colombia", "", "easy", "hardtimes", "game", 5);
        Event event2 = new Event(0, 0, 333, "New York", "New York State", "", "easy", "loudtimes", "game", 10);
        Event event3 = new Event(0, 0, 444, "Atlanta", "Georgia", "", "easy", "freshtimes", "game", 15);
        eventDao.addEvent(event1);
        eventDao.addEvent(event2);
        eventDao.addEvent(event3);
        List<Event> events = eventDao.getEventsByTitle("loud");
        Assert.assertEquals(events.size(), 1);
        Assert.assertEquals(events.get(0).getEventTitle(), "loudtimes");
        eventDao.deleteEvent(event1.getEventId());
        eventDao.deleteEvent(event2.getEventId());
        eventDao.deleteEvent(event3.getEventId());
        }
}

