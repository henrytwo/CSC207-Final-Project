package user.exception;

import java.util.UUID;

/**
 * raises an error when a user that does not exist is called
 */
public class NullUserException extends RuntimeException {
    public NullUserException(UUID userUUID) {
        super(String.format("User %s does not exist.", userUUID));
    }
}
