package messaging;

import contact.ContactManager;
import user.UserManager;

import java.util.*;

/**
 * Operations on Conversations
 */
public class ConversationController {
    private ConversationManager conversationManager;
    private ContactManager contactManager;
    private UserManager userManager;

    /**
     * Constructor for ConversationController
     *
     * @param contactManager      ContactManager object relevant to this conversation
     * @param conversationManager ConversationManager onject for this conversation
     * @param userManager         User Manager object
     */
    public ConversationController(ContactManager contactManager, ConversationManager conversationManager, UserManager userManager) {
        this.contactManager = contactManager;
        this.conversationManager = conversationManager;
        this.userManager = userManager;
    }

    /**
     * Checks if the User have access to start a conversation with another User.
     *
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
     * @param executorUUID the Id of the sender of the message
     * @param messageContent   The content of the message to be sent
     * @param convId           the conversation Id of the conversation to which this message has to be added
     */
    public void sendMessage(UUID executorUUID, String messageContent, UUID convId) {
        conversationManager.sendMessage(executorUUID, messageContent, convId, userManager.getUserIsGod(executorUUID));
    }

    /**
     * Initiates a new Chat(Conversation) between 2 or more users.
     *
     * @param convName       the name of the Chat to be initiated
     * @param executorUUID   the UUID of the user running this operation
     * @param otherUsers     the set of other users in this conversation
     * @param messageContent The content of the initial message to be sent
     */
    public UUID initiateConversation(String convName, UUID executorUUID, Set<UUID> otherUsers, String messageContent) {
        Set<UUID> conversationUsers = new HashSet<>(otherUsers);
        conversationUsers.add(executorUUID);

        /**
         * TODO: Verify access before allowing conversation to be created
         */
        return conversationManager.createConversation(convName, conversationUsers, conversationUsers, executorUUID, messageContent);
    }

    /**
     * Gets messages for a conversation a user has read access to. Throws NoReadAccessException if the user has no
     * read access.
     *
     * God users can bypass read-restrictions.
     *
     * @param executorUUID         The ID of the User
     * @param conversationUUID The Id of the Conversation for which the messages need to be seen
     * @return returns an list of Hashmaps. Each Hashmap stores information about a message in the conversation.
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
     * Get the set of Id's of Conversation that the user is part of
     *
     * @param userId UUID of the user for which the set of conversation is required
     * @return set of UUID's of conversations that the user is part of
     */
    public Set<UUID> getConversationList(UUID userId) {
        // God users can see all conversations
        if (userManager.getUserIsGod(userId)) {
            return conversationManager.getConversationList();
        } else {
            return conversationManager.getConversationList(userId);
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






//
//    /**
//     * Adds user to the a specific chat
//     *
//     * @param signedInUserUUID         The userId of the user to be added to the Chat
//     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
//     */
    //public void addUser(UUID signedInUserUUID, UUID conversationUUID) {
    //    convoManager.addUser(signedInUserUUID, conversationUUID);
    //}
    // Not currently in use, need to add admin chat users for this

//    /**
//     * Adds user to the a specific chat
//     *
//     * @param signedInUserUUID         The userId of the user to be added to the Chat
//     * @param conversationUUID The UUID of the conversation/chat to which the user needs to be added
//     */
    //public void removeUser(UUID signedInUserUUID, UUID conversationUUID) {
    //    convoManager.removeUser(signedInUserUUID, conversationUUID);
    //}
    // Not currently in use, need to add admin chat users for this
}
