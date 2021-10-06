package com.pixelrp.core.mechanic.roleplayMechanic;

import com.pixelrp.core.mechanic.Mechanic;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.HelpCommand;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.ListCommand;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.NewCommand;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.channelCommands.*;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.leaveChannelCommands.LeaveListCommand;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.leaveChannelCommands.LeaveOOCCommand;
import com.pixelrp.core.mechanic.roleplayMechanic.listeners.ChatListener;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.CardManager;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.ChannelManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class RoleplayMechanic extends Mechanic {

    private CardManager cardManager;
    private ChannelManager channelManager;

    public RoleplayMechanic(String id) {
        super(id);
    }

    @Override
    public void initialize() {
        cardManager = new CardManager();
        channelManager = new ChannelManager();

        Sponge.getEventManager().registerListeners(plugin, new ChatListener());

        buildCMDs();
    }

    private void buildCMDs(){
        //Card Commands
        CommandSpec.Builder baseCmdBuilder = CommandSpec.builder();
        CommandSpec helpCmdSpec = HelpCommand.createSpec();

        addSubcommand(baseCmdBuilder, NewCommand.createSpec(cardManager), "new");
        addSubcommand(baseCmdBuilder, ListCommand.createSpec(cardManager), "list");
        addSubcommand(baseCmdBuilder, HelpCommand.createSpec(), "help");

        baseCmdBuilder.executor(helpCmdSpec.getExecutor());

        Sponge.getCommandManager().register(plugin, baseCmdBuilder.build(), "card");

        //Channel Commands
        CommandSpec oocSpec = CommandSpec.builder().description(Text.of("Sets the users channel to OOC"))
                .executor(new OOCCommand()).build();
        CommandSpec loocSpec = CommandSpec.builder().description(Text.of("Sets the users channel to LOOC"))
                .executor(new LOOCCommand()).build();
        CommandSpec soocSpec = CommandSpec.builder().description(Text.of("Sets the users channel to SOOC"))
                .executor(new SOOCCommand()).build();
        CommandSpec rpSpec = CommandSpec.builder().description(Text.of("Sets the users channel to RP"))
                .executor(new RPCommand()).build();
        CommandSpec srpSpec = CommandSpec.builder().description(Text.of("Sets the users channel to SRP"))
                .executor(new SRPCommand()).build();

        Sponge.getCommandManager().register(plugin, oocSpec, "ooc");
        Sponge.getCommandManager().register(plugin, loocSpec, "looc");
        Sponge.getCommandManager().register(plugin, soocSpec, "sooc");
        Sponge.getCommandManager().register(plugin, rpSpec, "rp");
        Sponge.getCommandManager().register(plugin, srpSpec, "yell");

        //Leave Chanel Commands
        CommandSpec.Builder baseCmdBuilder1 = CommandSpec.builder();
        CommandSpec listCmdSpec = LeaveListCommand.createSpec();

        addSubcommandLeave(baseCmdBuilder1, LeaveOOCCommand.createSpec(), "ooc");
        addSubcommandLeave(baseCmdBuilder1, LeaveListCommand.createSpec(), "list");

        baseCmdBuilder1.executor(listCmdSpec.getExecutor());
        Sponge.getCommandManager().register(plugin, baseCmdBuilder1.build(), "leave");
    }

    private void addSubcommand(CommandSpec.Builder builder, CommandCallable callable, String label) {
        HelpCommand.registerSubcommand(label, callable);
        builder.child(callable, label);
    }

    private void addSubcommandLeave(CommandSpec.Builder builder, CommandCallable callable, String label) {
        LeaveListCommand.registerSubcommand(label, callable);
        builder.child(callable, label);
    }
}
