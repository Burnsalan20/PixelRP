package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.LoggerMechanic.LoggerMechanic;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.Constants;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.PagedView;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.Formatter;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogManager;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.UserLog;
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
import org.spongepowered.api.text.serializer.TextSerializers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.NavigableSet;

public class SeenCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");
    private static final Text KEY_DAYS = Text.of("days");

    public static CommandSpec createSpec(LogManager logMan) {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.user(KEY_USER),
                        GenericArguments.optional(
                                GenericArguments.integer(KEY_DAYS)
                        )
                )
                .permission(Constants.PERM_CMD_SEEN)
                .executor(new SeenCommand(logMan))
                .build();
    }

    private final LogManager logMan;

    public SeenCommand(LogManager logMan) {
        this.logMan = logMan;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User target = args.<User>getOne(KEY_USER).get();

        UserLog log = logMan.requestUserLog(target);
        if (log == null) {
            src.sendMessage(Text.of(TextColors.RED, "This user does not have a log."));
            return CommandResult.empty();
        }

        String periodString = "Total: ";
        final long from;
        if (args.hasAny(KEY_DAYS)) {
            int days = args.<Integer>getOne(KEY_DAYS).get();
            from = System.currentTimeMillis() - Duration.of(days, ChronoUnit.DAYS).toMillis();
            periodString = "Last " + days + " days: ";
        } else {
            from = 0;
        }

        NavigableSet<Period> periods = log.getTImeLog().getOnlinePeriods(from);
        PagedView view = new PagedView(target.getName() + "'s playtime");
        long currentTime = log.getTImeLog().getCurrentOnlineTime();
        if (currentTime > 0) {
            view.addLine(Text.of(TextColors.YELLOW, "Current online time: ", TextColors.RESET,
                    Formatter.convertTime(currentTime)));
        }

        long totalDuration = periods.stream().map(Period::getSum).reduce(0L, Long::sum);
        view.addLine(Text.of(TextColors.YELLOW, periodString, TextColors.RESET, Formatter.convertTime(totalDuration)));

        Iterator<Period> it = periods.descendingIterator();
        while (it.hasNext()) {
            view.addLine(TextSerializers.FORMATTING_CODE.deserialize(Formatter.TIME_GAME.format(it.next())));
        }

        src.sendMessage(view.getPage(1).get());
        LoggerMechanic lm = (LoggerMechanic) PixelRP.instance.mechanicManager.getMechanic(1);
        lm.getViews().put(src.getName(), view);

        return CommandResult.success();
    }

}
