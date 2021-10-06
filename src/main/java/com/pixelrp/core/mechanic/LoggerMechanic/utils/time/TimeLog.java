package com.pixelrp.core.mechanic.LoggerMechanic.utils.time;

import java.io.Serializable;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class TimeLog implements Serializable {

    private static final long serialVersionUID = -5169315886754611194L;

    private final NavigableSet<Period> onlineTime = new TreeSet<>();
    private transient PeriodBuilder currentPeriod = null;


    public void startCounting() {
        currentPeriod = new PeriodBuilder();
    }

    public void stopCounting() {
        if (currentPeriod == null) {
            return;
        }
        onlineTime.add(currentPeriod.end());
        currentPeriod = null;
    }

    public long getCurrentOnlineTime() {
        if (currentPeriod == null) {
            return 0;
        }
        return currentPeriod.end().getSum();
    }

    public long getOnlineTime(long from) {
        return sum(getOnlinePeriods(from));
    }

    public NavigableSet<Period> getOnlinePeriods(long from) {
        if (from == 0) {
            return onlineTime;
        }
        return onlineTime.tailSet(new Period(from, from), true);
    }

    private long sum(Set<Period> periods) {
        long result = 0;
        for (Period p : periods) {
            result += p.getSum();
        }
        return result;
    }

    private static class PeriodBuilder {

        private final long start = System.currentTimeMillis();

        public Period end() {
            return new Period(start, System.currentTimeMillis());
        }
    }
}
