package messaging;

import java.util.*;

public class Message {
    private String content;
    private Set<Message> responses = new HashSet<>();
    private Date timestamp;
    private UUID senderId;

    public Message(UUID messageSender_id, String messageContent, Date messageTimestamp){
        content = messageContent;
        timestamp = messageTimestamp;
        senderId = messageSender_id;
    }

    public void add_responses(Message messageResponse){
        responses.add(messageResponse);
    }

    public UUID getSenderId(){
        return senderId;
    }

//    public void edit_message(String new_content, Date new_timestamp){
//        content = new_content;
//        timestamp = new_timestamp;
//    }
}
