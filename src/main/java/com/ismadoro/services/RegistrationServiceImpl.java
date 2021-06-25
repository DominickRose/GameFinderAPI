package com.ismadoro.services;

import com.ismadoro.daos.RegistrationDao;
import com.ismadoro.entities.Registration;

import java.util.ArrayList;
import java.util.List;

public class RegistrationServiceImpl implements RegistrationService{
    private RegistrationDao registrationDao;

    public RegistrationServiceImpl(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    @Override
    public Registration addRegistration(Registration registration) {
        return this.registrationDao.addRegistration(registration);
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
        return this.registrationDao.deleteRegistration(registrationId);
    }

    @Override
    public List<Integer> getAllPlayersForEvent(int eventId) {
        List<Registration> allRegistrations = this.registrationDao.getAllRegistrations();
        List<Integer> playerIds = new ArrayList<>();

        //For now, this is what I'll go with.  We'll optimize it once we have the database set up
        for (Registration registration : allRegistrations) {
            if (registration.getEventId() == eventId) {
                playerIds.add(registration.getPlayerId());
            }
        }
        return playerIds;
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
