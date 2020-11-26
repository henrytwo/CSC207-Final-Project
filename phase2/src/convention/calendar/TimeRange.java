package convention.calendar;

import convention.exception.InvalidTimeRangeException;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Object with a start end time date pair. Ensures the start time is always before the end time.
 */
public class TimeRange implements Serializable {
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Constructor for Time range
     *
     * @param start start time date
     * @param end   end time date
     */
    public TimeRange(LocalDateTime start, LocalDateTime end) {
        if (start.isBefore(end)) {
            this.start = start;
            this.end = end;
        } else {
            throw new InvalidTimeRangeException();
        }
    }

    /**
     * Get start time date object
     *
     * @return
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Get end time date object
     *
     * @return
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Tests if this time range overlaps with the other time range
     *
     * @param otherTimeRange other time range to test
     * @return true iff there is an overlap between this time range and the other
     */
    public boolean hasOverlap(TimeRange otherTimeRange) {
        boolean otherIsBefore = otherTimeRange.getStart().isBefore(getStart()) && otherTimeRange.getEnd().isBefore(getStart());
        boolean otherIsAfter = otherTimeRange.getStart().isAfter(getEnd()) && otherTimeRange.getEnd().isAfter(getEnd());

        return !otherIsBefore && !otherIsAfter;
    }

    /**
     * Tests of this time range equals another
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeRange) {
            return ((TimeRange) obj).getEnd().equals(getEnd()) &&
                    ((TimeRange) obj).getStart().equals(getStart());
        }

        return false;
    }

    /**
     * Gets the string representation of this time range
     * @return
     */
    @Override
    public String toString() {
        return String.format("[%s → %s]", getStart(), getEnd());
    }
}
