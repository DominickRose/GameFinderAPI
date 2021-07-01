package com.ismadoro.services;

import com.ismadoro.entities.Registration;

import java.util.List;

public interface RegistrationService {
    //Create Wrapper
    public Registration addRegistration(Registration registration);

    //Read Wrapper
    public Registration getSingleRegistration(int registrationId);
    public List<Registration>  getAllRegistrations();

    //Update Wrapper
    public Registration updateRegistration(Registration registration);

    //Delete Wrapper
    public boolean deleteRegistration(int registrationId);

    //Business Logic
    public boolean deleteRegistrationByContents(int playerId, int eventId);
    public boolean isPlayerRegisteredForEvent(int playerId, int eventId);
    public List<Integer> getAllPlayersForEvent(int eventId);
    public List<Integer> getAllEventsForPlayer(int playerId);
}
