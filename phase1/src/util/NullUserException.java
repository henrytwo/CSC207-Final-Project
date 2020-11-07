package util;

import java.util.UUID;

public class NullUserException extends RuntimeException {
    public NullUserException(UUID userUUID) {
        super(String.format("User %s does not exist", userUUID));
    }
}
