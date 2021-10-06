package com.pixelrp.core;
import com.google.inject.Inject;
import com.pixelrp.core.mechanic.LoggerMechanic.LoggerMechanic;
import com.pixelrp.core.mechanic.MechanicManager;
import com.pixelrp.core.utils.user.UserManager;
import com.pixelrp.core.utils.configuration.Configuration;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.Optional;

@Plugin(id = "pixelrp", name = "PixelRP", version = "1.0")
public class PixelRP {

    public static PixelRP instance;

    @Inject
    public Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    public Configuration config;

    public UserManager userManager;
    public MechanicManager mechanicManager;

    @Listener
    public void onPreInit(GamePreInitializationEvent e){
        instance = this;
        config = new Configuration("config.conf");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        System.out.println("Successfully running RCPlugin");

        userManager = new UserManager();
        mechanicManager = new MechanicManager();
    }

    @Listener
    public void onClientConnect(ClientConnectionEvent.Join event){
        Cause cause = event.getCause();
        Optional<Player> player = cause.first(Player.class);
        userManager.addUser(player.get());
        System.out.println("[RCPlugin] " + player.get().getName() + " has joined the server!");
        System.out.println("[RCPlugin] " + "There are " + userManager.getUsersSize() + " players online!");
    }

    @Listener
    public void onClientDisconnect(ClientConnectionEvent.Disconnect event){
        Cause cause = event.getCause();
        Optional<Player> player = cause.first(Player.class);
//        userManager.removeUser(player.get());
        System.out.println("[RCPlugin] " + player.get().getName() + " has left the server!");
        System.out.println("[RCPlugin] " + "There are " + userManager.getUsersSize() + " players online!");
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event){
        LoggerMechanic lm = (LoggerMechanic) mechanicManager.getMechanic(1);
        if(lm.isEnabled()) lm.saveLog();
    }

    public Path getConfigDir(){
        return configDir;
    }

}
