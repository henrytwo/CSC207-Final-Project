package contact.exception;

import java.util.UUID;

public class GhostAcceptDeniedException extends RuntimeException {
    public GhostAcceptDeniedException(UUID userUUID, UUID potential) {
        super(String.format("Cannot accept a request that does not exist. %s is not on the request list of %s.", potential, userUUID));
    }
}
