package com.pixelrp.core.utils.user;

import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.Channels;
import com.pixelrp.core.utils.database.Database;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class UserManager {

    private Database userDB;
    private Map<String, User> users;

    public UserManager(){
        userDB = new Database("users");
        users = new HashMap<>();

        try {
            users = (HashMap<String, User>) userDB.loadDataBase();
        } catch (Exception e){}

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
        if(!users.containsKey(player.getUniqueId().toString()))
            users.put(player.getUniqueId().toString(), new User(player.getUniqueId().toString()));
    }

    public void addUser(Player player, Channels channel){
        users.put(player.getUniqueId().toString(), new User(player.getUniqueId().toString()));
    }

    public void removeUser(Player player){
        users.remove(player.getUniqueId().toString());
    }

    public int getUsersSize(){
        return users.size();
    }

    public boolean saveDatabase(){
        try {
            userDB.saveDataBase(users);
            return true;
        } catch (Exception e){
            System.out.println("There was an issue saving the users database...");
            return false;
        }
    }

    public Database getDatabase(){
        return userDB;
    }
}
