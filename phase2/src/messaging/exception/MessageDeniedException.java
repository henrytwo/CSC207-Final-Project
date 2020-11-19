package messaging.exception;

import java.util.UUID;

public class MessageDeniedException extends RuntimeException {
    public MessageDeniedException(UUID executor, UUID receiver) {
        super(String.format("Cannot create conversation. %s is not on the contact list of %s.", executor, receiver));
    }
}
