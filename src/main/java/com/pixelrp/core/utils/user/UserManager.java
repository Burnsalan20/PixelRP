package com.pixelrp.core.utils.user;

import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class UserManager {

    private Map<String, User> users = new HashMap<>();

    public UserManager(){

    }

    public List<User> getAllUsers(){
        if(users.size() > 0) return new ArrayList<User>(users.values());

        return null;
    }

    public User getUser(Player player){
        if(users.containsKey(player.getUniqueId().toString()))
            return users.get(player.getUniqueId().toString());

        return null;
    }

    public void addUser(Player player){
        users.put(player.getUniqueId().toString(), new User(player.getUniqueId().toString()));
    }

    public void removeUser(Player player){
        users.remove(player.getUniqueId().toString());
    }

    public int getUsersSize(){
        return users.size();
    }

    public void destroy(Player player){
        player.getInventory().
    }
}
