package contact;

import java.util.Set;
import java.util.UUID;

public class ContactController {
    ContactManager hermes = new ContactManager();
    public void sendRequest(UUID userId, UUID potentialContact){
        Set<UUID> requestList = hermes.getRequests(potentialContact);
        Set<UUID> sentList = hermes.getSentRequests(userId);
        requestList.add(userId);
        sentList.add(potentialContact);
        hermes.setRequests(potentialContact, requestList);
        hermes.setSentRequests(userId, sentList);
    }

    public void deleteContacts(UUID userId, UUID extraContact){
        Set<UUID> contactsList = hermes.getContacts(userId);
        contactsList.remove(extraContact);
        hermes.setContacts(userId, contactsList);
    }

//    public Set<UUID> showContacts(){
//    }
//    ARE WE RETURNING USERS OR JUST THEIR IDS?
}
