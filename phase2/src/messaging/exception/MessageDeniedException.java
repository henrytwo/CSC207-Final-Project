package messaging.exception;

import java.util.UUID;

/**
 * raises an error if the sender is trying to create a conversation with someone outside of their contacts
 */
public class MessageDeniedException extends RuntimeException {
    public MessageDeniedException(UUID executor, UUID receiver) {
        super(String.format("Cannot create conversation. %s is not on the contact list of %s.", executor, receiver));
    }
}
