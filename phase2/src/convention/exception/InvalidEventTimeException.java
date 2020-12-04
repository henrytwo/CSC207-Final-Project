package convention.exception;

import convention.calendar.TimeRange;

public class InvalidEventTimeException extends RuntimeException {
    public InvalidEventTimeException(TimeRange eventTimeRange, TimeRange conferenceTimeRange) {
        super(String.format("Unable to book events at %s since it goes beyond the start/end time of the parent conference of %s.", eventTimeRange, conferenceTimeRange));
    }
}
