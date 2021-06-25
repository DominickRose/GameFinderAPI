package com.ismadoro.daos;

import com.ismadoro.entities.Event;
import com.ismadoro.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDaoPostgres implements EventDao {
    @Override
    public Event addEvent(Event event) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "insert into event (ownerId, date, city, state, description, skillLevel, eventTitle, type, maxPlayers) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, event.getOwnerId());
            ps.setString(2, event.getDate());
            ps.setString(3, event.getCity());
            ps.setString(4, event.getState());
            ps.setString(5, event.getDescription());
            ps.setString(6, event.getSkillLevel());
            ps.setString(7, event.getEventTitle());
            ps.setString(8, event.getType());
            ps.setInt(9, event.getMaxPlayers());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(0);
            event.setEventId(key);
            return event;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public Event getSingleEvent(int Id) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from event where eventId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            Event event = new Event();

            event.setOwnerId(rs.getInt("ownerId"));
            event.setEventId(rs.getInt("eventId"));
            event.setDate(rs.getString("date"));
            event.setCity(rs.getString("city"));
            event.setState(rs.getString("state"));
            event.setDescription(rs.getString("description"));
            event.setSkillLevel(rs.getString("skillLevel"));
            event.setEventTitle(rs.getString("eventTitle"));
            event.setType(rs.getString("type"));
            event.setMaxPlayers(rs.getInt("maxPlayers"));

            return event;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from event";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Event> events = new ArrayList<>();

            while (rs.next()) {
                Event event = new Event();
                event.setOwnerId(rs.getInt("ownerId"));
                event.setEventId(rs.getInt("eventId"));
                event.setDate(rs.getString("date"));
                event.setCity(rs.getString("city"));
                event.setState(rs.getString("state"));
                event.setDescription(rs.getString("description"));
                event.setSkillLevel(rs.getString("skillLevel"));
                event.setEventTitle(rs.getString("eventTitle"));
                event.setType(rs.getString("type"));
                event.setMaxPlayers(rs.getInt("maxPlayers"));
                events.add(event);
            }
            return events;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public Event updateEvent(Event event) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "update event set ownerId=?, date=?, city=?, state=?, description=?, skillLevel=?, eventTitle=?, type=?, maxPlayers=? where eventId=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, event.getOwnerId());
            ps.setString(2, event.getDate());
            ps.setString(3, event.getCity());
            ps.setString(4, event.getState());
            ps.setString(5, event.getDescription());
            ps.setString(6, event.getSkillLevel());
            ps.setString(7, event.getEventTitle());
            ps.setString(8, event.getType());
            ps.setInt(9, event.getMaxPlayers());
            ps.setInt(10, event.getEventId());

            ps.executeUpdate();

            return event;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteEvent(int eventId) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "delete from event where eventId = ?";
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
