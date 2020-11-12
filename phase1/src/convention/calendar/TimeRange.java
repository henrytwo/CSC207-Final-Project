package convention.calendar;

import util.exception.InvalidTimeRangeException;

import java.time.LocalDateTime;

public class TimeRange {
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
