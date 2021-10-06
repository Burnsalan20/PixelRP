package com.pixelrp.core.mechanic.LoggerMechanic.utils.log;

import com.pixelrp.core.mechanic.LoggerMechanic.utils.time.TimeLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserLog implements Serializable {

    private static final long serialVersionUID = -8190450803618770527L;

    private final List<LogEntry> entries = new ArrayList<>();
    private final List<LogEntry> commands = new ArrayList<>();
    private final TimeLog onlineTime = new TimeLog();

    public TimeLog getTImeLog() {
        return onlineTime;
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public List<LogEntry> getCommands() {
        return commands;
    }
}