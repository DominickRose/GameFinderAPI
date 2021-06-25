package com.ismadoro.entities;

public class Event {
    private int ownerId;
    private int eventId;
    private String date;
    private String city;
    private String state;
    private String description;
    private String skillLevel;
    private String eventTitle;
    private String type;
    private int maxPlayers;

    public Event() {
    }

    public Event(int ownerId, int eventId, String date, String city, String state, String description, String skillLevel, String eventTitle, String type, int maxPlayers) {
        this.ownerId = ownerId;
        this.eventId = eventId;
        this.date = date;
        this.city = city;
        this.state = state;
        this.description = description;
        this.skillLevel = skillLevel;
        this.eventTitle = eventTitle;
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Event{" +
                "ownerId=" + ownerId +
                ", eventId=" + eventId +
                ", date='" + date + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", type='" + type + '\'' +
                ", maxPlayers=" + maxPlayers +
                '}';
    }
}
