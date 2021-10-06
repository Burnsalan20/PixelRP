package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogEntry;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogManager;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.UserLog;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AddCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");
    private static final Text KEY_ENTRY = Text.of("entry");

    public static CommandSpec createSpec(LogManager logMan) {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.user(KEY_USER),
                        GenericArguments.remainingJoinedStrings(KEY_ENTRY)
                )
                .permission(Constants.PERM_CMD_ADD)
                .executor(new AddCommand(logMan))
                .build();
    }

    private final LogManager logMan;

    public AddCommand(LogManager logMan) {
        this.logMan = logMan;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User target = args.<User>getOne(KEY_USER).get();
        String entryStr = args.<String>getOne(KEY_ENTRY).get();

        UserLog log = logMan.getUserLog(target);

        LogEntry entry = new LogEntry(entryStr, src.getName());
        log.getEntries().add(entry);

        src.sendMessage(Text.of(TextColors.GREEN, "Entry added."));
        return CommandResult.success();
    }

}
