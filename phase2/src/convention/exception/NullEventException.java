package convention.exception;

import java.util.UUID;

/**
 * Thrown when an invalid event UUID is used
 */
public class NullEventException extends RuntimeException {
    public NullEventException(UUID conferenceUUID) {
        super(String.format("Event %s does not exist", conferenceUUID));
    }
}
