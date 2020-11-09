package messaging;

import user.User;

import java.util.*;

public class ConversationManager {

//    Handles sending messages
    private HashMap<UUID, Set<UUID>> mapUserConvo = new HashMap<UUID,
        Set<UUID>>();

    public Conversation conversationCreator(String convName, Set<UUID> usersWrite, Set<UUID>
            usersRead, Message convMessages1){
        ArrayList<Message> convMessages = new ArrayList<>();
        convMessages.add(convMessages1);
        Conversation newConversation = new Conversation(convName, usersWrite, usersRead, convMessages);
        return newConversation;
    }

    public void messageCreator(UUID messageSender_id, String messageContent, Date messageTimestamp){
        Message newMessage = new Message(messageSender_id, messageContent, messageTimestamp);
    }

    public boolean addUserToConversation(UUID userId, UUID newConversationId){
        if (mapUserConvo.keySet().contains(userId)){
            if (mapUserConvo.get(userId).contains(newConversationId)){
                System.out.println("This conversation has already been added.");
                return false;
            }
            else{mapUserConvo.get(userId).add(newConversationId);
            return true;}
        }
        else{
            Set<UUID> convoSet = new HashSet<>();
            convoSet.add(newConversationId);
            mapUserConvo.put(userId, convoSet);
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
        conversation.addMessage(message);
    }

    public Set getConversationlist(UUID userId){
        return mapUserConvo.get(userId);
    }

}
