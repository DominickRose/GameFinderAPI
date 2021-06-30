package com.ismadoro.entities;

public class Registration {
    int registrationId;
    int playerId;
    int eventId;

    public Registration() {
    }

    public Registration(int registrationId, int playerId, int eventId) {
        this.registrationId = registrationId;
        this.playerId = playerId;
        this.eventId = eventId;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "registrationId=" + registrationId +
                ", playerId=" + playerId +
                ", eventId=" + eventId +
                '}';
    }
}
