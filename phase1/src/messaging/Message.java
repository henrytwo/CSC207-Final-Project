package messaging;

import java.util.Date;
import java.util.UUID;
import java.util.ArrayList;

public class Message {
    private String content;
    private ArrayList<Message> responses = new ArrayList<>();
    private Date timestamp;
    private UUID sender_id;

    public Message(UUID message_sender_id, String message_content, Date message_timestamp){
        content = message_content;
        timestamp = message_timestamp;
        sender_id = message_sender_id;
    }

    public void add_responses(Message message_response){
        responses.add(message_response);
    }

//    public void edit_message(String new_content, Date new_timestamp){
//        content = new_content;
//        timestamp = new_timestamp;
//    }
}
