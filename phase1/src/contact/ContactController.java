package contact;

import java.util.Set;
import java.util.UUID;

public class ContactController {
    ContactManager linker;

    public ContactController(ContactManager contactManager) {
        this.linker = contactManager;
    }


    /**
     * Sends a request to a potential contact of a user.
     * @param userId UUID of the request sender.
     * @param potentialContact UUID of the user receiving this request.
     */
    public void sendRequest(UUID userId, UUID potentialContact){
        Set<UUID> requestList = linker.getRequests(potentialContact);
        Set<UUID> sentList = linker.getSentRequests(userId);
        if(!requestList.contains(userId)){
            requestList.add(userId);
            sentList.add(potentialContact);
            linker.setRequests(potentialContact, requestList);
            linker.setSentRequests(userId, sentList);
        }
        else {
            System.out.println("Sorry, You have already sent a request to this user.");
        }
    }


    /**
     * Allows a user to accept a request from another user.
     * @param userId UUID of the user who is making the decision on accepting a request.
     * @param potentialContact UUID of the user whose request is being considered.
     */
    public void acceptRequests(UUID userId, UUID potentialContact){
        if(showRequests(userId).contains(potentialContact)){
            Set<UUID> contacts = showContacts(userId);
            contacts.add(potentialContact);
            Set<UUID> requestsList = linker.getRequests(userId);
            requestsList.remove(potentialContact);
            linker.setContacts(userId, contacts);
            linker.setRequests(userId, requestsList);
        }
    }

    /**
     * Delete a contact from the list of a users contacts.
     * @param userId UUID of the user deleting the contact.
     * @param extraContact UUID of the user whose contact is being deleted.
     */
    public void deleteContacts(UUID userId, UUID extraContact){
        Set<UUID> contactsList = linker.getContacts(userId);
        contactsList.remove(extraContact);
        linker.setContacts(userId, contactsList);
    }

    /**
     * Return a list of a user's contacts.
     * @param userId UUID of the user for whom the list of contacts is being requested.
     * @return set of UUIDs of the users contacts.
     */
    public Set<UUID> showContacts(UUID userId){
        return linker.getContacts(userId);
    }

    /**
     * Return a list of a user's received requests.
     * @param userId UUID of the user for whom the list of requests is being requested.
     * @return set of UUIDs of the users received requests.
     */
    public Set<UUID> showRequests(UUID userId){
        return linker.getRequests(userId);
    }
}
