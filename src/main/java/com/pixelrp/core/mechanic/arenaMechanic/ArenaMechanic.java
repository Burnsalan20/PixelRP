package com.pixelrp.core.mechanic.arenaMechanic;

import com.pixelrp.core.mechanic.Mechanic;
import com.pixelrp.core.mechanic.arenaMechanic.commands.ArenaCreateCommand;
import com.pixelrp.core.mechanic.arenaMechanic.commands.ArenaHelpCommand;
import com.pixelrp.core.mechanic.arenaMechanic.utils.ArenaManager;
import com.pixelrp.core.mechanic.townMechanic.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

public class ArenaMechanic extends Mechanic {

    public ArenaMechanic(String id) {
        super(id);
    }

    private ArenaManager am;

    @Override
    public void initialize() {
        am = new ArenaManager();

        buildCMDs();
    }

    private void buildCMDs(){
        CommandSpec.Builder baseCmdBuilder = CommandSpec.builder();
        CommandSpec helpCmdSpec = ArenaHelpCommand.createSpec();

        addSubcommand(baseCmdBuilder, ArenaCreateCommand.createSpec(am), "create");
        addSubcommand(baseCmdBuilder, ArenaHelpCommand.createSpec(), "help");

        baseCmdBuilder.executor(helpCmdSpec.getExecutor());

        Sponge.getCommandManager().register(plugin, baseCmdBuilder.build(), "arena");
    }

    private void addSubcommand(CommandSpec.Builder builder, CommandCallable callable, String label) {
        ArenaHelpCommand.registerSubcommand(label, callable);
        builder.child(callable, label);
    }
}
