package messaging;

import user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ConversationManager {

//    Handles sending messages
    private HashMap<UUID, ArrayList<Conversation>> mapUserConvo = new HashMap<UUID,
        ArrayList<Conversation>>();

    public void conversationCreator(String convName, ArrayList<UUID> usersWrite, ArrayList<UUID>
            usersRead, ArrayList<Message> convMessages){
        Conversation newConversation = new Conversation(convName, usersWrite, usersRead, convMessages);
    }

    public void messageCreator(UUID messageSender_id, String messageContent, Date messageTimestamp){
        Message newMessage = new Message(messageSender_id, messageContent, messageTimestamp);
    }

    public boolean addUserWithConversation(UUID userId, Conversation newConversation){
        if (mapUserConvo.keySet().contains(userId)){
            if (mapUserConvo.get(userId).contains(newConversation)){
                System.out.println("This conversation has already been added.");
                return false;
            }
            else{mapUserConvo.get(userId).add(newConversation);
            return true;}
        }
        else{
            ArrayList<Conversation> convoArray = new ArrayList<>();
            convoArray.add(newConversation);
            mapUserConvo.put(userId, convoArray);
            return true;
        }
    }

    public void removeUser(UUID userId){
        if (mapUserConvo.keySet().contains(userId)){
        mapUserConvo.remove(userId);
        }
    }

    public void sendMessage(Message message, Conversation conversation){
        // We can assume that a conversation has already been created here
        UUID userId = message.getSenderId();
        ArrayList<Conversation> arraylist = mapUserConvo.get(userId);
        for(Conversation convo: arraylist){
            if (convo == conversation){
                convo.addMessage(message);
                break;
            }
        }
    }

}
