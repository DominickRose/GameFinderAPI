package com.ismadoro.dsa;

import com.ismadoro.dsa.IndexableHeap;
import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.ResourceNotFound;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class IndexableHeapTests {
    IndexableHeap idxHeap = new IndexableHeap();

    private boolean verifyHeap() {
        if(idxHeap.getSize() == 0) return true;
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(0);
        while(!queue.isEmpty()) {
            int curIndex = queue.getFirst();
            Event curEvent = idxHeap.getEventByIndex(curIndex);
            queue.removeFirst();

            int leftChildIdx = idxHeap.getLeftChild(curIndex);
            int rightChildIdx = idxHeap.getRightChild(curIndex);
            //Confirm leftChild is greater than curEvent
            if(leftChildIdx < idxHeap.getSize()) {
                Event leftChildEvent = idxHeap.getEventByIndex(leftChildIdx);
                if(leftChildEvent.getEventDate() < curEvent.getEventDate())
                    return false;
                queue.addLast(leftChildIdx);
            }
            //Confirm rightChild is greater than curEvent
            if(rightChildIdx < idxHeap.getSize()) {
                Event rightChildEvent = idxHeap.getEventByIndex(rightChildIdx);
                if(rightChildEvent.getEventDate() < curEvent.getEventDate())
                    return false;
                queue.addLast(rightChildIdx);
            }
        }
        return true;
    }
    private boolean verifyIndexMap() {
        for(int i = 0; i < idxHeap.getSize(); ++i) {
            Event curEvent = idxHeap.getEventByIndex(i);
            if(idxHeap.getEventByID(curEvent.getEventId()) != curEvent)
                return false;
        }
        return true;
    }

    //Basic Heap Functionality
    @Test(priority = 1)
    void testCreateBasicHeap() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(0, 1, 4, "", "", "", "", "", "", 0));
        events.add(new Event(0, 2, 2, "", "", "", "", "", "", 0));
        events.add(new Event(0, 3, 1, "", "", "", "", "", "", 0));
        events.add(new Event(0, 4, 5, "", "", "", "", "", "", 0));
        idxHeap.setEventsList(events);
        Assert.assertTrue(verifyHeap());
    }

    @Test(priority = 1, dependsOnMethods = { "testCreateBasicHeap" })
    void testTopElement() {
        Event top = idxHeap.top();
        Assert.assertEquals(top.getEventDate(), 1);
    }

    @Test(priority = 1, dependsOnMethods = { "testCreateBasicHeap", "testTopElement" })
    void testPopElement() {
        int sortedIndex = 0;
        int[] sortedTimes = {1,2,4,5};
        while(idxHeap.getSize() > 0) {
            Event top = idxHeap.pop();
            Assert.assertEquals(top.getEventDate(), sortedTimes[sortedIndex]);
            ++sortedIndex;
        }
    }

    @Test(priority = 2, dependsOnMethods = { "testCreateBasicHeap"})
    void testAddEvent() {
        idxHeap.addEvent(new Event(0, 1, 50, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
        idxHeap.addEvent(new Event(0, 2, 41, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
        idxHeap.addEvent(new Event(0, 3, 14, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
        idxHeap.addEvent(new Event(0, 4, 42, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
    }

    @Test(priority = 2, dependsOnMethods = {"testAddEvent"})
    void testAddEventFail() {
        try {
            idxHeap.addEvent(new Event(0, 4, 42, "", "", "", "", "", "", 0));
        } catch (DuplicateResourceException duplicate) {
            //Success
        }
    }

    @Test(priority = 2, dependsOnMethods = { "testCreateBasicHeap", "testAddEvent"})
    void testGetElementsBeforeTimeAll() {
        ArrayList<Event> resultEvents = idxHeap.getElementsBefore(41);
        for(int i = 0; i < resultEvents.size(); ++i) {
            Assert.assertTrue(resultEvents.get(i).getEventDate() <= 41);
        }
    }

    @Test(priority = 2, dependsOnMethods = { "testCreateBasicHeap", "testAddEvent"})
    void testGetElementsBeforeTimeSome() {
        ArrayList<Event> resultEvents = idxHeap.getElementsBefore(100, 2);
        Assert.assertTrue(resultEvents.size() == 2);
    }

    //Indexing Functionality
    @Test(priority = 3, dependsOnMethods = { "testCreateBasicHeap" } )
    void testCreateIndexHeap() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(0, 1, 4, "", "", "", "", "", "", 0));
        events.add(new Event(0, 2, 2, "", "", "", "", "", "", 0));
        events.add(new Event(0, 3, 1, "", "", "", "", "", "", 0));
        events.add(new Event(0, 4, 5, "", "", "", "", "", "", 0));
        idxHeap.setEventsList(events);
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
    }

    @Test(priority = 3, dependsOnMethods = { "testCreateIndexHeap", "testPopElement" })
    void testIndexPopElement() {
        while(idxHeap.getSize() > 0) {
            Event top = idxHeap.pop();
            Assert.assertTrue(verifyIndexMap());
        }
    }

    @Test(priority = 4, dependsOnMethods = { "testCreateIndexHeap"})
    void testIndexAddEvent() {
        idxHeap.addEvent(new Event(0, 5, 500, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyIndexMap());
        idxHeap.addEvent(new Event(0, 6, 50, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyIndexMap());
        idxHeap.addEvent(new Event(0, 7, 1000, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyIndexMap());
        idxHeap.addEvent(new Event(0, 8, 200, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyIndexMap());
        idxHeap.addEvent(new Event(0, 9, 400, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyIndexMap());
    }

    @Test(priority = 4, dependsOnMethods = { "testCreateIndexHeap", "testIndexAddEvent"})
    void testUpdateEventByIdFail() {
        try {
            Event toUpdate = new Event(0, 111, 400, "", "", "", "", "", "", 0);
            idxHeap.updateEventByID(111, toUpdate);
        } catch(ResourceNotFound res) {
            //Success
        }
    }

    @Test(priority = 4, dependsOnMethods = { "testCreateIndexHeap", "testIndexAddEvent"})
    void testUpdateEventIncTimeById() {
        idxHeap.updateEventByID(9,  new Event(0, 9, 1400, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
    }

    @Test(priority = 4, dependsOnMethods = { "testCreateIndexHeap", "testIndexAddEvent"})
    void testUpdateEventSameTimeById() {
        idxHeap.updateEventByID(9,  new Event(0, 9, idxHeap.getEventByID(9).getEventDate(), "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
    }

    @Test(priority = 4, dependsOnMethods = { "testCreateIndexHeap", "testIndexAddEvent"})
    void testUpdateEventDecTimeById() {
        idxHeap.updateEventByID(9,  new Event(0, 9, 0, "", "", "", "", "", "", 0));
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
    }

    @Test(priority = 4)
    void testDeleteEventByIdFail() {
        try {
            idxHeap.removeEventByID(111);
        } catch(ResourceNotFound res) {
            //Success
        }
    }

    @Test(priority = 5, dependsOnMethods = { "testCreateIndexHeap", "testIndexAddEvent"})
    void testDeleteEventById() {
        idxHeap.removeEventByID(5);
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
        idxHeap.removeEventByID(6);
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
        idxHeap.removeEventByID(7);
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
        idxHeap.removeEventByID(8);
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
        idxHeap.removeEventByID(9);
        Assert.assertTrue(verifyHeap());
        Assert.assertTrue(verifyIndexMap());
    }
}
