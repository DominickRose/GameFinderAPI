package com.ismadoro.daos;

import com.ismadoro.entities.Registration;

import java.util.List;

public interface RegistrationDao {
    //Create
    Registration addRegistration(Registration registration);

    //Read
    Registration getSingleRegistation(int registrationId);
    List<Registration> getAllRegistrations();

    //Update
    Registration updateRegistration(Registration registration);

    //Delete
    boolean deleteRegistration(int registrationId);
}
