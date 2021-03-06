package messaging;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Conversation object. Contains messages and metadata such as conversation name, users who have read and write access, etc.
 */
public class Conversation implements Serializable {
    private String conversationName; // either group chat or 2 person chat
    private final List<Message> conversationMessages;
    private final Set<UUID> writeAccessUsers;
    private final Set<UUID> readAccessUsers;
    private final UUID conversationUUID;
    private Set<UUID> usersHaveRead = new HashSet<>();
    private Set<UUID> userArchivedUUIDs = new HashSet<>();

    /**
     * Constructor for Conversation
     *
     * @param name            name of the Conversation
     * @param usersWriteUUIDs The set of users that have write access to the chat/conversation
     * @param usersReadUUIDs  The set of users that have read access to the chat/conversation
     * @param messages        A list of all the messages in the Chat/conversation
     */
    public Conversation(String name, Set<UUID> usersWriteUUIDs, Set<UUID>
            usersReadUUIDs, List<Message> messages) {
        conversationName = name;
        conversationMessages = messages;
        writeAccessUsers = usersWriteUUIDs;
        readAccessUsers = usersReadUUIDs;
        conversationUUID = UUID.randomUUID();
    }

    /**
     * returns the UUID of this conversation
     *
     * @return the UUID associated with this conversation
     */
    public UUID getConversationUUID() {
        return conversationUUID;
    }

    /**
     * Adds the User UUID of the Person (user) to the list of Users that have write access
     *
     * @param userUUID UserId of the User
     */
    void grantWriteAccess(UUID userUUID) {
        writeAccessUsers.add(userUUID);
    }

    /**
     * Adds the User UUID of the Person (user) to the list of Users that have read access
     *
     * @param userUUID UserId of the User
     */
    void grantReadAccess(UUID userUUID) {
        readAccessUsers.add(userUUID);
    }

    /**
     * Remove the User UUID of the Person (user) to the list of Users that have write access
     *
     * @param userUUID UserId of the User
     */
    public void removeUserFromWrite(UUID userUUID) {
        writeAccessUsers.remove(userUUID);
    }

    /**
     * Remove the User UUID of the Person (user) to the list of Users that have read access
     *
     * @param userUUID UserId of the User
     */
    public void removeUserFromRead(UUID userUUID) {
        readAccessUsers.remove(userUUID);
    }

    /**
     * Returns a set of UUID's of users that have write access in this conversation
     *
     * @return Set of UUID's of users that have write access for this conversation
     */
    public Set<UUID> getWriteAccessUsers() {
        return writeAccessUsers;
    }

    /**
     * Returns a set of UUID's of users that have read access in this conversation
     *
     * @return Set of UUID's of users that have read access for this conversation
     */
    public Set<UUID> getReadAccessUsers() {
        return readAccessUsers;
    }

    /**
     * Returns a list of messages in this conversation
     *
     * @return list of messages in this conversation
     */
    public List<Message> getConversationMessages() {
        return conversationMessages;
    }

    /**
     * Changes the name of the Chat (Group or private) to the new name provided
     *
     * @param newName UserId of the User
     */
    public void changeConversationName(String newName) {
        conversationName = newName;
    }


    /**
     * Gets the name of the Conversation
     */
    public String getConversationName() {
        return conversationName;
    }


    /**
     * Adds message to the list of messages in this Conversation
     *
     * @param message Message to be added in the conversation
     */
    public void addMessage(Message message) {
        if (conversationMessages.contains(message)) {
            System.out.println("Message has already been added");
        } else {
            conversationMessages.add(message);
        }
    }

    /**
     * deletes a message
     *
     * @param index the index of the message being deleted
     */
    public void deleteMessage(int index) {
        conversationMessages.remove(index);
    }

    /**
     * archives a conversation for a specific user
     *
     * @param userUUID the user archiving the conversation
     */
    public void archiveConversation(UUID userUUID) {
        userArchivedUUIDs.add(userUUID);
    }

    /**
     * resets the list of people who have archived this conversation
     */
    public void resetUserArchivedUUIDs() {
        userArchivedUUIDs = new HashSet<>();
    }

    /**
     * gets the list of people who have archived this conversation
     *
     * @return the list of people who have archived this conversation
     */
    public Set<UUID> getUserArchivedUUIDs() {
        return userArchivedUUIDs;
    }

    /**
     * Marks a conversation for a specific user
     *
     * @param userUUID the user having read this conversation
     */
    public void readConversation(UUID userUUID) {
        usersHaveRead.add(userUUID);
    }

    /**
     * removes a user form the list of users who have read this conversation
     *
     * @param userUUID the user in question
     */
    public void unreadConversation(UUID userUUID) {
        usersHaveRead.remove(userUUID);
    }

    /**
     * resets the set of users who have read this conversation
     */
    public void resetUsersHaveRead() {
        usersHaveRead = new HashSet<>();
    }

    /**
     * gets the list of users who have read this conversation
     *
     * @return the list of users who have read this conversation
     */
    public Set<UUID> getUsersHaveRead() {
        return usersHaveRead;
    }

    /**
     * gets whether a specific user has read this conversation
     *
     * @param userUUID the user in question
     * @return true if the user in question has read this conversation
     */
    public boolean getUserHasRead(UUID userUUID) {
        return usersHaveRead.contains(userUUID);
    }
}