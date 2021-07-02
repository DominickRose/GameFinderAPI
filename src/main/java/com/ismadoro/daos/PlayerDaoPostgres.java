package com.ismadoro.daos;

import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.utils.ConnectionUtil;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoPostgres implements PlayerDao{
    @Override
    public Player addPlayer(Player player) {
        try(Connection connection = ConnectionUtil.createConnection()) {
            String sql = "insert into player (first_name, last_name, username, player_password, bio, visible, email, phone_number, state, city) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, player.getFirstName());
            ps.setString(2, player.getLastName());
            ps.setString(3, player.getUsername());
            ps.setString(4, player.getPassword());
            ps.setString(5, player.getBio());
            ps.setBoolean(6, player.isVisible());
            ps.setString(7, player.getEmail());
            ps.setString(8, player.getPhoneNumber());
            ps.setString(9, player.getState());
            ps.setString(10, player.getCity());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            int key = rs.getInt("player_id");
            player.setPlayerId(key);
            return player;

        } catch (SQLException sqlException) {
            if (sqlException.getSQLState().equals("23505")) {
                throw new DuplicateResourceException("A player with this username already exists");
            }
            return null;
        }
    }

    @Override
    public Player getSinglePlayer(int playerId) throws ResourceNotFound {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from player where player_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, playerId);

            ResultSet rs = ps.executeQuery();
            rs.next();

            Player player = new Player();
            player.setPlayerId(rs.getInt("player_id"));
            player.setFirstName(rs.getString("first_name"));
            player.setLastName(rs.getString("last_name"));
            player.setUsername(rs.getString("username"));
            player.setPassword(rs.getString("player_password"));
            player.setBio(rs.getString("bio"));
            player.setVisible(rs.getBoolean("visible"));
            player.setEmail(rs.getString("email"));
            player.setPhoneNumber(rs.getString("phone_number"));
            player.setCity(rs.getString("city"));
            player.setState(rs.getString("state"));

            return player;
        } catch (SQLException sqlException) {
            if (sqlException.getSQLState().equals("24000")) {
                throw new ResourceNotFound("Resource with the given ID does not exist");
            }
            return null;
        }
    }

    @Override
    public List<Player> getAllPlayers() {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "select * from player";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Player> players = new ArrayList<>();

            while(rs.next()) {
                Player player = new Player();
                player.setPlayerId(rs.getInt("player_id"));
                player.setFirstName(rs.getString("first_name"));
                player.setLastName(rs.getString("last_name"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("player_password"));
                player.setBio(rs.getString("bio"));
                player.setVisible(rs.getBoolean("visible"));
                player.setEmail(rs.getString("email"));
                player.setPhoneNumber(rs.getString("phone_number"));
                player.setCity(rs.getString("city"));
                player.setState(rs.getString("state"));

                players.add(player);
            }

            return players;
        } catch (SQLException sqlException) {
            return null;
        }
    }

    @Override
    public Player updatePlayer(Player player) {
        try(Connection connection = ConnectionUtil.createConnection()) {
            String sql = "update player set first_name=?, last_name=?, username=?, player_password=?, bio=?, visible=?, email=?, phone_number=?, state=?, city=? where player_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player.getFirstName());
            ps.setString(2, player.getLastName());
            ps.setString(3, player.getUsername());
            ps.setString(4, player.getPassword());
            ps.setString(5, player.getBio());
            ps.setBoolean(6, player.isVisible());
            ps.setString(7, player.getEmail());
            ps.setString(8, player.getPhoneNumber());
            ps.setString(9, player.getState());
            ps.setString(10, player.getCity());
            ps.setInt(11, player.getPlayerId());

            int result = ps.executeUpdate();
            if (result == 0) {
                throw new ResourceNotFound("Resource with the given ID does not exist");
            }
            return player;

        } catch (SQLException sqlException) {
            if (sqlException.getSQLState().equals("23505")) {
                throw new DuplicateResourceException("Failed to update because a player with this username already exists");
            }
            return null;
        }
    }

    @Override
    public boolean deletePlayer(int playerId) throws ResourceNotFound {
        try (Connection connection = ConnectionUtil.createConnection()) {
            String sql = "delete from player where player_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, playerId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFound("Resource with the given ID does not exist");
            }
            return true;
        } catch (SQLException sqlException) {
            return false;
        }
    }
}
