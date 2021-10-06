package com.pixelrp.core.mechanic.LoggerMechanic.utils.time;

import java.io.Serializable;

public class Period implements Comparable<Period>, Serializable {

    private static final long serialVersionUID = -7915571177104378224L;

    private final long start;
    private final long end;

    public Period(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getSum() {
        return end - start;
    }

    @Override
    public int compareTo(Period o) {
        if (start != o.start) {
            return start < o.start ? -1 : 1;
        }
        if (end != o.end) {
            return end < o.end ? -1 : 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Period period = (Period) o;
        if (end != period.end) {
            return false;
        }
        if (start != period.start) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        return result;
    }
}
