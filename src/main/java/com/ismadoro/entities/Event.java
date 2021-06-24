package com.ismadoro.entities;

public class Event {
    private int ownerId;
    private int eventId;
    private String date;
    private int location;
    private String description;
    private String skillLevel;
    private String eventTitle;
    private String type;

    public Event() {
    }

    public Event(int ownerId, int eventId, String date, int location, String description, String skillLevel, String eventTitle, String type) {
        this.ownerId = ownerId;
        this.eventId = eventId;
        this.date = date;
        this.location = location;
        this.description = description;
        this.skillLevel = skillLevel;
        this.eventTitle = eventTitle;
        this.type = type;
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
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
                ", location=" + location +
                ", description='" + description + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
