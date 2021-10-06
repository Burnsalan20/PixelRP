package com.pixelrp.core.mechanic.LoggerMechanic.utils.log;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.time.Period;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class Formatter<T> {

    public abstract String format(T entry);

    // static part

    public static final SimpleDateFormat GAME_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd, HH:mm");
    public static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static String convertTime(long time) {
        String format = "s 'seconds'";
        if (time >= TimeUnit.MINUTES.toMillis(1)) {
            format = "m 'minutes' " + format;
        }
        if (time >= TimeUnit.HOURS.toMillis(1)) {
            format = "H 'hours' " + format;
        }
        if (time >= TimeUnit.DAYS.toMillis(1)) {
            format = "d 'days' " + format;
        }
        return DurationFormatUtils.formatDuration(time, format);
    }

    public static final Formatter<LogEntry> ENTRY_GAME = new Formatter<LogEntry>() {
        @Override
        public String format(LogEntry entry) {
            return TextSerializers.FORMATTING_CODE.serialize(
                    Text.of(TextColors.YELLOW, "By ", entry.getIssuer(),
                            " (", GAME_DATE_FORMAT.format(new Date(entry.getLogTime())), ")", Text.NEW_LINE,
                            TextColors.RESET, entry.getEntry())
            );
        }
    };

    public static final Formatter<LogEntry> ENTRY_FILE = new Formatter<LogEntry>() {
        @Override
        public String format(LogEntry entry) {
            return "By " + entry.getIssuer()
                    + " (" + FILE_DATE_FORMAT.format(new Date(entry.getLogTime())) + ")\n"
                    + entry.getEntry() + "\n--------------------------------------------------";
        }
    };

    public static final Formatter<LogEntry> COMMAND_GAME = new Formatter<LogEntry>() {
        @Override
        public String format(LogEntry entry) {
            return "[" + GAME_DATE_FORMAT.format(new Date(entry.getLogTime())) + "] " + entry.getEntry();
        }
    };

    public static final Formatter<LogEntry> COMMAND_FILE = new Formatter<LogEntry>() {
        @Override
        public String format(LogEntry entry) {
            return FILE_DATE_FORMAT.format(new Date(entry.getLogTime())) + " " + entry.getEntry();
        }
    };

    public static final Formatter<Period> TIME_GAME = new Formatter<Period>() {
        @Override
        public String format(Period period) {
            return TextSerializers.FORMATTING_CODE.serialize(
                    Text.of(TextColors.YELLOW, "[", GAME_DATE_FORMAT.format(new Date(period.getStart())), "] ",
                            TextColors.RESET, convertTime(period.getSum()))
            );
        }
    };

    public static final Formatter<Period> TIME_FILE = new Formatter<Period>() {
        @Override
        public String format(Period period) {
            return "[" + FILE_DATE_FORMAT.format(new Date(period.getStart())) + "] --> "
                    + "[" + FILE_DATE_FORMAT.format(new Date(period.getEnd())) + "] = " + convertTime(period.getSum());
        }
    };
}
