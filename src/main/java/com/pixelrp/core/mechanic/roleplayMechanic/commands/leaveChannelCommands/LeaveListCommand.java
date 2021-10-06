package com.pixelrp.core.mechanic.roleplayMechanic.commands.leaveChannelCommands;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;

public class LeaveListCommand implements CommandExecutor {

    private static final SortedMap<String, CommandCallable> cmds = new TreeMap<>();

    public static CommandSpec createSpec() {
        cmds.put("list", null);

        CommandSpec spec = CommandSpec.builder()
                .executor(new LeaveListCommand())
                .arguments()
                .build();

        cmds.remove("list");

        return spec;
    }

    public static void registerSubcommand(String label, CommandCallable callable) {
        checkArgument(!cmds.containsKey(label));
        cmds.put(label, callable);
    }

    public LeaveListCommand(){

    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of(TextColors.YELLOW, "=============== Channel List ==============="));

        for (Map.Entry<String, CommandCallable> e : cmds.entrySet()) {
            if (!e.getValue().testPermission(src)) {
                continue;
            }

            src.sendMessage(createListMsg(src, e.getKey(), e.getValue()));
        }

        return CommandResult.success();
    }

    private Text createListMsg(CommandSource src, String label, CommandCallable callable) {
        return Text.of(TextColors.GOLD, "/leave ", label, " ", callable.getUsage(src));
    }
}
