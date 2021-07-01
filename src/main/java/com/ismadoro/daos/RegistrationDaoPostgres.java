package com.ismadoro.daos;

import com.ismadoro.entities.Registration;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDaoPostgres implements RegistrationDao{
    @Override
    public Registration addRegistration(Registration registration) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "insert into registration (player_id, event_id) values (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, registration.getPlayerId());
            ps.setInt(2, registration.getEventId());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            int key = rs.getInt("registration_id");
            registration.setRegistrationId(key);
            return registration;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public Registration getSingleRegistation(int registrationId) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from registration where registration_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, registrationId);
            ResultSet rs = ps.executeQuery();
            rs.next();

            Registration registration = new Registration();
            registration.setRegistrationId(rs.getInt("registration_id"));
            registration.setPlayerId(rs.getInt("player_id"));
            registration.setEventId(rs.getInt("event_id"));
            return registration;

        } catch (SQLException sqlException) {
            if (sqlException.getSQLState().equals("24000")) {
                throw new ResourceNotFound("The resource with the given ID could not be found");
            }
            sqlException.printStackTrace();
            return null;
        }    }

    @Override
    public List<Registration> getAllRegistrations() {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from registration";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Registration> registrationList = new ArrayList<>();

            while(rs.next()) {
                Registration registration = new Registration();
                registration.setRegistrationId(rs.getInt("registration_id"));
                registration.setPlayerId(rs.getInt("player_id"));
                registration.setEventId(rs.getInt("event_id"));
                registrationList.add(registration);
            }

            return registrationList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public Registration updateRegistration(Registration registration) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "update registration set player_id=?, event_id=? where registration_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, registration.getPlayerId());
            ps.setInt(2, registration.getEventId());
            ps.setInt(3, registration.getRegistrationId());

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new ResourceNotFound("The resource with the given ID could not be found");
            }
            return registration;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }    }

    @Override
    public boolean deleteRegistration(int registrationId) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "delete from registration where registration_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, registrationId);

            int deletedRows = ps.executeUpdate();
            if (deletedRows == 0) {
                throw new ResourceNotFound("The resource with the given ID could not be found");
            }
            return true; 
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }
}
