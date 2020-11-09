package messaging;

import user.User;

import java.util.*;

public class ConversationManager {

//    Handles sending messages
    private HashMap<UUID, Set<Conversation>> mapUserConvo = new HashMap<UUID,
        Set<Conversation>>();

    public void conversationCreator(String convName, Set<UUID> usersWrite, Set<UUID>
            usersRead, Set<Message> convMessages){
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
            Set<Conversation> convoArray = new HashSet<>();
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

    public void sendMessage(Message message, String conversation_name){
        // We can assume that a conversation has already been created here
        UUID userId = message.getSenderId();
        Set<Conversation> arraylist = mapUserConvo.get(userId);
        for(Conversation convo: arraylist){
            if (convo.getConversationName() == conversation_name){
                convo.addMessage(message);
                break;
            }
        }
    }

}
