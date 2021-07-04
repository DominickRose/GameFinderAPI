package com.ismadoro.integration;

import com.ismadoro.daos.EventDao;
import com.ismadoro.daos.EventDaoLocal;
import com.ismadoro.daos.EventDaoPostgres;
import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidInput;
import com.ismadoro.services.EventServices;
import com.ismadoro.services.EventServicesImpl;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class EventServiceIntegrationTests {

    //static EventDao eventDao = null;
    static EventDao eventDao = new EventDaoPostgres();
    //static EventDao eventDao = new EventDaoLocal();
    EventServices eventServices = new EventServicesImpl(eventDao);

    ArrayList<Integer> idList = new ArrayList<>();

//    @BeforeClass
//    @Parameters({"database"})
//    void setupClass(String database) {
//        if (database.equals("postgres")) eventDao = new EventDaoPostgres();
//        else eventDao = new EventDaoLocal();
//    }

    @Test(priority = 1)
    void testCreateEventPass() {
        Event event = new Event(0, 0, 1, "Chicago", "IL", "Lalalala", "easy", "Fun Tastic", "2-vs-2", 3);
        Event addEvent = eventServices.createEvent(event);
        Assert.assertEquals(addEvent.getEventTitle(), "Fun Tastic");
        idList.add(addEvent.getEventId());
    }

    @Test(priority = 1, dependsOnMethods = {"testCreateEventPass"})
    void testGetEventPass() {
        Event event = eventServices.getEventById(idList.get(0));
        Assert.assertEquals(event.getEventTitle(), "Fun Tastic");
    }

    @Test(priority = 1, dependsOnMethods = {"testCreateEventPass"})
    void testGetEventFail() {
        try {
            eventServices.getEventById(-1);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 1, dependsOnMethods = {"testCreateEventPass", "testGetEventPass"})
    void testUpdateEventPass() {
        Event event = eventServices.getEventById(idList.get(0));
        event.setMaxPlayers(4);
        eventServices.updateEvent(event);
        Event upEvent = eventServices.getEventById(idList.get(0));
        Assert.assertEquals(upEvent.getMaxPlayers(), 4);
    }

    @Test(priority = 1, dependsOnMethods = {"testCreateEventPass", "testGetEventPass"})
    void testUpdateEventFail() {
        try {
            Event event = eventServices.getEventById(-1);
            event.setMaxPlayers(4);
            eventServices.updateEvent(event);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateEventPass"})
    void testDeleteEventPass() {
        boolean deleted = eventServices.deleteEvent(idList.get(0));
        idList.remove(0);
        Assert.assertTrue(deleted);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateEventPass"})
    void testDeleteEventFail() {
        try {
            eventServices.deleteEvent(-1);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchFullFail() {
        try {
            eventServices.getEventsBySearch(null, null, -1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchOnlyTitleFail() {
        try {
            eventServices.getEventsBySearch("loud", null, -1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchOnlyPlaceFail() {
        try {
            eventServices.getEventsBySearch(null, "n", -1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchOnlyTimeFail() {
        try {
            eventServices.getEventsBySearch(null, null, 1, null, null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchOnlyTypeFail() {
        try {
            eventServices.getEventsBySearch(null, null, -1, "2-vs-2", null);
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 3)
    void testSearchOnlySkillFail() {
        try {
            eventServices.getEventsBySearch(null, null, -1, null, "intermediate");
        } catch (InvalidInput invalidInput) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 4)
    void testSearchEmptyPass() {
        Event event1 = new Event(0, 1, 2, "Washington", "DC", "", "intermediate", "Fresh Times", "2-vs-2", 5);
        Event event2 = new Event(0, 2, 3, "New York", "NY", "", "easy", "Loud Times", "4-vs-4", 10);
        Event event3 = new Event(0, 3, 4, "Charlotte", "NC", "", "easy", "Louder Times", "2-vs-2", 15);

        eventServices.createEvent(event1);
        eventServices.createEvent(event2);
        eventServices.createEvent(event3);
        idList.add(event1.getEventId());
        idList.add(event2.getEventId());
        idList.add(event3.getEventId());

        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 0);
    }

    @Test(priority = 5, dependsOnMethods = {"testSearchEmptyPass"})
    void testSearchOnlyTitlePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("loud", "", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 5, dependsOnMethods = {"testSearchEmptyPass"})
    void testSearchOnlyPlacePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "n", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 5)
    void testSearchOnlyTypePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 0, "4-vs-4", "");
        Assert.assertEquals(eventSearch.size(), 1);
    }


    @Test(priority = 5)
    void testSearchOnlyTimePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 2, "", "");
        Assert.assertEquals(eventSearch.size(), 1);
    }


    @Test(priority = 5)
    void testSearchOnlySkillPass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("", "", 0, "", "easy");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 6)
    void testSearchTitleAndPlacePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("f", "d", 0, "", "");
        Assert.assertEquals(eventSearch.size(), 1);
    }

    @Test(priority = 6)
    void testSearchTitleAndTimePass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("l", "", 4, "", "");
        Assert.assertEquals(eventSearch.size(), 2);
    }

    @Test(priority = 6)
    void testSearchFullPass() {
        List<Event> eventSearch = eventServices.getEventsBySearch("l", "n", 3, "4-vs-4", "easy");
        Assert.assertEquals(eventSearch.size(), 1);
    }


    @AfterClass
    void tearDown(){
        for(int i = 0; i < idList.size(); ++i){
            eventServices.deleteEvent(idList.get(i));
        }
    }
}
