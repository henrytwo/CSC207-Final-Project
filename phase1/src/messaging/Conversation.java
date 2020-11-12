package messaging;

import user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Conversation {
    private String conversationName; // either group chat or 2 person chat
    private ArrayList<Message> conversationMessages = new ArrayList<>();
    private Set<UUID> writeAccessUsers = new HashSet<>();
    private Set<UUID> readAccessUsers = new HashSet<>();
    private UUID convoId;

    public Conversation(String convName, Set<UUID> usersWrite, Set<UUID>
            usersRead, ArrayList<Message> convMessages){
        conversationName = convName;
        conversationMessages = convMessages;
        writeAccessUsers = usersWrite;
        readAccessUsers = usersRead;
        convoId = UUID.randomUUID();
    }

    /**
     *  returns the UUID of this conversation
     * @return the UUID associated with this conversation
     */
    public UUID getconvId(){
        return convoId;
    }

    /**
     *  Adds the User Id of the Person (user) to the list of Users that have write access
     * @param userUUID UserId of the User
     */
    public void addUserToWrite(UUID userUUID){
        writeAccessUsers.add(userUUID);
    }

    /**
     *  Adds the User Id of the Person (user) to the list of Users that have read access
     * @param userUUID UserId of the User
     */
    public void addUsersToRead(UUID userUUID){
        readAccessUsers.add(userUUID);
    }

    public Set<UUID> getWriteAccessUsers() {
        return writeAccessUsers;
    }

    /**
     *  Changes the name of the Chat (Group or private) to the new name provided
     * @param newName UserId of the User
     */
    public void changeConversationName(String newName){
        conversationName = newName;
    }


    /**
     *  Gets the name of the Conversation
     */
    public String getConversationName(){
        return conversationName;
    }

    /**
     *  Gets the name of the Conversation
     * @param conversationId The UUID associated with this Conversation
     */
    public String getConversationName(UUID conversationId){
        return conversationName;
    }

    /**
     *  Adds message to the list of messages in this Conversation
     * @param message Message to be added in the conversation
     * @return true iff message was sent successfully
     */
    public boolean addMessage(Message message){
        if (conversationMessages.contains(message)){
            System.out.println("Message has already been added");
            return false;
        }
        else{
            conversationMessages.add(message);
            return true;
        }
    }

    /**
     *  Deletes a specific message in the Chat
     * @param message Message to be deleted
     * @return true iff message was deleted successfully
     */
    public boolean deleteMessage(Message message){
        if (conversationMessages.contains(message)){
            conversationMessages.remove(message);
            return true;
        }
        else{
            return false;
        }
    }

}