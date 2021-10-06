package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.LoggerMechanic.LoggerMechanic;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.PagedView;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.Formatter;
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
import org.spongepowered.api.text.serializer.TextSerializers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ListIterator;

public class ViewCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");
    private static final Text KEY_COMMANDS = Text.of("commands");
    private static final Text KEY_DAYS = Text.of("days");

    // hadouken
    public static CommandSpec createSpec(LogManager logMan) {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.user(KEY_USER),
                        GenericArguments.optional(
                                GenericArguments.requiringPermission(
                                        GenericArguments.seq(
                                                GenericArguments.literal(KEY_COMMANDS, "commands"),
                                                GenericArguments.optional(
                                                        GenericArguments.integer(KEY_DAYS)
                                                )
                                        ),
                                        Constants.PERM_CMD_VIEWCMD
                                )
                        )
                )
                .permission(Constants.PERM_CMD_VIEWLOG)
                .executor(new ViewCommand(logMan))
                .build();
    }

    private final LogManager logMan;

    public ViewCommand(LogManager logMan) {
        this.logMan = logMan;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User target = args.<User>getOne(KEY_USER).get();
        LoggerMechanic lm = (LoggerMechanic) PixelRP.instance.mechanicManager.getMechanic(1);
        final UserLog log = logMan.requestUserLog(target);
        if (log == null) {
            src.sendMessage(Text.of(TextColors.RED, "This player does not have a log."));
            return CommandResult.empty();
        }

        if (args.hasAny(KEY_COMMANDS)) {
            long from = 0;
            if (args.hasAny(KEY_DAYS)) {
                from = System.currentTimeMillis()
                        - Duration.of(args.<Integer>getOne(KEY_DAYS).get(), ChronoUnit.DAYS).toMillis();
            }

            PagedView view = new PagedView(target.getName() + "'s commands");
            ListIterator<LogEntry> it = log.getCommands().listIterator(log.getCommands().size());
            while (it.hasPrevious()) {
                LogEntry e = it.previous();
                if (from > 0 && e.getLogTime() < from) {
                    break;
                }
                view.addLine(Text.of(Formatter.COMMAND_GAME.format(e)));
            }

            lm.getViews().put(src.getName(), view);
            src.sendMessage(view.getPage(1).get());
            return CommandResult.empty();
        }

        PagedView view = new PagedView(target.getName() + "'s entries", 3);
        int count = 0;
        for (LogEntry e : log.getEntries()) {
            view.addLine(Text.of("[", count, "] ", TextSerializers.FORMATTING_CODE.deserialize(Formatter.ENTRY_GAME.format(e))));
            count++;
        }

        lm.getViews().put(src.getName(), view);
        src.sendMessage(view.getPage(1).get());
        return CommandResult.empty();
    }

}
