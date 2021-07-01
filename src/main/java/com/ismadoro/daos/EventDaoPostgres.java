package com.ismadoro.daos;

import com.ismadoro.entities.Event;
import com.ismadoro.exceptions.InvalidInput;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.utils.ConnectionUtil;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDaoPostgres implements EventDao {

    @Override
    public String nameTrimmer(int eventId) {
        String untrimmed = getSingleEvent(eventId).getEventTitle();
        String trimmed = untrimmed.replace(" ", "");
        return trimmed;
    }

    @Override
    public Event addEvent(Event event) {
        try (Connection connection = ConnectionUtil.createConnection()) {

            String sql = "insert into events (owner_id, event_date, city, state, description, skill_level, event_title, event_type, max_players) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, event.getOwnerId());
            ps.setFloat(2, event.getEventDate());
            ps.setString(3, event.getCity());
            ps.setString(4, event.getState());
            ps.setString(5, event.getDescription());
            ps.setString(6, event.getSkillLevel());
            ps.setString(7, event.getEventTitle());
            ps.setString(8, event.getEventType());
            ps.setInt(9, event.getMaxPlayers());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            event.setEventId(key);
            return event;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0) {
                throw new InvalidInput("Event_date and max_events must be greater than 0");
            }
            throw new InvalidInput("One or more Event fields were invalid or null");

        }
    }

    @Override
    public Event getSingleEvent(int id) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            if (id < 0)
                throw new InvalidInput("Invalid id for event");

            String sql = "select * from events where event_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            rs.next();
            Event event = new Event();

            event.setOwnerId(rs.getInt("owner_id"));
            event.setEventId(rs.getInt("event_id"));
            event.setEventDate(rs.getFloat("event_date"));
            event.setCity(rs.getString("city"));
            event.setState(rs.getString("state"));
            event.setDescription(rs.getString("description"));
            event.setSkillLevel(rs.getString("skill_level"));
            event.setEventTitle(rs.getString("event_title"));
            event.setEventType(rs.getString("event_type"));
            event.setMaxPlayers(rs.getInt("max_players"));


            return event;


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new ResourceNotFound("No event with that id exists");
        }

    }

    @Override
    public List<Event> getAllEvents() {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from events";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Event> events = new ArrayList<>();

            while (rs.next()) {
                Event event = new Event();
                event.setOwnerId(rs.getInt("owner_id"));
                event.setEventId(rs.getInt("event_id"));
                event.setEventDate(rs.getFloat("event_date"));
                event.setCity(rs.getString("city"));
                event.setState(rs.getString("state"));
                event.setDescription(rs.getString("description"));
                event.setSkillLevel(rs.getString("skill_level"));
                event.setEventTitle(rs.getString("event_title"));
                event.setEventType(rs.getString("event_type"));
                event.setMaxPlayers(rs.getInt("max_players"));
                events.add(event);
            }
            return events;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        return null;
    }

    @Override
    public Event updateEvent(Event event) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "update events set owner_id=?, event_date=?, city=?, state=?, description=?, skill_level=?, event_title=?, event_type=?, max_players=? where event_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, event.getOwnerId());
            ps.setFloat(2, event.getEventDate());
            ps.setString(3, event.getCity());
            ps.setString(4, event.getState());
            ps.setString(5, event.getDescription());
            ps.setString(6, event.getSkillLevel());
            ps.setString(7, event.getEventTitle());
            ps.setString(8, event.getEventType());
            ps.setInt(9, event.getMaxPlayers());
            ps.setInt(10, event.getEventId());

            ps.executeUpdate();

            return event;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            if (event.getEventDate() <= 0 || event.getMaxPlayers() <= 0) {
                throw new InvalidInput("Event_date and max_events must be greater than 0");
            }
            throw new InvalidInput("One or more Event fields were invalid or null");
        }
    }

    @Override
    public boolean deleteEvent(int eventId) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "delete from events where event_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ps.execute();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }
}
