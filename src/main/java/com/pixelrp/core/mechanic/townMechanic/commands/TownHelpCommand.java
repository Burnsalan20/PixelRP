package com.pixelrp.core.mechanic.townMechanic.commands;

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

public class TownHelpCommand implements CommandExecutor {

    private static final SortedMap<String, CommandCallable> cmds = new TreeMap<>();

    public static CommandSpec createSpec() {
        // lil hack to allow the help command to be used as a parameter
        cmds.put("help", null);

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownHelpCommand())
                .arguments()
                .build();

        cmds.remove("help");

        return spec;
    }

    public static void registerSubcommand(String label, CommandCallable callable) {
        checkArgument(!cmds.containsKey(label));
        cmds.put(label, callable);
    }

    private Text createHelpMsg(CommandSource src, String label, CommandCallable callable) {
        return Text.of(TextColors.GOLD, "/town ", label, " ", callable.getUsage(src));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of(TextColors.YELLOW, "=============== Town Help ==============="));
        cmds.remove("join");
        for (Map.Entry<String, CommandCallable> e : cmds.entrySet()) {
            if (!e.getValue().testPermission(src)) {
                continue;
            }

            src.sendMessage(createHelpMsg(src, e.getKey(), e.getValue()));
        }

        return CommandResult.success();
    }
}