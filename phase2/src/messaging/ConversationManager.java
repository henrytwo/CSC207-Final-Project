package messaging;

import messaging.exception.NoReadAccessException;
import messaging.exception.NoWriteAccessException;
import messaging.exception.NullConversationException;

import java.io.Serializable;
import java.util.*;

/**
 * Manages Conversation entities
 */
public class ConversationManager implements Serializable {

    // Note that the userUUIDtoConversationUUIDs HashMap is only there for efficiency reasons
    // the permissions are decided based on the read/write access to each conversation
    // (looping thru all the conversations is O(n), but accessing the HashMap is O(1) time)
    private final Map<UUID, Set<UUID>> userUUIDtoConversationUUIDs = new HashMap<>();
    private final Map<UUID, Conversation> conversationUUIDsToEntity = new HashMap<>();

    /**
     * Creates an instance of Conversation
     *
     * @param conversationName name of the conversation
     * @param usersWrite       The set of users that have writing access to this conversation
     * @param usersRead        The set of users that have reading access to this conversation
     * @param messageSender_id the UUID of the sender of the message
     * @param messageContent   The content of the message to be sent
     * @return A chat with the given specifications
     */
    public UUID createConversation(String conversationName, Set<UUID> usersWrite, Set<UUID> usersRead, UUID messageSender_id, String messageContent) {
        // Create an initial message that initiates a conversation
        Message initialMessage = new Message(messageSender_id, messageContent);
        // Adds the initial messages
        List<Message> messages = new ArrayList<>();
        messages.add(initialMessage);

        // Add conversation object to UUID -> Conversation map
        Conversation newConversation = new Conversation(conversationName, usersWrite, usersRead, messages);
        UUID conversationUUID = newConversation.getConversationUUID();
        conversationUUIDsToEntity.put(conversationUUID, newConversation);

        // This contains all the users in this conversation
        Set<UUID> conversationUsers = new HashSet<>();
        conversationUsers.addAll(usersWrite);
        conversationUsers.addAll(usersRead);

        // Add this conversation to the user's list
        for (UUID user : conversationUsers) {
            addConversationToUserList(user, conversationUUID);
        }

        return conversationUUID;
    }

    public Conversation getConversation(UUID conversationUUID) {
        if (conversationUUIDsToEntity.get(conversationUUID) == null) {
            throw new NullConversationException(conversationUUID);
        }

        return conversationUUIDsToEntity.get(conversationUUID);
    }

    private void addConversationToUserList(UUID userUUID, UUID conversationUUID) {
        userUUIDtoConversationUUIDs.computeIfAbsent(userUUID, k -> new HashSet<>());

        userUUIDtoConversationUUIDs.get(userUUID).add(conversationUUID);
    }

    private void removeConversationFromUserList(UUID userUUID, UUID conversationUUID) {
        if (userUUIDtoConversationUUIDs.get(userUUID) != null) {
            userUUIDtoConversationUUIDs.get(userUUID).remove(conversationUUID);
        }
    }

    /**
     * Deletes a conversation
     *
     * @param conversationUUID The UUID of the conversation/chat to be deleted
     */
    public void deleteConversation(UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);

        // Fetch all the users that are in this conversation
        Set<UUID> conversationUsers = new HashSet<>();
        conversationUsers.addAll(conversation.getReadAccessUsers());
        conversationUsers.addAll(conversation.getWriteAccessUsers());

        // Remove the conversation from all the user lists
        for (UUID userUUID : conversationUsers) {
            removeConversationFromUserList(userUUID, conversationUUID);
        }

        conversationUUIDsToEntity.remove(conversationUUID);
    }

    /**
     * Adds user to the a specific chat
     *
     * @param userUUID         The userUUID of the user to be added to the Chat
     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
     */
    public void addUser(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);

        // Update permission within conversation
        conversation.grantWriteAccess(userUUID);
        conversation.grantReadAccess(userUUID);

        // Add conversation to user's list
        addConversationToUserList(userUUID, conversationUUID);
    }

    /**
     * Returns the UUID of users who are part of a Conversation
     *
     * @param conversationUUID The UUID of a Conversation/Chat Group
     * @return set of users in the conversation
     */
    public Set<UUID> getUsers(UUID conversationUUID) {
        Set<UUID> usersInConversation = new HashSet<>();
        Set<UUID> allUsersUUID = userUUIDtoConversationUUIDs.keySet();
        for (UUID userUUID : allUsersUUID) {
            if (userUUIDtoConversationUUIDs.get(userUUID).contains(conversationUUID)) {
                usersInConversation.add(userUUID);
            }

        }
        return usersInConversation;
    }

    /**
     * Remove user from a specific chat
     *
     * @param userUUID         The userUUID of the user to be added to the Chat
     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be removed
     */
    public void removeUser(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);

        // Update permission within conversation
        conversation.removeUserFromWrite(userUUID);
        conversation.removeUserFromRead(userUUID);

        // Add conversation to user's list
        removeConversationFromUserList(userUUID, conversationUUID);
    }

    /**
     * returns a set of all conversations in the system
     */
    Set<UUID> getConversationList() {
        return new HashSet<>(conversationUUIDsToEntity.keySet());
    }

    /**
     * returns a set of Conversations that a particular user is part of
     *
     * @param userUUID the UUID of the user for whom we want to know the set of Conversation lists
     */
    Set<UUID> getConversationList(UUID userUUID) {
        if (userUUIDtoConversationUUIDs.get(userUUID) == null) {
            return new HashSet<>();
        }

        return new HashSet<>(userUUIDtoConversationUUIDs.get(userUUID));
    }

    /**
     * Gets the Conversation name
     *
     * @param conversationUUID UUID of the conversation to operate on
     * @return Conversation name
     */
    String getConversationName(UUID conversationUUID) {
        return getConversation(conversationUUID).getConversationName();
    }

    /**
     * Sends a particular message to a specific chat
     *
     * @param messageSender_id  the UUID of the sender of the message
     * @param messageContent    The content of the message to be sent
     * @param conversationUUID  the conversation UUID of the conversation to which this message has to be added
     * @param bypassRestriction whether to bypass write access restrictions
     */
    void sendMessage(UUID messageSender_id, String messageContent, UUID conversationUUID, boolean bypassRestriction) {
        Message message = new Message(messageSender_id, messageContent);
        Conversation conversation = getConversation(conversationUUID);

        UUID userUUID = message.getSenderUUID();

        if (conversation.getWriteAccessUsers().contains(userUUID) || bypassRestriction) {
            conversation.addMessage(message);
        } else {
            throw new NoWriteAccessException();
        }
    }

    /**
     * Gets messages for a conversation a user has read access to. Throws NoReadAccessException if the user has no
     * read access.
     *
     * @param userUUID          The ID of the User
     * @param conversationUUID  The UUID of the Conversation for which the messages need to be seen
     * @param bypassRestriction whether to bypass read access restrictions
     * @return returns an List of Maps. Each Map stores information about a message in the conversation.
     */
    List<Map<String, String>> getMessages(UUID userUUID, UUID conversationUUID, boolean bypassRestriction) {
        Conversation conversation = getConversation(conversationUUID);

        if (conversation.getReadAccessUsers().contains(userUUID) || bypassRestriction) {
            List<Map<String, String>> newList = new ArrayList<>();
            conversation.readConversation(userUUID);

            for (Message message : conversation.getConversationMessages()) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("sender", message.getSenderUUID().toString());
                messageMap.put("timestamp", message.getTimestamp().toString());
                messageMap.put("content", message.getContent());
                newList.add(messageMap);
            }
            return newList;
        } else {
            throw new NoReadAccessException();
        }
    }

    /**
     * Marks a conversation as unread for a specific user
     *
     * @param userUUID         user in question
     * @param conversationUUID conversation in question
     */
    public void userUnreadConversation(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);
        conversation.unreadConversation(userUUID);
    }

    /**
     * Archives a conversation for a specific user
     *
     * @param userUUID         user in question
     * @param conversationUUID conversation in question
     */
    public void userArchiveConversation(UUID userUUID, UUID conversationUUID) {
        Conversation conversation = getConversation(conversationUUID);
        conversation.archiveConversation(userUUID);
    }

    public void userDeleteMessage(UUID conversationUUID, int index) {
        getConversation(conversationUUID).deleteMessage(index);

    }

}