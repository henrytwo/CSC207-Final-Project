package contact;

import contact.exception.GhostAcceptDeniedException;
import contact.exception.GhostDeleteException;
import contact.exception.RequestDeniedException;

import java.util.Set;
import java.util.UUID;

/**
 * Controls operations on user contacts
 */
public class ContactController {
    private contact.ContactManager contactManager;

    /**
     * Construct contact controller
     *
     * @param contactManager
     */
    public ContactController(contact.ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    /**
     * Sends a request to a potential contact of a user.
     *
     * @param userId           UUID of the request sender.
     * @param potentialContact UUID of the user receiving this request.
     */
    public void sendRequest(UUID userId, UUID potentialContact) {
        Set<UUID> requestList = contactManager.getRequests(potentialContact);
        Set<UUID> sentList = contactManager.getSentRequests(userId);
        if (!requestList.contains(userId)) {
            requestList.add(userId);
            sentList.add(potentialContact);
            contactManager.setRequests(potentialContact, requestList);
            contactManager.setSentRequests(userId, sentList);
        } else {
            throw new RequestDeniedException(userId, potentialContact);
        }
    }


    /**
     * Allows a user to accept a request from another user.
     *
     * @param userId           UUID of the user who is making the decision on accepting a request.
     * @param potentialContact UUID of the user whose request is being considered.
     */
    public void acceptRequests(UUID userId, UUID potentialContact) {
        if (showRequests(userId).contains(potentialContact)) {
            Set<UUID> contacts = showContacts(userId);
            Set<UUID> contacts2 = showContacts(potentialContact);
            contacts.add(potentialContact);
            contacts2.add(userId);
            Set<UUID> requestsList = contactManager.getRequests(userId);
            requestsList.remove(potentialContact);
            contactManager.setContacts(userId, contacts);
            contactManager.setContacts(potentialContact, contacts2);
            contactManager.setRequests(userId, requestsList);
        } else {
            throw new GhostAcceptDeniedException(userId, potentialContact);
        }
    }

    /**
     * Allows a user to reject a request from another user.
     * @param userId UUID of the user who is rejecting the request.
     * @param potentialContact UUID of the user whose request is being rejected :(
     */
    public void rejectRequests(UUID userId, UUID potentialContact) {
        if (showRequests(userId).contains(potentialContact)) {
            Set<UUID> requestsList = contactManager.getRequests(userId);
            requestsList.remove(potentialContact);
            contactManager.setRequests(userId, requestsList);
        } else {
            throw new GhostAcceptDeniedException(userId, potentialContact);
        }
    }

    /**
     * Delete a contact from the list of a users contacts.
     *
     * @param userId       UUID of the user deleting the contact.
     * @param extraContact UUID of the user whose contact is being deleted.
     */
    public void deleteContacts(UUID userId, UUID extraContact) {
        Set<UUID> contactsList = showContacts(userId);
        Set<UUID> contactsList2 = showContacts(extraContact);
        if (!showContacts(userId).contains(extraContact)) {
            throw new GhostDeleteException(userId, extraContact);
        }
        contactsList.remove(extraContact);
        contactsList2.remove(userId);
        contactManager.setContacts(extraContact, contactsList2);
        contactManager.setContacts(userId, contactsList);
    }

    /**
     * Return a list of a user's contacts.
     *
     * @param userId UUID of the user for whom the list of contacts is being requested.
     * @return set of UUIDs of the users contacts.
     */
    public Set<UUID> showContacts(UUID userId) {
        return contactManager.getContacts(userId);
    }

    /**
     * Return a list of a user's received requests.
     *
     * @param userId UUID of the user for whom the list of requests is being requested.
     * @return set of UUIDs of the users received requests.
     */
    public Set<UUID> showRequests(UUID userId) {
        return contactManager.getRequests(userId);
    }

    /**
     * Return a list of a user's sent requests.
     *
     * @param userId UUID of the user for whom the list of requests is being requested.
     * @return set of UUIDs of the users who received requests from user with UUID userId.
     */
    public Set<UUID> showSentRequests(UUID userId) {
        return contactManager.getSentRequests(userId);
    }
}