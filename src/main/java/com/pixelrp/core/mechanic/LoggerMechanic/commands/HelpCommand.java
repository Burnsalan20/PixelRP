package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
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

public class HelpCommand implements CommandExecutor {

    private static final SortedMap<String, CommandCallable> cmds = new TreeMap<>();

    public static CommandSpec createSpec() {
        // lil hack to allow the help command to be used as a parameter
        cmds.put(Constants.CMD_HELP, null);

        CommandSpec spec = CommandSpec.builder()
                .executor(new HelpCommand())
                .arguments()
                .build();

        cmds.remove(Constants.CMD_HELP);

        return spec;
    }

    public static void registerSubcommand(String label, CommandCallable callable) {
        checkArgument(!cmds.containsKey(label));
        cmds.put(label, callable);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of(TextColors.YELLOW, "=============== rcLogger Help ==============="));

        for (Map.Entry<String, CommandCallable> e : cmds.entrySet()) {
            if (!e.getValue().testPermission(src)) {
                continue;
            }

            src.sendMessage(createHelpMsg(src, e.getKey(), e.getValue()));
        }

        return CommandResult.success();
    }

    private Text createHelpMsg(CommandSource src, String label, CommandCallable callable) {
        return Text.of(TextColors.GOLD, "/log ", label, " ", callable.getUsage(src));
    }

}
