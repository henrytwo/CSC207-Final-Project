package messaging;

import contact.ContactController;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ConversationController {
    ContactController linky = new ContactController();
    ConversationManager convoManager = new ConversationManager();

    public boolean checkAccess(UUID sender, UUID receiver){
        return linky.showContacts(sender).contains(receiver);
    }

    public void sendMessage(Message message, String conversation_name, UUID receiver, UUID sender, String convName1,
                            Set<UUID> usersWrite, Set<UUID> usersRead, Message convMessages){
//        boolean condition = false;
//        Set<Conversation> conversation_set = convoManager.getConversationlist(receiver);
//        Conversation[] convo = conversation_set.toArray(new Conversation[conversation_set.size()]);
//        for(int i =0; i < conversation_set.size() ; i++){
//            if (convo[i].getConversationName() == conversation_name){
//                condition = true;
//                break;
//            }
//        }


        if (checkAccess(sender, receiver) && checkAccess(receiver, sender)){
            Conversation newConversation = convoManager.conversationCreator(convName1, usersWrite, usersRead, convMessages);
            convoManager.sendMessage(message, newConversation);
        }
    }

//    public void sendMessageSpecificOrganizer(UUID organizer, String conversation_name, )

}
