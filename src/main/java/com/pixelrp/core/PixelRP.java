package com.pixelrp.core;
import com.google.inject.Inject;
import com.pixelrp.core.mechanic.MechanicManager;
import com.pixelrp.core.temp.PingCommand;
import com.pixelrp.core.temp.TestCommand;
import com.pixelrp.core.utils.configuration.Configuration;
import com.pixelrp.core.utils.user.UserManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.Optional;

@Plugin(id = "pixelrp", name = "PixelRP", version = "1.0")
public class PixelRP {

    public static PixelRP instance;

    @Inject
    private Logger logger;

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
        logger.info("Successfully running PixelRP");
        System.out.println("Successfully running PixelRP");

        registerCommands();

        userManager = new UserManager();
        mechanicManager = new MechanicManager();
    }

    @Listener
    public void onClientConnect(ClientConnectionEvent.Join event){
        Cause cause = event.getCause();
        Optional<Player> player = cause.first(Player.class);
        userManager.addUser(player.get());
        System.out.println("[PixelRP] " + player.get().getName() + " has joined the server!");
        System.out.println("[PixelRP] " + "There are " + userManager.getUsersSize() + " players online!");
    }

    @Listener
    public void onClientDisconnect(ClientConnectionEvent.Disconnect event){
        Cause cause = event.getCause();
        Optional<Player> player = cause.first(Player.class);
        userManager.removeUser(player.get());
        System.out.println("[PixelRP] " + player.get().getName() + " has left the server!");
        System.out.println("[PixelRP] " + "There are " + userManager.getUsersSize() + " players online!");
    }

    private void registerCommands(){
        // ping
        CommandSpec pingSpec = CommandSpec.builder()
                .description(Text.of("Basic ping command."))
                .permission("pixelrp.player")
                .executor(new PingCommand())
                .build();

        Sponge.getCommandManager().register(this, pingSpec, "ping");

        // test <x> <z>
        CommandSpec testSpec = CommandSpec.builder()
                .description(Text.of(""))
                .permission("pixelrp.player")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("x"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("y"))))
                .executor(new TestCommand())
                .build();

        Sponge.getCommandManager().register(this, testSpec, "test");
    }

    public Path getConfigDir(){
        return configDir;
    }

}
