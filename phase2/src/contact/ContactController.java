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
     * @param userUUID         UUID of the request sender.
     * @param potentialContact UUID of the user receiving this request.
     */
    public void sendRequest(UUID userUUID, UUID potentialContact) {
        Set<UUID> requestList = contactManager.getRequests(potentialContact);
        Set<UUID> sentList = contactManager.getSentRequests(userUUID);
        if (!requestList.contains(userUUID)) {
            requestList.add(userUUID);
            sentList.add(potentialContact);
            contactManager.setRequests(potentialContact, requestList);
            contactManager.setSentRequests(userUUID, sentList);
        } else {
            throw new RequestDeniedException(userUUID, potentialContact);
        }
    }


    /**
     * Allows a user to accept a request from another user.
     *
     * @param userUUID         UUID of the user who is making the decision on accepting a request.
     * @param potentialContact UUID of the user whose request is being considered.
     */
    public void acceptRequests(UUID userUUID, UUID potentialContact) {
        if (showRequests(userUUID).contains(potentialContact)) {
            Set<UUID> contacts = showContacts(userUUID);
            Set<UUID> contacts2 = showContacts(potentialContact);
            contacts.add(potentialContact);
            contacts2.add(userUUID);
            Set<UUID> requestsList = contactManager.getRequests(userUUID);
            requestsList.remove(potentialContact);
            contactManager.setContacts(userUUID, contacts);
            contactManager.setContacts(potentialContact, contacts2);
            contactManager.setRequests(userUUID, requestsList);
        } else {
            throw new GhostAcceptDeniedException(userUUID, potentialContact);
        }
    }

    /**
     * Allows a user to reject a request from another user.
     *
     * @param userUUID         UUID of the user who is rejecting the request.
     * @param potentialContact UUID of the user whose request is being rejected :(
     */
    public void rejectRequests(UUID userUUID, UUID potentialContact) {
        if (showRequests(userUUID).contains(potentialContact)) {
            Set<UUID> requestsList = contactManager.getRequests(userUUID);
            requestsList.remove(potentialContact);
            contactManager.setRequests(userUUID, requestsList);
        } else {
            throw new GhostAcceptDeniedException(userUUID, potentialContact);
        }
    }

    /**
     * Delete a contact from the list of a users contacts.
     *
     * @param userUUID     UUID of the user deleting the contact.
     * @param extraContact UUID of the user whose contact is being deleted.
     */
    public void deleteContacts(UUID userUUID, UUID extraContact) {
        Set<UUID> contactsList = showContacts(userUUID);
        Set<UUID> contactsList2 = showContacts(extraContact);
        if (!showContacts(userUUID).contains(extraContact)) {
            throw new GhostDeleteException(userUUID, extraContact);
        }
        contactsList.remove(extraContact);
        contactsList2.remove(userUUID);
        contactManager.setContacts(extraContact, contactsList2);
        contactManager.setContacts(userUUID, contactsList);
    }

    /**
     * Return a list of a user's contacts.
     *
     * @param userUUID UUID of the user for whom the list of contacts is being requested.
     * @return set of UUIDs of the users contacts.
     */
    public Set<UUID> showContacts(UUID userUUID) {
        return contactManager.getContacts(userUUID);
    }

    /**
     * Return a list of a user's received requests.
     *
     * @param userUUID UUID of the user for whom the list of requests is being requested.
     * @return set of UUIDs of the users received requests.
     */
    public Set<UUID> showRequests(UUID userUUID) {
        return contactManager.getRequests(userUUID);
    }

    /**
     * Return a list of a user's sent requests.
     *
     * @param userUUID UUID of the user for whom the list of requests is being requested.
     * @return set of UUIDs of the users who received requests from user with UUID userUUID.
     */
    public Set<UUID> showSentRequests(UUID userUUID) {
        return contactManager.getSentRequests(userUUID);
    }
}