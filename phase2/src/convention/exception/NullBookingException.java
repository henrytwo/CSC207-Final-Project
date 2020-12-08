package convention.exception;

import java.util.UUID;

/**
 * Thrown when an invalid event UUID is used
 */
public class NullBookingException extends RuntimeException {
    public NullBookingException(UUID eventUUID) {
        super(String.format("No booking for the events %s does not exist", eventUUID));
    }
}
