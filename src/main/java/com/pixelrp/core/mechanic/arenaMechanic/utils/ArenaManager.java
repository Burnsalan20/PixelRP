package com.pixelrp.core.mechanic.arenaMechanic.utils;

import com.pixelrp.core.utils.database.Database;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {

    public Database database;
    public Map<String, Arena> arenas;

    public ArenaManager(){
        database = new Database("arenas");
        arenas = new HashMap<>();

        try {
            arenas = (Map<String, Arena>) database.loadDataBase();
        } catch (Exception e){}
    }

    public boolean createArena(String name, Location loc1, Location loc2){
        if(!arenas.containsKey(name)){
            arenas.put(name, new Arena(name));
            save();
            return true;
        }
        return false;
    }

    public boolean setLocation(String key, Arena arena, Player player){
        Location location = player.getLocation();
        if(arena.locations.containsKey(key)){
            arena.locations.put(key, new ArenaLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), player.getRotation().getX(), player.getRotation().getY()));
            save();
            return true;
        }
        return false;
    }

    public void save(){
        this.database.saveDataBase(arenas);
    }
}
