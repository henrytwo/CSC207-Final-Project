package messaging;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Message implements Serializable {
    private String content;
//    private Set<Message> responses = new HashSet<>();
    private LocalDateTime timestamp;
    private UUID senderId;

    /**
     * Constructor for Message.
     *
     * @param messageSender_id The ID of the sender of the message
     * @param messageContent The content of the message to be sent.
     * @return Returns an instance of Message
     */
    public Message(UUID messageSender_id, String messageContent){
        content = messageContent;
        timestamp = LocalDateTime.now();
        senderId = messageSender_id;
    }

    /**
     *  returns the UUID of the sender of the message
     * @return UUID of the message sender
     */
    public UUID getSenderId(){
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s @ %s] %s\n", senderId, timestamp, content);
    }

    //    public void edit_message(String new_content, Date new_timestamp){
//        content = new_content;
//        timestamp = new_timestamp;
//    }
}
