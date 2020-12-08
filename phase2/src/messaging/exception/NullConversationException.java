package messaging.exception;

import java.util.UUID;

/**
 * raises an error if a conversation does not exist
 */
public class NullConversationException extends RuntimeException {
    public NullConversationException(UUID conversationUUID) {
        super(String.format("Conversation %s does not exist", conversationUUID));
    }
}
