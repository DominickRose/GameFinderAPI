package com.ismadoro.services;

import com.ismadoro.daos.EventDao;
import com.ismadoro.entities.Event;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class EventServiceTests {
    @Mock
    final EventDao mockEventDao = Mockito.mock(EventDao.class);
    EventServices eventServices = null;

    @BeforeMethod
    void EventDaoSetup() {
        Event event1 = new Event(0, 0, 222, "Washington", "DC", "", "intermediate", "Louder Times", "2-vs-2", 5);
        Event event2 = new Event(0, 0, 333, "New York", "NY", "", "easy", "Loud Times", "4-vs-4", 10);
        Event event3 = new Event(0, 0, 444, "Charlotte", "NC", "", "easy", "Fresh Times", "2-vs-2", 15);
        List<Event> mockList = new ArrayList<>();
        mockList.add(event1);
        mockList.add(event2);
        mockList.add(event3);

        Mockito.when(mockEventDao.getAllEvents()).thenReturn(mockList);
        Mockito.when(mockEventDao.getSingleEvent(1)).thenReturn(event1);
        Mockito.when(mockEventDao.getSingleEvent(2)).thenReturn(event2);
        Mockito.when(mockEventDao.getSingleEvent(3)).thenReturn(event3);

        //need to find some way to move trie searches so I can get rid of these
        List<Event> e = new ArrayList<>();
        List<Event> e12 = new ArrayList<>();
        List<Event> e23 = new ArrayList<>();
        e12.add(event1);
        e12.add(event2);
        e23.add(event2);
        e23.add(event3);

        Mockito.when(mockEventDao.getEventsByTitle("loud")).thenReturn(e12);
        Mockito.when(mockEventDao.getEventsByPlace("n")).thenReturn(e23);
        Mockito.when(mockEventDao.getEventsByTitle("b")).thenReturn(e);
        Mockito.when(mockEventDao.getEventsByPlace("j")).thenReturn(e);
        eventServices = new EventServicesImpl(mockEventDao);
    }

    @Test(priority = 1)
    void testSearchByTitlePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("loud", null, 0, null, null);
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 2)
    void testSearchByPlacePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch(null, "n", 0, null, null);
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 3)
    void testSearchByTitleAndPlacePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("loud", "n", 0, null, null);
        Assert.assertEquals(eventSearch.size(), 1);
        Assert.assertEquals(eventSearch.get(0).getEventTitle(), "Loud Times");
    }
    @Test(priority = 4)
    void testSearchByTitleNone() {
        List<Event> eventSearch = eventServices.getEventsBySearch("b", null, 0, null, null);
        Assert.assertEquals(eventSearch.size(), 0);
    }
    @Test(priority = 5)
    void testSearchByPlaceNone() {
        List<Event> eventSearch = eventServices.getEventsBySearch(null, "j", 0, null, null);
        Assert.assertEquals(eventSearch.size(), 0);
    }
    @Test(priority = 6)
    void testSearchByTitleAndPlaceNone() {
        List<Event> eventSearch = eventServices.getEventsBySearch("b", "j", 0, null, null);
        Assert.assertEquals(eventSearch.size(), 0);
    }

}
