package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
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

public class RemoveCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");
    private static final Text KEY_ID = Text.of("id");

    public static CommandSpec createSpec(LogManager logMan) {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.user(KEY_USER),
                        GenericArguments.integer(KEY_ID)
                )
                .permission(Constants.PERM_CMD_REMOVE)
                .executor(new RemoveCommand(logMan))
                .build();
    }

    private final LogManager logMan;

    public RemoveCommand(LogManager logMan) {
        this.logMan = logMan;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User target = args.<User>getOne(KEY_USER).get();
        int id = args.<Integer>getOne(KEY_ID).get();

        UserLog log = logMan.getUserLog(target);

        if (id >= log.getEntries().size()) {
            src.sendMessage(Text.of(TextColors.RED, "Specified entry does not exist."));
            return CommandResult.empty();
        }

        log.getEntries().remove(id);

        src.sendMessage(Text.of(TextColors.GREEN, "Entry removed."));
        return CommandResult.success();
    }

}
