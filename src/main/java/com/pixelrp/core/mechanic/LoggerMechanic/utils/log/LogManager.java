package com.pixelrp.core.mechanic.LoggerMechanic.utils.log;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.LoggerMechanic.LoggerMechanic;
import org.spongepowered.api.entity.living.player.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogManager {

    private final Map<UUID, UserLog> users;

    public LogManager(Map<UUID, UserLog> users) {
        if (users == null) {
            users = new HashMap<>();
        }
        this.users = users;
    }

    public UserLog getUserLog(User user) {
        UserLog log = users.get(user.getUniqueId());
        if (log == null) {
            log = new UserLog();
            users.put(user.getUniqueId(), log);
        }
        return log;
    }

    public UserLog requestUserLog(User user) {
        return users.get(user.getUniqueId());
    }

    public void saveToFile(File file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(users);
        try {
            oos.flush();
            oos.close();
        } catch (Exception ignore) {
        }
    }

    public static LogManager loadFromFile(File file) throws IOException {
        Map<UUID, UserLog> temp = null;

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        try {
            temp = (Map<UUID, UserLog>) ois.readObject();
        } catch (ClassNotFoundException ex) {
            PixelRP.instance.logger.error("Failed to load log database. Invalid database format.");
        }
        try {
            ois.close();
        } catch (Exception ignore) {
        }
        return new LogManager(temp);
    }

    public void wipeLogs(){
        if(users.size() > 0)
        for(UserLog log : users.values()){
            if(log.getCommands().size() > 0) log.getCommands().clear();
            if(log.getEntries().size() > 0) log.getEntries().clear();
        }

        LoggerMechanic lm = (LoggerMechanic) PixelRP.instance.mechanicManager.getMechanic(1);
        lm.saveLog();
    }
}
