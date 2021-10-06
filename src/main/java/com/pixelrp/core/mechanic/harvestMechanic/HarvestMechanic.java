package com.pixelrp.core.mechanic.harvestMechanic;

import com.pixelrp.core.mechanic.Mechanic;
import com.pixelrp.core.mechanic.harvestMechanic.commands.ResourceEditCommand;
import com.pixelrp.core.mechanic.harvestMechanic.commands.ResourceHelpCommand;
import com.pixelrp.core.mechanic.harvestMechanic.commands.ResourceSetCommand;
import com.pixelrp.core.mechanic.harvestMechanic.listeners.HarvestListener;
import com.pixelrp.core.mechanic.harvestMechanic.utils.HarvestManager;
import com.pixelrp.core.mechanic.townMechanic.commands.*;
import com.pixelrp.core.mechanic.townMechanic.listeners.TownListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

public class HarvestMechanic extends Mechanic {

    private HarvestManager hm;

    public HarvestMechanic(String id) {
        super(id);
    }

    @Override
    public void initialize() {
        hm = new HarvestManager();

        buildCMDs();
        Sponge.getEventManager().registerListeners(plugin, new HarvestListener(hm));
    }

    private void buildCMDs(){
        CommandSpec.Builder baseCmdBuilder = CommandSpec.builder();
        CommandSpec helpCmdSpec = ResourceHelpCommand.createSpec();

        addSubcommand(baseCmdBuilder, ResourceEditCommand.createSpec(hm), "edit");
        addSubcommand(baseCmdBuilder, ResourceSetCommand.createSpec(hm), "set");
        addSubcommand(baseCmdBuilder, ResourceHelpCommand.createSpec(), "help");

        baseCmdBuilder.executor(helpCmdSpec.getExecutor());

        Sponge.getCommandManager().register(plugin, baseCmdBuilder.build(), "resource", "res");
    }

    private void addSubcommand(CommandSpec.Builder builder, CommandCallable callable, String label) {
        ResourceHelpCommand.registerSubcommand(label, callable);
        builder.child(callable, label);
    }
}
