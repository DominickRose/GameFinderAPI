package com.ismadoro.entities;

public class Event {
    private int ownerId;
    private int eventId;
    private float eventDate;
    private String city;
    private String state;
    private String description;
    private String skillLevel;
    private String eventTitle;
    private String eventType;
    private int maxPlayers;

    public Event() {
    }

    public Event(int ownerId, int eventId, float eventDate, String city, String state, String description, String skillLevel, String eventTitle, String eventType, int maxPlayers) {
        this.ownerId = ownerId;
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.city = city;
        this.state = state;
        this.description = description;
        this.skillLevel = skillLevel;
        this.eventTitle = eventTitle;
        this.eventType = eventType;
        this.maxPlayers = maxPlayers;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public float getEventDate() {
        return eventDate;
    }

    public void setEventDate(float eventDate) {
        this.eventDate = eventDate;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "ownerId=" + ownerId +
                ", eventId=" + eventId +
                ", eventDate=" + eventDate +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventType='" + eventType + '\'' +
                ", maxPlayers=" + maxPlayers +
                '}';
    }
}
