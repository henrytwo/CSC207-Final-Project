package convention.exception;

import java.util.UUID;

/**
 * Thrown when an invalid conference UUID is used
 */
public class NullConferenceException extends RuntimeException {
    public NullConferenceException(UUID conferenceUUID) {
        super(String.format("Conference %s does not exist", conferenceUUID));
    }
}
