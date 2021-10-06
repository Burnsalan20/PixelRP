package com.pixelrp.core.mechanic.LoggerMechanic;

import com.pixelrp.core.mechanic.LoggerMechanic.commands.*;
import com.pixelrp.core.mechanic.LoggerMechanic.listeners.PlayerListener;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.PagedView;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogManager;
import com.pixelrp.core.mechanic.Mechanic;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoggerMechanic extends Mechanic {

    private LogManager logManager = null;

    private File dbFile = null;

    private Map<String, PagedView> views = new HashMap<>();

    public LoggerMechanic(String id) {
        super(id);
    }

    @Override
    public void initialize() {
        setupDataBase();

        Sponge.getEventManager().registerListeners(plugin, new PlayerListener(logManager));

        buildCMD();

        Sponge.getScheduler().createTaskBuilder().execute(this::saveLog)
                .delay(10L, TimeUnit.MINUTES).interval(10L, TimeUnit.MINUTES).submit(plugin);
    }

    private void setupDataBase(){
        dbFile = plugin.getConfigDir().resolve("logs.db").toFile();
        try {
            if (!dbFile.exists()) {
//                dbFile.getParentFile().mkdirs();
                Files.createFile(dbFile.toPath());
            } else {
                logManager = LogManager.loadFromFile(dbFile);
            }
        } catch (IOException ex) {
            plugin.logger.error("Failed to load log database.", ex);
        }
        if (logManager == null) {
            logManager = new LogManager(null);
        }
        new File(plugin.getConfigDir().toFile(), "commandlogs").mkdirs();
        new File(plugin.getConfigDir().toFile(), "userlogs").mkdirs();
        new File(plugin.getConfigDir().toFile(), "timelogs").mkdirs();
    }

    public void saveLog() {
        try {
            logManager.saveToFile(dbFile);
            plugin.logger.info("Log database saved.");
        } catch (IOException ex) {
            plugin.logger.error("Failed to save log database.", ex);
        }
    }

    private void buildCMD(){
        CommandSpec.Builder baseCmdBuilder = CommandSpec.builder();

        CommandSpec helpCmdSpec = HelpCommand.createSpec();

        addSubcommand(baseCmdBuilder, AddCommand.createSpec(logManager), Constants.CMD_ADD);
        addSubcommand(baseCmdBuilder, GenerateCommand.createSpec(logManager, plugin.getConfigDir().toFile()), Constants.CMD_GENERATE);
        addSubcommand(baseCmdBuilder, PageCommand.createSpec(logManager), Constants.CMD_PAGE);
        addSubcommand(baseCmdBuilder, RemoveCommand.createSpec(logManager), Constants.CMD_REMOVE);
        addSubcommand(baseCmdBuilder, SeenCommand.createSpec(logManager), Constants.CMD_SEEN);
        addSubcommand(baseCmdBuilder, ViewCommand.createSpec(logManager), Constants.CMD_VIEW);
        addSubcommand(baseCmdBuilder, WipeCommand.createSpec(logManager), Constants.CMD_WIPE);
        addSubcommand(baseCmdBuilder, helpCmdSpec, Constants.CMD_HELP);

        baseCmdBuilder.executor(helpCmdSpec.getExecutor());

        Sponge.getCommandManager().register(plugin, baseCmdBuilder.build(), Constants.CMD_BASE);
    }

    public Map<String, PagedView> getViews() {
        return views;
    }

    private void addSubcommand(CommandSpec.Builder builder, CommandCallable callable, String label) {
        HelpCommand.registerSubcommand(label, callable);
        builder.child(callable, label);
    }
}
