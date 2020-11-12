package convention.exception;

import java.util.UUID;

public class NullBookingException extends RuntimeException {
    public NullBookingException(UUID eventUUID) {
        super(String.format("No booking for the event %s does not exist", eventUUID));
    }
}
