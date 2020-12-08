package convention.exception;

import java.util.UUID;

/**
 * Thrown when an invalid user UUID is used
 */
public class NullUserException extends RuntimeException {
    public NullUserException(UUID userUUID) {
        super(String.format("User %s does not exist", userUUID));
    }
}
