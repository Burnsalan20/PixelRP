package com.pixelrp.core.mechanic.LoggerMechanic.utils.log;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;

public class LogEntry implements Serializable {

    private static final long serialVersionUID = -7541738220230292525L;

    private final String entry;
    private final long logTime;
    private final String issuer;

    public LogEntry(String entry, String issuer) {
        Validate.notNull(entry);
        Validate.notNull(issuer);
        this.entry = entry;
        this.logTime = System.currentTimeMillis();
        this.issuer = issuer;
    }

    public long getLogTime() {
        return logTime;
    }

    public String getEntry() {
        return entry;
    }

    public String getIssuer() {
        return issuer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogEntry logEntry = (LogEntry) o;

        if (logTime != logEntry.logTime) {
            return false;
        }
        if (!entry.equals(logEntry.entry)) {
            return false;
        }
        if (!issuer.equals(logEntry.issuer)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = entry.hashCode();
        result = 31 * result + (int) (logTime ^ (logTime >>> 32));
        result = 31 * result + issuer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LogEntry { issuer: " + issuer + "; time: " + logTime + "; entry: " + entry + " }";
    }
}
