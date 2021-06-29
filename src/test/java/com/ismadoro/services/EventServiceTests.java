package com.ismadoro.services;

import com.ismadoro.daos.EventDao;
import com.ismadoro.entities.Event;
import org.junit.Assert;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class EventServiceTests {
    static EventDao eventDao = Mockito.mock(EventDao.class);
    EventServices eventServices = new EventServicesImpl(eventDao);

    @BeforeMethod
    void init() {
        List<Event> testEvents = new ArrayList<>();
        Event event1 = new Event(0, 0, 222, "Washington", "District of Colombia", "", "easy", "Hard Times", "game", 5);
        Event event2 = new Event(0, 0, 333, "New York", "New York State", "", "easy", "Loud Times", "game", 10);
        Event event3 = new Event(0, 0, 444, "Atlanta", "Georgia", "", "easy", "Fresh Times", "game", 15);
        testEvents.add(event1);
        testEvents.add(event2);
        testEvents.add(event3);
        Mockito.when(this.eventDao.getAllEvents()).thenReturn(testEvents);
    }

    @Test
    void getEventsByTitle() {
        List<Event> events = this.eventServices.getEventsByTitle("Hard");
        Assert.assertEquals(events.size(), 1);
        Assert.assertEquals(events.get(0).getEventTitle(), "Hard Times");
    }

}
