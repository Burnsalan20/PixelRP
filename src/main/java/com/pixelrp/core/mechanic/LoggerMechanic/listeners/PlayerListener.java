package com.pixelrp.core.mechanic.LoggerMechanic.listeners;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogEntry;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerListener {
    private final LogManager logMan;

    public PlayerListener(LogManager logMan) {
        this.logMan = logMan;
    }

    @Listener(order = Order.POST)
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        final Player player = event.getTargetEntity();
        if (player.hasPermission(Constants.PERM_LOG_TIME)) {
            logMan.getUserLog(player).getTImeLog().startCounting();
        }
    }

    @Listener(order = Order.POST)
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event) {
        // nothing happens if it did not start, perm check not required
        logMan.getUserLog(event.getTargetEntity()).getTImeLog().stopCounting();
    }

    @Listener
    public void onServerEnd(GameStoppedServerEvent event){
        if(Sponge.getServer().getOnlinePlayers().size() > 1){
            for(Player player : Sponge.getServer().getOnlinePlayers())
                logMan.getUserLog(player).getTImeLog().stopCounting();
        }
    }

    @Listener(order = Order.POST)
    public void onPlayerCommand(SendCommandEvent event, @First Player player) {
        if (player.hasPermission(Constants.PERM_LOG_CMD)) {
            final LogEntry entry = new LogEntry(event.getCommand() + " " + event.getArguments(), "rcLogger");
            logMan.getUserLog(player).getCommands().add(entry);
        }
    }
}
