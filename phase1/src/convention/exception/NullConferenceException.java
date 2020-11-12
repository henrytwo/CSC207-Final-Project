package convention.exception;

import java.util.UUID;

public class NullConferenceException extends RuntimeException {
    public NullConferenceException(UUID conferenceUUID) {
        super(String.format("Conference %s does not exist", conferenceUUID));
    }
}
