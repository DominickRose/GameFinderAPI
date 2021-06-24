package com.ismadoro.daos;

import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDaoLocal implements PlayerDao{

    private static final Map<Integer, Player> map = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public Player addPlayer(Player player) {
        Integer id = idCounter++;
        player.setPlayerId(id);
        map.put(id, player);
        return player;
    }

    @Override
    public Player getSinglePlayer(int playerId) throws ResourceNotFound {
        Integer id = playerId;
        Player result = map.get(id);
        if (result == null) {
            throw new ResourceNotFound();
        }
        return result;
    }

    @Override
    public List<Player> getAllPlayers() {
        List<Player> results = new ArrayList<>();
        for (Integer i : map.keySet()) {
            results.add(map.get(i));
        }
        return results;
    }

    @Override
    public Player updatePlayer(Player player) {
        Integer id = player.getPlayerId();
        if (map.get(id) == null) {
            throw new ResourceNotFound();
        }
        map.put(id, player);
        return player;
    }

    @Override
    public boolean deletePlayer(int playerId) throws ResourceNotFound {
        Integer id = playerId;
        Player result = map.remove(id);
        if (result == null) {
            throw new ResourceNotFound();
        }
        else {
            return true;
        }
    }
}
