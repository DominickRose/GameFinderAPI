package com.ismadoro.dsa;
import java.util.*;

import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.ResourceNotFound;

//BUG - DO NOT EDIT Event times directly it will break this delicate structure
//Always update by creating new events then place them inside
public class IndexableHeap {
    private final Map<Integer, Integer> EventIdToIndex;
    private ArrayList<Event> events;
    //[0-10]
    //[0 1 4 2 3 7 9]
    //     0
    //   /   \
    //  1     4
    // / \   / \
    //2   3 7   9
    public IndexableHeap() {
        events = null;
        EventIdToIndex = new HashMap<>();
    }
    public IndexableHeap(ArrayList<Event> newEventList) {
        EventIdToIndex = new HashMap<>();
        setEventsList(newEventList);
    }
    //2-1/2 -> parent is 0
    //3-1/2 -> parent is 1
    public int getParent(int curIndex) { return (curIndex-1)/2; }
    //0*2+1 -> left child is 1
    //1*2+1 -> left child is 3
    public int getLeftChild(int curIndex) { return (curIndex * 2)+1; }
    //0*2+2 -> right child is 2
    //1*2+2 -> right child is 4
    public int getRightChild(int curIndex) { return (curIndex * 2)+2; }
    public int getSize() {
        if (events == null) return 0;
        return events.size();
    }
    private void siftDown(int curIndex) {
        if(events == null) return;
        while(curIndex < events.size()) {
            Event leftChild = null;
            Event rightChild = null;
            int leftIndex = getLeftChild(curIndex);
            int rightIndex = getRightChild(curIndex);
            if(leftIndex < events.size()) leftChild = events.get(leftIndex);
            if(rightIndex < events.size()) rightChild = events.get(rightIndex);

            //Get smaller child
            //Both children exist
            Event smallerChild = null;
            int smallerIndex = -1;
            if(leftChild != null && rightChild != null) {
                if(leftChild.getEventDate() <= rightChild.getEventDate()) {
                    smallerChild = leftChild;
                    smallerIndex = leftIndex;
                }
                else {
                    smallerChild = rightChild;
                    smallerIndex = rightIndex;
                }
            }
            //Left Child exists
            else if(leftChild != null) {
                smallerChild = leftChild;
                smallerIndex = leftIndex;
            }
            //No children exist
            else break;

            Event curEvent = events.get(curIndex);
            if(curEvent.getEventDate() > smallerChild.getEventDate()) {
                //Update map + eventList
                Collections.swap(events, curIndex, smallerIndex);
                EventIdToIndex.replace(smallerChild.getEventId(), curIndex);
                EventIdToIndex.replace(curEvent.getEventId(), smallerIndex);
                //Continue to traverse down the 'tree'
                curIndex = smallerIndex;
            }
            //Exit if current is smaller than the smaller child; its in the right position
            else break;
        }
    }
    private void siftUp(int curIndex) {
        if(events == null) return;
        while(curIndex >= 0) {
            Event curEvent = events.get(curIndex);
            int parentIndex = getParent(curIndex);
            //Exit if no parent exists
            if(parentIndex < 0) break;
            Event parentEvent = events.get(parentIndex);
            if(parentEvent.getEventDate() > curEvent.getEventDate()) {
                //Update map + eventList
                Collections.swap(events, curIndex, parentIndex);
                EventIdToIndex.replace(curEvent.getEventId(), parentIndex);
                EventIdToIndex.replace(parentEvent.getEventId(), curIndex);
                //Continue to traverse up the 'tree'
                curIndex = parentIndex;
            }
            else break;
        }
    }

    public void debugPrint(int endPos) {
        for(int i = 0; i < endPos; ++i) {
            if(i >= events.size()) break;
            System.out.println(events.get(i).getEventDate());
        }
    }
    public void setEventsList(ArrayList<Event> eventList) {
        EventIdToIndex.clear();
        events = eventList;
        //Setup our map
        for(int i = 0; i < events.size(); ++i) {
            EventIdToIndex.put(events.get(i).getEventId(), i);
        }
        //Turn our list of events into a heap
        //Siftdown on each parent for faster partial-sorting
        for(int i = events.size() - 1; i > 0; i-=2) {
            siftDown(getParent(i));
        }
    }
    public Event top() {
        if(events == null || events.isEmpty()) return null;
        return events.get(0);
    }
    public Event pop() {
        if(events == null || events.isEmpty()) return null;
        //Swap first element with the last for efficient array removal
        int lastIndex = events.size() - 1;
        Collections.swap(events, 0, lastIndex);
        EventIdToIndex.replace(events.get(0).getEventId(), 0);
        EventIdToIndex.remove(events.get(lastIndex).getEventId());
        Event topElement = events.remove(lastIndex);
        //Reposition the first element in the 'tree' (previously last element)
        siftDown(0);
        return topElement;
    }
    public Event getEventByID(int eventId) {
        Integer eventIndex = EventIdToIndex.get(eventId);
        if(eventIndex == null) return null;
        return events.get(eventIndex);
    }
    public Event getEventByIndex(int index) {
        if(index < 0 || index >= events.size())
            return null;

        return events.get(index);
    }
    public void addEvent(Event newEvent) {
        if(events == null) events = new ArrayList<>();
        if(EventIdToIndex.containsKey(newEvent.getEventId()))
            throw new DuplicateResourceException("This key already exists");

        events.add(newEvent);
        int lastIndex = events.size() - 1;
        EventIdToIndex.put(newEvent.getEventId(), lastIndex);
        //Place newly added event in the proper place in the tree
        siftUp(lastIndex);
    }
    public boolean removeEventByID(int eventId) {
        Integer indexToRemove = EventIdToIndex.get(eventId);
        if(indexToRemove == null)
            throw new ResourceNotFound("Event with the given id was not found");
        //Swap this element with the last for efficient array removal
        int lastIndex = events.size() - 1;
        Collections.swap(events, indexToRemove, lastIndex);
        EventIdToIndex.replace(events.get(indexToRemove).getEventId(), indexToRemove);
        EventIdToIndex.remove(events.get(lastIndex).getEventId());
        events.remove(lastIndex);
        //Reposition this element in the 'tree' (previously last element)
        siftDown(indexToRemove);
        return true;
    }
    public boolean updateEventByID(int eventId, Event updatedEvent) {
        Event prevEvent = getEventByID(eventId);
        if(prevEvent == null)
            throw new ResourceNotFound("Event with the given id was not found");
        //If no change in time do nothing
        long prevTime = prevEvent.getEventDate();
        long newTime = updatedEvent.getEventDate();
        int eventIndex = EventIdToIndex.get(eventId);
        events.set(eventIndex, updatedEvent);
        if(prevTime == newTime) return true;
        //Otherwise update its position in the 'tree' accordingly
        else if(prevTime < newTime) siftDown(eventIndex);
        else siftUp(eventIndex);
        return true;
    }
    public ArrayList<Event> getElementsBefore(long time) {
        return getElementsBefore(time, Integer.MAX_VALUE);
    }
    public ArrayList<Event> getElementsBefore(long time, int amount) {
        //Iterate through our 'tree' in BFS fashion to check
        //requirements in semi-sorted order
        ArrayList<Event> results = new ArrayList<>();
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(0);
        while(!queue.isEmpty() && results.size() < amount) {
            int curIndex = queue.getFirst();
            Event curEvent = events.get(curIndex);
            queue.removeFirst();

            //Skip if this event time is greater
            if(curEvent.getEventDate() > time)
                continue;

            results.add(curEvent);
            //Add children to the queue
            int leftIndex = getLeftChild(curIndex);
            int rightIndex = getRightChild(curIndex);
            if(leftIndex < events.size()) queue.addLast(leftIndex);
            if(rightIndex < events.size()) queue.addLast(rightIndex);
        }
        return results;
    }
}
