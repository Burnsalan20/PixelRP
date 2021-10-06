package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.*;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.time.Period;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NavigableSet;

public class GenerateCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");
    private static final Text KEY_MODE = Text.of("mode");
    private static final Text KEY_DAYS = Text.of("days");

    public static CommandSpec createSpec(LogManager logMan, File configDir) {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.user(KEY_USER),
                        GenericArguments.enumValue(KEY_MODE, Mode.class),
                        GenericArguments.optional(
                                GenericArguments.integer(KEY_DAYS)
                        )
                )
                .permission(Constants.PERM_CMD_GENERATE)
                .executor(new GenerateCommand(logMan, configDir))
                .build();
    }

    private final LogManager logMan;
    private final File configDir;

    public GenerateCommand(LogManager logMan, File configDir) {
        this.logMan = logMan;
        this.configDir = configDir;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User target = args.<User>getOne(KEY_USER).get();

        UserLog log = logMan.requestUserLog(target);
        if (log == null) {
            src.sendMessage(Text.of(TextColors.RED, "This user does not have a log."));
            return CommandResult.empty();
        }

        final long from;
        if (args.hasAny(KEY_DAYS)) {
            long millis = Duration.of(args.<Integer>getOne(KEY_DAYS).get(), ChronoUnit.DAYS).toMillis();
            from = System.currentTimeMillis() - millis;
        } else {
            from = 0;
        }

        Mode mode = args.<Mode>getOne(KEY_MODE).get();
        switch (mode) {
            case COMMANDS:
                new LogWriter(src, new File(configDir, "commandlogs/" + target.getName() + ".log")) {
                    @Override
                    protected void write0(final FileWriter writer) throws IOException {
                        final ListIterator<LogEntry> it = log.getCommands().listIterator(log.getCommands().size());
                        while (it.hasPrevious()) {
                            final LogEntry e = it.previous();
                            if (from > 0 && e.getLogTime() < from) {
                                break;
                            }
                            writer.write(Formatter.COMMAND_FILE.format(e) + "\n");
                        }
                    }
                }.write();
                break;
            case ENTRIES:
                new LogWriter(src, new File(configDir, "userlogs/" + target.getName() + ".log")) {
                    @Override
                    protected void write0(final FileWriter writer) throws IOException {
                        for (LogEntry e : log.getEntries()) {
                            writer.write(Formatter.ENTRY_FILE.format(e) + "\n");
                        }
                    }
                }.write();
                break;
            case TIME:
                new LogWriter(src, new File(configDir, "timelogs/" + target.getName() + ".log")) {
                    @Override
                    protected void write0(final FileWriter writer) throws IOException {
                        NavigableSet<Period> periods = log.getTImeLog().getOnlinePeriods(from);
                        for (Iterator<Period> it = periods.descendingIterator(); it.hasNext(); ) {
                            writer.write(Formatter.TIME_FILE.format(it.next()) + "\n");
                        }
                    }
                }.write();
                break;
        }

        return CommandResult.success();
    }

    private enum Mode {
        COMMANDS,
        ENTRIES,
        TIME
    }

}
