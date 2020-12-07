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
     * Deletes the request a sender has sent to the recipient.
     *
     * @param senderUUID UUID of the request sender.
     * @param recipientUUID UUID of the recipient who has rejected the request from the sender.
     */
    private void deleteRequest(UUID senderUUID, UUID recipientUUID) {
        Set<UUID> senderSentList = contactManager.getSentRequests(senderUUID);
        senderSentList.remove(recipientUUID);
        contactManager.setSentRequests(senderUUID, senderSentList);

        Set<UUID> recipientRequestList = contactManager.getRequests(recipientUUID);
        recipientRequestList.remove(senderUUID);
        contactManager.setRequests(recipientUUID, recipientRequestList);
    }

    /**
     * Allows a user to accept a request from another user.
     *
     * @param userUUID             UUID of the user who is making the decision on accepting a request.
     * @param potentialContactUUID UUID of the user whose request is being considered.
     */
    public void acceptRequest(UUID userUUID, UUID potentialContactUUID) {
        if (showRequests(userUUID).contains(potentialContactUUID)) {
            Set<UUID> myContacts = showContacts(userUUID);
            Set<UUID> theirContacts = showContacts(potentialContactUUID);

            // Add to each other's contact list
            myContacts.add(potentialContactUUID);
            contactManager.setContacts(userUUID, myContacts);

            theirContacts.add(userUUID);
            contactManager.setContacts(potentialContactUUID, theirContacts);

            // Erase requests now that the connection is established
            deleteRequest(userUUID, potentialContactUUID);
            deleteRequest(potentialContactUUID, userUUID);
        } else {
            throw new GhostAcceptDeniedException(userUUID, potentialContactUUID);
        }
    }

    /**
     * Allows a user to reject a request from another user.
     *
     * @param userUUID             UUID of the user who is rejecting the request.
     * @param potentialContactUUID UUID of the user whose request is being rejected :(
     */
    public void rejectRequest(UUID userUUID, UUID potentialContactUUID) {
        if (showRequests(userUUID).contains(potentialContactUUID)) {
            deleteRequest(potentialContactUUID, userUUID);
        } else {
            throw new GhostAcceptDeniedException(userUUID, potentialContactUUID);
        }
    }

    /**
     * Delete a contact from the list of a users contacts.
     *
     * @param userUUID       UUID of the user deleting the contact.
     * @param targetUserUUID UUID of the user whose contact is being deleted.
     */
    public void deleteContacts(UUID userUUID, UUID targetUserUUID) {
        Set<UUID> myContactList = showContacts(userUUID);
        Set<UUID> theirContactList = showContacts(targetUserUUID);

        if (!showContacts(userUUID).contains(targetUserUUID)) {
            throw new GhostDeleteException(userUUID, targetUserUUID);
        }

        myContactList.remove(targetUserUUID);
        theirContactList.remove(userUUID);
        contactManager.setContacts(targetUserUUID, theirContactList);
        contactManager.setContacts(userUUID, myContactList);
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