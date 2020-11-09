package contact;

import java.util.Set;
import java.util.UUID;

public class ContactController {
    ContactManager linker = new ContactManager();
    public void sendRequest(UUID userId, UUID potentialContact){
        Set<UUID> requestList = linker.getRequests(potentialContact);
        Set<UUID> sentList = linker.getSentRequests(userId);
        requestList.add(userId);
        sentList.add(potentialContact);
        linker.setRequests(potentialContact, requestList);
        linker.setSentRequests(userId, sentList);
    }

    public void deleteContacts(UUID userId, UUID extraContact){
        Set<UUID> contactsList = linker.getContacts(userId);
        contactsList.remove(extraContact);
        linker.setContacts(userId, contactsList);
    }

    public Set<UUID> showContacts(UUID userId){
        return linker.getContacts(userId);
    }
}
