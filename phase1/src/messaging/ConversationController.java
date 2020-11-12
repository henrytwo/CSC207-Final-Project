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

    public void sendMessage(Message message, UUID convId){
//        if (checkAccess(sender, receiver) && checkAccess(receiver, sender)){
//            Conversation newConversation = convoManager.conversationCreator(convName1, usersWrite, usersRead, convMessages);
//            convoManager.sendMessage(message, newConversation);
        if (convoManager.getMapUUIDConvo().keySet().contains(convId)) {
            UUID userId = message.getSenderId();
            Conversation conversationObject = convoManager.getMapUUIDConvo().get(convId);
            Set<Conversation> listofConversation = convoManager.getConversationlist(userId);
            if (listofConversation.contains(conversationObject)) {
                conversationObject.addMessage(message);
            } else {
                System.out.println("You are not allowed to message in this chat.");
            }
        }
        else{System.out.println("There is no conversation with this Id.");}

        }

    public Conversation initiateConversation(String convName, Set<UUID> usersWrite, Set<UUID>
            usersRead, Message convMessages1){
        return convoManager.conversationCreator(convName, usersWrite, usersRead, convMessages1);

    }

}
