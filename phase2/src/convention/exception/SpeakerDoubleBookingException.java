package convention.exception;

import convention.calendar.TimeRange;
import java.util.UUID;

public class SpeakerDoubleBookingException extends RuntimeException {
    public SpeakerDoubleBookingException(UUID speakerUUID, TimeRange timeRange) {
        super(String.format("Speaker %s is already scheduled to be at another event at the given time (%s).", speakerUUID, timeRange));
    }
}

