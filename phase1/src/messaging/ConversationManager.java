package messaging;

import com.sun.xml.internal.bind.v2.model.core.NonElement;
import org.omg.PortableInterceptor.NON_EXISTENT;
import user.User;

import java.util.*;

public class ConversationManager {

//    Handles sending messages
    private HashMap<UUID, Set<UUID>> mapUserConvo = new HashMap<UUID,
        Set<UUID>>();
    private HashMap<UUID, Conversation> mapUUIDConvo = new HashMap<>();

    /**
     *  Creates an instance of Conversation
     * @param convName name of the conversation
     * @param usersWrite The set of users that have writing access to this conversation
     * @param usersRead The set of users that have reading access to this conversation
     * @param convMessages1 the first message to be sent to this conversation if it exists
     * @return A chat with the given specifications
     */
    public Conversation conversationCreator(String convName, Set<UUID> usersWrite, Set<UUID>
            usersRead, Message convMessages1){
        ArrayList<Message> convMessages = new ArrayList<>();
        convMessages.add(convMessages1);
        Conversation newConversation = new Conversation(convName, usersWrite, usersRead, convMessages);
        UUID convId = newConversation.getconvId();
        mapUUIDConvo.put(convId, newConversation);
        return newConversation;
    }

    /**
     *  Creates an instance of Message
     * @param messageSender_id the UUID of the message sender
     * @param messageTimestamp the date and time on which the message was sent
     * @param messageContent the content of message
     * @return The newly created message
     */
    public Message messageCreator(UUID messageSender_id, String messageContent, Date messageTimestamp){
        Message newMessage = new Message(messageSender_id, messageContent, messageTimestamp);
        return newMessage;
    }

    public HashMap<UUID, Conversation> getMapUUIDConvo() {
        return mapUUIDConvo;
    }

    public HashMap<UUID, Set<UUID>> getMapUserConvo() {
        return mapUserConvo;
    }

    /**
     *  Adds user to the a specific chat
     * @param userId The userId of the user to be added to the Chat
     * @param newConversationId The UUID of the conversation/chat to which the user needs to be added
     */
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
    /**
     *  Removes the user from the Conversation
     * @param userId The userId of the user to be removed from the chat
     */
    public void removeUser(UUID userId){
        if (mapUserConvo.keySet().contains(userId)){
        mapUserConvo.remove(userId);
        }
    }

    /**
     *  Sends a particular message to a specific chat
     * @param message the message to be sent
     * @param conversation the conversation to which this message has to be added
     */
    public void sendMessage(Message message, Conversation conversation){
        // We can assume that a conversation has already been created here
        conversation.addMessage(message);
    }
    /**
     *  returns a set of Conversations that a particular user is part of
     * @param userId the userid of the user for whom we want to know the set of Conversation lists
     */
    public Set getConversationlist(UUID userId){
        return mapUserConvo.get(userId);
    }




}
