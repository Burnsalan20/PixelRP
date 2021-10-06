package com.pixelrp.core.mechanic.townMechanic;

import com.pixelrp.core.mechanic.Mechanic;
import com.pixelrp.core.mechanic.townMechanic.commands.*;
import com.pixelrp.core.mechanic.townMechanic.listeners.TownListener;
import com.pixelrp.core.mechanic.townMechanic.utils.TownManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

public class TownMechanic extends Mechanic {

    public TownMechanic(String id) {
        super(id);
    }

    private TownManager tm;

    @Override
    public void initialize() {
        tm = new TownManager();
        Sponge.getEventManager().registerListeners(plugin, new TownListener(tm));
        buildCMDs();
    }

    private void buildCMDs(){
        CommandSpec.Builder baseCmdBuilder = CommandSpec.builder();
        CommandSpec helpCmdSpec = TownHelpCommand.createSpec();

        addSubcommand(baseCmdBuilder, TownCreateCommand.createSpec(tm), "create");
        addSubcommand(baseCmdBuilder, TownEditCommand.createSpec(tm), "edit");
        addSubcommand(baseCmdBuilder, TownSaveCommand.createSpec(tm), "save");
        addSubcommand(baseCmdBuilder, TownSetBuilderCommand.createSpec(tm), "setbuilder");
        addSubcommand(baseCmdBuilder, TownInviteCommand.createSpec(tm), "invite");
        addSubcommand(baseCmdBuilder, TownKickCommand.createSpec(tm), "kick");
        addSubcommand(baseCmdBuilder, TownJoinCommand.createSpec(tm), "join");
        addSubcommand(baseCmdBuilder, TownLeaveCommand.createSpec(tm), "leave");
        addSubcommand(baseCmdBuilder, TownInfoCommand.createSpec(tm), "info");
        addSubcommand(baseCmdBuilder, TownHelpCommand.createSpec(), "help");

        baseCmdBuilder.executor(helpCmdSpec.getExecutor());

        Sponge.getCommandManager().register(plugin, baseCmdBuilder.build(), "town", "t");
    }

    private void addSubcommand(CommandSpec.Builder builder, CommandCallable callable, String label) {
        TownHelpCommand.registerSubcommand(label, callable);
        builder.child(callable, label);
    }

    public TownManager getTownManager(){
        return tm;
    }

}
