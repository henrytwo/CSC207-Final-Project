package messaging;

import contact.ContactManager;
import messaging.exception.MessageDeniedException;
import user.UserManager;

import java.util.*;

/**
 * Operations on Conversations
 */
public class ConversationController {
    private final ConversationManager conversationManager;
    private final ContactManager contactManager;
    private final UserManager userManager;

    /**
     * Constructor for ConversationController
     *
     * @param contactManager      ContactManager object relevant to this conversation
     * @param conversationManager ConversationManager object for this conversation
     * @param userManager         User Manager object
     */
    public ConversationController(ContactManager contactManager, ConversationManager conversationManager, UserManager userManager) {
        this.contactManager = contactManager;
        this.conversationManager = conversationManager;
        this.userManager = userManager;
    }

    /**
     * Checks if the User have access to start a conversation with another User.
     * <p>
     * God users can message anyone without restriction.
     *
     * @param sender   UUID of the person who wants to send the message
     * @param receiver UUID of the person to whom the message is to be sent
     * @return true iff the receiver is in the friend list of sender
     */
    private boolean checkAccess(UUID sender, UUID receiver) {
        return userManager.getUserIsGod(sender) || (contactManager.getContacts(sender) != null && contactManager.getContacts(sender).contains(receiver));
    }

    /**
     * Sends a particular message to a specific chat. God users can bypass write-restrictions.
     *
     * @param executorUUID     the UUID of the sender of the message
     * @param messageContent   The content of the message to be sent
     * @param conversationUUID the conversation UUID of the conversation to which this message has to be added
     */
    public void sendMessage(UUID executorUUID, String messageContent, UUID conversationUUID) {
        conversationManager.sendMessage(executorUUID, messageContent, conversationUUID, userManager.getUserIsGod(executorUUID));
    }

    /**
     * Initiates a new Chat(Conversation) between 2 or more users.
     *
     * @param conversationName the name of the Chat to be initiated
     * @param executorUUID     the UUID of the user running this operation
     * @param otherUsers       the set of other users in this conversation
     * @param messageContent   The content of the initial message to be sent
     */
    public UUID initiateConversation(String conversationName, UUID executorUUID, Set<UUID> otherUsers, String messageContent) {
        Set<UUID> conversationUsers = new HashSet<>(otherUsers);
        conversationUsers.add(executorUUID);

        // Verify recipient is on the sender's contact list before creating conversation
        for (UUID otherUserUUID : otherUsers) {
            // You aren't on your own contact list but that's fine
            if (otherUserUUID != executorUUID && !checkAccess(executorUUID, otherUserUUID)) {
                throw new MessageDeniedException(executorUUID, otherUserUUID);
            }
        }

        return conversationManager.createConversation(conversationName, conversationUsers, conversationUsers, executorUUID, messageContent);
    }

    /**
     * Gets messages for a conversation a user has read access to. Throws NoReadAccessException if the user has no
     * read access.
     * <p>
     * God users can bypass read-restrictions.
     *
     * @param executorUUID     The ID of the User
     * @param conversationUUID The UUID of the Conversation for which the messages need to be seen
     * @return returns an list of Maps. Each Map stores information about a message in the conversation.
     */
    public List<Map<String, String>> getMessages(UUID executorUUID, UUID conversationUUID) {
        return conversationManager.getMessages(executorUUID, conversationUUID, userManager.getUserIsGod(executorUUID));
    }

    /**
     * Get the conversation name
     *
     * @param conversationUUID UUID of the conversation to operate on
     * @return Conversation name
     */
    public String getConversationName(UUID conversationUUID) {
        return conversationManager.getConversationName(conversationUUID);
    }

    /**
     * Get the set of UUID's of Conversation that the user is part of
     *
     * @param userUUID UUID of the user for which the set of conversation is required
     * @return set of UUID's of conversations that the user is part of
     */
    public Set<UUID> getConversationList(UUID userUUID) {
        // God users can see all conversations
        if (userManager.getUserIsGod(userUUID)) {
            return conversationManager.getConversationList();
        } else {
            //return conversationManager.getConversationList(userUUID);
            Set<UUID> conversationList = new HashSet<>();
            for (UUID conversationUUID : conversationManager.getConversationList(userUUID)) {
                if (!conversationManager.getUserArchiveConversation(conversationUUID).contains(userUUID)) {
                    conversationList.add(conversationUUID);
                }
            }
            return conversationList;
        }
    }

    /**
     * adds a specific user to a specific conversation
     *
     * @param conversationUUID UUID of the specific conversation
     * @param userUUID         UUID of the specific user
     */
    public void addUserToConversation(UUID conversationUUID, UUID userUUID) {
        conversationManager.addUser(userUUID, conversationUUID);
    }

    /**
     * removes a specific user from a specific conversation
     *
     * @param conversationUUID UUID of the specific conversation
     * @param userUUID         UUID of the specific user
     */
    public void removeUserFromConversation(UUID conversationUUID, UUID userUUID) {
        conversationManager.removeUser(userUUID, conversationUUID);
    }

    /**
     * gets the UUID's of all users in a specific conversation
     *
     * @param conversationUUID the UUID of the conversation in question
     * @return the list of users in this conversation
     */
    public Set<UUID> getUsersInConversation(UUID conversationUUID) {
        return conversationManager.getUsers(conversationUUID);
    }

    /**
     * archives a conversation for a specific user
     *
     * @param userUUID         user in question
     * @param conversationUUID conversation in question
     */
    public void userArchiveConversation(UUID userUUID, UUID conversationUUID) {
        conversationManager.userArchiveConversation(userUUID, conversationUUID);
    }

    /**
     * archives a conversation
     *
     * @param userUUID         user in question
     * @param conversationUUID conversation in question
     */
    public void userUnreadConversation(UUID userUUID, UUID conversationUUID) {
        conversationManager.userUnreadConversation(userUUID, conversationUUID);
    }

    /**
     * gets whether a specific conversation has been read by a specific user
     *
     * @param userUUID         user in question
     * @param conversationUUID conversation
     * @return true if that user has read that conversation, false otherwise
     */
    public boolean getUserHasRead(UUID userUUID, UUID conversationUUID) {
        return conversationManager.getUserHasRead(userUUID, conversationUUID);
    }


    /**
     * Deletes a specific message if the message was sent by that person or is being deleted by a god user
     *
     * @param conversationUUID conversation in question
     * @param userUUID         user in question
     * @param index            index of the message in question
     */
    public void deleteMessage(UUID conversationUUID, UUID userUUID, int index) {
        if (checkIfSender(conversationUUID, userUUID, index)) {
            conversationManager.userDeleteMessage(conversationUUID, index);
        }
    }

    /**
     * checks if a specific user is a god user or sender of a specific message
     *
     * @param conversationUUID conversation in question
     * @param userUUID         user in question
     * @param index            index of the message in question in the list of messages
     * @return true if that user is a god user or the sender of the message
     */
    public boolean checkIfSender(UUID conversationUUID, UUID userUUID, int index) {
        return conversationManager.getConversation(conversationUUID).getConversationMessages().get(index).getSenderUUID() == userUUID
                || userManager.getUserIsGod(userUUID);
    }

//    /**
//     * Adds user to the a specific chat
//     *
//     * @param signedInUserUUID         The userUUID of the user to be added to the Chat
//     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
//     */
    //public void addUser(UUID signedInUserUUID, UUID conversationUUID) {
    //    conversationManager.addUser(signedInUserUUID, conversationUUID);
    //}
    // Not currently in use, need to add admin chat users for this

//    /**
//     * Adds user to the a specific chat
//     *
//     * @param signedInUserUUID         The userUUID of the user to be added to the Chat
//     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
//     */
    //public void removeUser(UUID signedInUserUUID, UUID conversationUUID) {
    //    conversationManager.removeUser(signedInUserUUID, conversationUUID);
    //}
    // Not currently in use, need to add admin chat users for this
}
