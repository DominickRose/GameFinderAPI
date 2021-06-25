package com.ismadoro.daos;

import com.ismadoro.entities.Registration;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationDaoLocal implements RegistrationDao{
    private final Map<Integer, Registration> registrationMap = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public Registration addRegistration(Registration registration) {
        Integer id = idGenerator++;
        registrationMap.put(id, registration);
        return registrationMap.get(id);
    }

    @Override
    public Registration getSingleRegistation(int registrationId) {
        Registration toReturn = registrationMap.get(registrationId);
        if (toReturn == null) {
            throw new ResourceNotFound("The Registration with the given ID could not be found");
        }
        return toReturn;
    }

    @Override
    public List<Registration> getAllRegistrations() {
        List<Registration> allRegistrations = new ArrayList<>();
        for (Integer id: registrationMap.keySet()) {
            allRegistrations.add(registrationMap.get(id));
        }
        return allRegistrations;
    }

    @Override
    public Registration updateRegistration(Registration registration) {
        Integer id = registration.getPlayerId();
        if (registrationMap.get(id) == null) {
            throw new ResourceNotFound("The Registration with the given ID could not be found");
        }
        registrationMap.put(id, registration);
        return registrationMap.get(id);
    }

    @Override
    public boolean deleteRegistration(int registrationId) {
        Integer id = registrationId;
        if (registrationMap.get(id) == null) {
            throw new ResourceNotFound("The Registration with the given ID could not be found");
        }
        registrationMap.remove(id);
        return registrationMap.get(id) == null; //Check that it actually has been removed
    }
}
