package com.ismadoro.services;

import com.ismadoro.daos.EventDao;
import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidInput;
import com.ismadoro.exceptions.InvalidParamException;
import org.checkerframework.checker.units.qual.A;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class EventServiceTests {
    @Mock
    final EventDao mockEventDao = Mockito.mock(EventDao.class);
    EventServices eventServices = null;
    Event event1 = new Event(0, 1, 222, "Washington", "DC", "", "intermediate", "Fresh Times", "2-vs-2", 5);
    Event event2 = new Event(0, 2, 333, "New York", "NY", "", "easy", "Loud Times", "4-vs-4", 10);
    Event event3 = new Event(0, 3, 444, "Charlotte", "NC", "", "easy", "Louder Times", "2-vs-2", 15);
    Event event1Update = new Event(0, 1, 222, "Washington", "DC", "", "intermediate", "Dank Times", "2-vs-2", 5);
    List<Event> mockList = new ArrayList<>();

    @BeforeClass
    void EventDaoSetup() {
        //read
        Mockito.when(mockEventDao.getAllEvents()).thenReturn(mockList);
        Mockito.when(mockEventDao.getSingleEvent(1)).thenReturn(event1);
        Mockito.when(mockEventDao.getSingleEvent(2)).thenReturn(event2);
        Mockito.when(mockEventDao.getSingleEvent(3)).thenReturn(event3);

        //create
        Mockito.when(mockEventDao.addEvent(event1)).thenReturn(event1);
        Mockito.when(mockEventDao.addEvent(event2)).thenReturn(event2);
        Mockito.when(mockEventDao.addEvent(event3)).thenReturn(event3);

        //update
        Mockito.when(mockEventDao.updateEvent(event1)).thenReturn(event1Update);

        //Delete
        Mockito.when(mockEventDao.deleteEvent(1)).thenReturn(true);
        Mockito.when(mockEventDao.deleteEvent(2)).thenReturn(true);
        Mockito.when(mockEventDao.deleteEvent(3)).thenReturn(true);
        Mockito.when(mockEventDao.deleteEvent(4)).thenReturn(false);

        ArrayList<Event> whenList = new ArrayList<Event>();
        whenList.add(event1);
        Mockito.when(mockEventDao.getEventsBefore(222)).thenReturn(whenList);

        eventServices = new EventServicesImpl(mockEventDao);
    }

    // Crud tests

    @Test(priority = 1)
    void testAddEventPass() {
        Event addEvent = eventServices.createEvent(event1);
        Assert.assertEquals(addEvent.getEventTitle(), "Fresh Times");
    }

    @Test(priority = 1, dependsOnMethods = {"testAddEventPass"})
    void testUpdateEventPass() {
        Event updateEvent = eventServices.updateEvent(event1);
        Assert.assertEquals(updateEvent.getEventTitle(), "Dank Times");
    }

    @Test(priority = 2)
    void testDeleteEventPass() {
        boolean deleteEvent = eventServices.deleteEvent(1);
        Assert.assertTrue(deleteEvent);
    }

    @Test(priority = 2, dependsOnMethods = {"testDeleteEventPass"})
    void testDeleteEventFail() {
        try {
            eventServices.deleteEvent(5);
            Assert.assertFalse(true);
        } catch (InvalidParamException invalidParamException) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2)
    void testSearchFullFail() {
        try {
            List<Event> eventSearch = eventServices.getEventsBySearch(null, null, -1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2)
    void testSearchOnlyTitleFail() {
        try {
            List<Event> eventSearch = eventServices.getEventsBySearch("loud", null, -1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2)
    void testSearchOnlyPlaceFail() {
        try {
            List<Event> eventSearch = eventServices.getEventsBySearch(null, "n", -1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2)
    void testSearchOnlyTimeFail() {
        try {
            List<Event> eventSearch = eventServices.getEventsBySearch(null, null, 1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2)
    void testSearchOnlyTypeFail() {
        try {
            List<Event> eventSearch = eventServices.getEventsBySearch(null, null, -1, "2-vs-2", null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2)
    void testSearchOnlySkillFail() {
        try {
            List<Event> eventSearch = eventServices.getEventsBySearch(null, null, -1, null, "intermediate");
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchEmptyPass() {
        eventServices.createEvent(event1);
        eventServices.createEvent(event2);
        eventServices.createEvent(event3);
        mockList.add(event1);
        mockList.add(event2);
        mockList.add(event3);
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 0);
    }

    @Test(priority = 4, dependsOnMethods = {"testSearchEmptyPass"})
    void testSearchOnlyTitlePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("loud", "", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 4, dependsOnMethods = {"testSearchEmptyPass"})
    void testSearchOnlyPlacePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "n", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 4)
    void testSearchOnlyTypePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 0, "4-vs-4", "");
        Assert.assertEquals(eventSearch.size(), 1);
    }


    @Test(priority = 4)
    void testSearchOnlyTimePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 222, "", "");
        Assert.assertEquals(eventSearch.size(), 1);
    }


    @Test(priority = 4)
    void testSearchOnlySkillPass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 0, "", "easy");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 5)
    void testSearchTitleAndPlacePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("f", "d", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 1);
    }

    @Test(priority = 5)
    void testSearchTitleAndTimePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("l", "", 444, "", "");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 5)
    void testSearchFullPass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("l", "n", 333, "4-vs-4", "easy");
        Assert.assertEquals(eventSearch.size(), 1);
    }

}
