package messaging;

import contact.ContactController;

import java.util.UUID;

public class ConversationController {
    ContactController linky = new ContactController();
    public boolean checkAccess(UUID sender, UUID receiver){
        return linky.showContacts(sender).contains(receiver);
    }
}
