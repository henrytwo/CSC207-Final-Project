package contact.exception;

import java.util.UUID;

/**
 *
 */
public class GhostDeleteException extends RuntimeException {
    public GhostDeleteException(UUID userUUID, UUID potential) {
        super(String.format("Cannot delete a contact that does not exist. %s is not on the contact list of %s.", potential, userUUID));
    }
}
