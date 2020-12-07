package messaging;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Contains information about individual messages, such as the sender, timestamp, etc.
 */
class Message implements Serializable {
    private final String content;
    private final LocalDateTime timestamp;
    private final UUID senderUUID;

    /**
     * Constructor for Message.
     *
     * @param senderUUID     The UUID of the sender of the message
     * @param messageContent The content of the message to be sent.
     */
    Message(UUID senderUUID, String messageContent) {
        content = messageContent;
        timestamp = LocalDateTime.now();
        this.senderUUID = senderUUID;
    }

    /**
     * returns the UUID of the sender of the message
     *
     * @return UUID of the message sender
     */
    UUID getSenderUUID() {
        return senderUUID;
    }

    /**
     * Getter for the content of the message
     *
     * @return the content of the message
     */
    String getContent() {
        return content;
    }

    /**
     * Getter for the date and time of the creation of message
     *
     * @return the date and time that the message was sent by the sender
     */
    LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * The toString method for the message
     */
    @Override
    public String toString() {
        return String.format("[%s @ %s] %s\n", senderUUID, timestamp, content);
    }
}
