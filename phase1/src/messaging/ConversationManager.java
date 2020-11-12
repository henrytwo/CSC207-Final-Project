package messaging;

import messaging.exception.NoReadAccessException;
import messaging.exception.NoWriteAccessException;
import messaging.exception.NullConversationException;

import java.util.*;

public class ConversationManager {

    // Note that the mapUserConvo hashmap is only there for efficiency reasons
    // the permissions are decided based on the read/write access to each conversation
    // (looping thru all the convos is O(n), but accessing the hashmap is O(1) time)
    private HashMap<UUID, Set<UUID>> mapUserConvo = new HashMap<>();
    private HashMap<UUID, Conversation> mapUUIDConvo = new HashMap<>();

    /**
     * Creates an instance of Conversation
     *
     * @param convName       name of the conversation
     * @param usersWrite     The set of users that have writing access to this conversation
     * @param usersRead      The set of users that have reading access to this conversation
     * @param initialMessage the first message to be sent to this conversation if it exists
     * @return A chat with the given specifications
     */
    public UUID createConversation(String convName, Set<UUID> usersWrite, Set<UUID> usersRead, Message initialMessage) {

        // Adds the initial messages
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(initialMessage);

        // Add conversation object to UUID -> Convo map
        Conversation newConversation = new Conversation(convName, usersWrite, usersRead, messages);
        UUID conversationUUID = newConversation.getconvId();
        mapUUIDConvo.put(conversationUUID, newConversation);

        // This contains all the users in this convo
        Set<UUID> conversationUsers = new HashSet<>();
        conversationUsers.addAll(usersWrite);
        conversationUsers.addAll(usersRead);

        // Add this conversation to the user's list
        for (UUID user : conversationUsers) {
            addConvoToUserList(user, conversationUUID);
        }

        return conversationUUID;
    }

    private Conversation getConversation(UUID conversationUUID) {
        if (mapUUIDConvo.get(conversationUUID) == null) {
            throw new NullConversationException(conversationUUID);
        }

        return mapUUIDConvo.get(conversationUUID);
    }

    private void addConvoToUserList(UUID userUUID, UUID conversationUUID) {
        if (mapUserConvo.get(userUUID) == null) {
            mapUserConvo.put(userUUID, new HashSet<>());
        }

        mapUserConvo.get(userUUID).add(conversationUUID);
    }

    private void removeConvoFromUserList(UUID userUUID, UUID conversationUUID) {
        if (mapUserConvo.get(userUUID) != null) {
            mapUserConvo.get(userUUID).remove(conversationUUID);
        }
    }

    /**
     * Adds user to the a specific chat
     *
     * @param userUUID         The userId of the user to be added to the Chat
     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
     */
    public void addUser(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);

        // Update permission within conversation
        conversation.addUserToWrite(userUUID);
        conversation.addUserToRead(userUUID);

        // Add conversation to user's list
        addConvoToUserList(userUUID, conversationUUID);
    }

    /**
     * Remove user from a specific chat
     *
     * @param userUUID         The userId of the user to be added to the Chat
     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
     */
    public void removeUser(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);

        // Update permission within conversation
        conversation.removeUserFromWrite(userUUID);
        conversation.removeUserFromRead(userUUID);

        // Add conversation to user's list
        removeConvoFromUserList(userUUID, conversationUUID);
    }

    /**
     * returns a set of Conversations that a particular user is part of
     *
     * @param userId the userid of the user for whom we want to know the set of Conversation lists
     */
    public Set<UUID> getConversationlist(UUID userId) {
        return mapUserConvo.get(userId);
    }

    /**
     * Sends a particular message to a specific chat
     *
     * @param message the message to be sent
     * @param convId  the conversation Id of the conversation to which this message has to be added
     */
    public void sendMessage(Message message, UUID convId) {
        Conversation conversation = getConversation(convId);

        UUID userId = message.getSenderId();

        if (conversation.getWriteAccessUsers().contains(userId)) {
            conversation.addMessage(message);
        } else {
            throw new NoWriteAccessException();
        }
    }

    /**
     * Gets messages for a conversation a user has read access to. Throws NoReadAccessException if the user has no
     * read access.
     *
     * @param userUUID
     * @param conversationUUID
     * @return
     */
    public ArrayList<Message> getMessages(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);

        if (conversation.getReadAccessUsers().contains(userUUID)) {
            return conversation.getConversationMessages();
        } else {
            throw new NoReadAccessException();
        }
    }
}
