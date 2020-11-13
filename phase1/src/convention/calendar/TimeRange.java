package convention.calendar;

import convention.exception.InvalidTimeRangeException;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TimeRange implements Serializable {
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeRange(LocalDateTime start, LocalDateTime end) {
        if (start.isBefore(end)) {
            this.start = start;
            this.end = end;
        } else {
            throw new InvalidTimeRangeException();
        }
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean hasOverlap(TimeRange otherTimeRange) {
        boolean otherIsBefore = otherTimeRange.getStart().isBefore(getStart()) && otherTimeRange.getEnd().isBefore(getStart());
        boolean otherIsAfter = otherTimeRange.getStart().isAfter(getEnd()) && otherTimeRange.getEnd().isAfter(getEnd());

        return !otherIsBefore && !otherIsAfter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeRange) {
            return ((TimeRange) obj).getEnd().equals(getEnd()) &&
                    ((TimeRange) obj).getStart().equals(getStart());
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("[%s -> %s]", getStart(), getEnd());
    }
}
