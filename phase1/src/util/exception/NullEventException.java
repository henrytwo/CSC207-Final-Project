package util.exception;

import java.util.UUID;

public class NullEventException extends RuntimeException {
    public NullEventException(UUID conferenceUUID) {
        super(String.format("Event %s does not exist", conferenceUUID));
    }
}
