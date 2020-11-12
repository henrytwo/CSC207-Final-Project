package exception;

import java.util.UUID;

public class NullConversationException extends RuntimeException {
    public NullConversationException(UUID conversationUUID) {
        super(String.format("Conversation %s does not exist", conversationUUID));
    }
}
