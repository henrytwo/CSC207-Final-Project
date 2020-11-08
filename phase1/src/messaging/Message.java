package messaging;

import java.util.Date;
import java.util.UUID;
import java.util.ArrayList;

public class Message {
    private String content;
    private ArrayList<Message> responses = new ArrayList<>();
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
