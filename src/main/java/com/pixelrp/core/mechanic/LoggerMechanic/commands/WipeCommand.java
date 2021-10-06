package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogManager;
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

public class WipeCommand implements CommandExecutor {

    private static final SortedMap<String, CommandCallable> cmds = new TreeMap<>();

    public static CommandSpec createSpec(LogManager logMan) {
        // lil hack to allow the help command to be used as a parameter
        cmds.put(Constants.CMD_WIPE, null);

        CommandSpec spec = CommandSpec.builder()
                .executor(new WipeCommand(logMan))
                .arguments()
                .build();

        cmds.remove(Constants.CMD_WIPE);

        return spec;
    }

    private final LogManager logMan;

    public WipeCommand(LogManager logMan){
        this.logMan = logMan;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        logMan.wipeLogs();

        src.sendMessage(Text.of(TextColors.GREEN, "Logs Wiped."));
        return CommandResult.success();
    }
}
