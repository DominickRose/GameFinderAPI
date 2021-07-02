package com.ismadoro.services;

import com.ismadoro.daos.RegistrationDao;
import com.ismadoro.entities.Registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationServiceImpl implements RegistrationService{
    private RegistrationDao registrationDao;
    private Map<Integer, ArrayList<Integer>> playersInEventMap;

    public RegistrationServiceImpl(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;

        playersInEventMap = new HashMap<>();
        List<Registration> registrationList = getAllRegistrations();
        for (Registration registration : registrationList) {
            ArrayList<Integer> ids = playersInEventMap.get(registration.getEventId());

            if (ids == null) {
                ids = new ArrayList<>();
            }

            ids.add(registration.getPlayerId());
            playersInEventMap.put(registration.getEventId(), ids);
        }
    }

    @Override
    public Registration addRegistration(Registration registration) {
        Registration newRegistration = this.registrationDao.addRegistration(registration);

        ArrayList<Integer> ids = playersInEventMap.get(newRegistration.getEventId());
        if (ids == null) ids = new ArrayList<>();
        ids.add(newRegistration.getPlayerId());
        playersInEventMap.put(newRegistration.getEventId(), ids);

        return newRegistration;
    }

    @Override
    public Registration getSingleRegistration(int registrationId) {
        return this.registrationDao.getSingleRegistation(registrationId);
    }

    @Override
    public List<Registration> getAllRegistrations() {
        return this.registrationDao.getAllRegistrations();
    }

    @Override
    public Registration updateRegistration(Registration registration) {
        return this.registrationDao.updateRegistration(registration);
    }

    @Override
    public boolean deleteRegistration(int registrationId) {
        Registration toDelete = this.registrationDao.getSingleRegistation(registrationId);
        boolean result = this.registrationDao.deleteRegistration(registrationId);

        ArrayList<Integer> ids = this.playersInEventMap.get(toDelete.getEventId());
        ids.remove(new Integer(toDelete.getPlayerId()));

        return result;
    }

    @Override
    public boolean deleteRegistrationByContents(int playerId, int eventId) {
        List<Registration> allRegistrations = this.registrationDao.getAllRegistrations();
        int toDelete = 0;
        for (Registration registration : allRegistrations) {
            if (registration.getPlayerId() == playerId && registration.getEventId() == eventId) {
                toDelete = registration.getRegistrationId();
            }
        }

        boolean result = this.registrationDao.deleteRegistration(toDelete);
        ArrayList<Integer> players = this.playersInEventMap.get(eventId);
        if (players != null)
            players.remove(new Integer(playerId));
        this.playersInEventMap.put(eventId, players);
        return result;
    }

    @Override
    public boolean isPlayerRegisteredForEvent(int playerId, int eventId) {
        List<Integer> registeredPlayers = this.playersInEventMap.get(eventId);
        return (registeredPlayers != null) && registeredPlayers.contains(playerId);
    }

    @Override
    public List<Integer> getAllPlayersForEvent(int eventId) {
        ArrayList<Integer> toReturn = this.playersInEventMap.get(eventId);
        if (toReturn == null) {
            return new ArrayList<>();
        } else {
            return toReturn;
        }
    }

    @Override
    public List<Integer> getAllEventsForPlayer(int playerId) {
        List<Registration> allRegistrations = this.registrationDao.getAllRegistrations();
        List<Integer> eventIds = new ArrayList<>();

        //For now, this is what I'll go with.  We'll optimize it once we have the database set up
        for (Registration registration : allRegistrations) {
            if (registration.getPlayerId() == playerId) {
                eventIds.add(registration.getEventId());
            }
        }
        return eventIds;
    }
}
