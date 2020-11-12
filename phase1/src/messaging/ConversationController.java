package messaging;

import contact.ContactController;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ConversationController {
    ContactController linky = new ContactController();
    ConversationManager convoManager = new ConversationManager();

    /**
     *  Checks if the User have access to send message to another User
     * @param sender UUID of the person who wants to send the message
     * @param receiver UUID of the person to whom the message is to be sent
     * @return true iff the receiver is in the friend list of sender
     */
    public boolean checkAccess(UUID sender, UUID receiver){
        return linky.showContacts(sender).contains(receiver);
    }

    /**
     *  Sends a particular message to a specific chat
     * @param message the message to be sent
     * @param convId the conversation Id of the conversation to which this message has to be added
     */
    public void sendMessage(Message message, UUID convId){
        convoManager.sendMessage(message, convId);
    }

    /**
     *  Initiates a new Chat(Conversation) between 2 or more users
     * @param convName the name of the Chat to be initiated
     * @param usersWrite the set of Users that have the permission to write messages in this chat
     * @param usersRead the set of Users that have the permission to read messages in this chat
     * @param convMessages1 the message that initiated the need for this Chat to be created
     */
    UUID initiateConversationOrganizer(String convName, Set<UUID> usersWrite, Set<UUID>
        usersRead, Message convMessages1){
        return convoManager.conversationCreator(convName, usersWrite, usersRead, convMessages1);
    }

    /**
     *  Adds user to the a specific chat
     * @param userId The userId of the user to be added to the Chat
     * @param newConversationId The UUID of the conversation/chat to which the user needs to be added
     */
    public boolean addUserToConvo(UUID userId, UUID newConversationId){
        return convoManager.addUserToConversation(userId, newConversationId);
    }

    /**
     *  Removes the user from the Conversation
     * @param userId The userId of the user to be removed from the chat
     */
    public void removeUser(UUID userId){
        convoManager.removeUser(userId);
    }

}
