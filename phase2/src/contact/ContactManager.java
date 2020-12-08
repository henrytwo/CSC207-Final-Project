package contact;

import java.io.Serializable;
import java.util.*;

/**
 * Stores and performs actions on contacts
 */
public class ContactManager implements Serializable {
    private Map<UUID, Set<UUID>> contactsMap;

    private Map<UUID, Set<UUID>> requestsMap;

    private Map<UUID, Set<UUID>> sentRequestsMap;

    /**
     * Constructs ContactManager
     */
    public ContactManager() {
        super();
        this.contactsMap = new HashMap<UUID, Set<UUID>>();
        this.sentRequestsMap = new HashMap<UUID, Set<UUID>>();
        this.requestsMap = new HashMap<UUID, Set<UUID>>();
    }

    /**
     * returns the Contacts (friends) list of a particular user
     *
     * @param userUUID the userUUID of the person for whom we need a list of contacts
     * @return The Set of UUID's of the contacts
     */
    public Set<UUID> getContacts(UUID userUUID) {
        if (contactsMap.get(userUUID) == null) {
            contactsMap.put(userUUID, new HashSet<>());
        }
        return new HashSet<>(contactsMap.get(userUUID));
    }

    /**
     * Returns connection requests received by a user
     *
     * @param userUUID the userUUID of the person for whom we want to run the requests
     * @return The Set of UUID's of the users who have made a connection request
     */
    public Set<UUID> getRequests(UUID userUUID) {
        if (requestsMap.get(userUUID) == null) {
            requestsMap.put(userUUID, new HashSet<>());
        }
        return new HashSet<>(requestsMap.get(userUUID));
    }

    /**
     * Returns connection requests sent by a user
     *
     * @param userUUID the userUUID of the person for whom we need to run the requests
     * @return The Set of UUID's of the users to whom this particular user have made a connection request
     */
    public Set<UUID> getSentRequests(UUID userUUID) {
        if (sentRequestsMap.get(userUUID) == null) {
            sentRequestsMap.put(userUUID, new HashSet<>());
        }
        return new HashSet<>(sentRequestsMap.get(userUUID));
    }

    /**
     * Sets/updates the contact list of this particular User
     *
     * @param userUUID the userUUID of the person for whom we need to update the contacts set
     */
    public void setContacts(UUID userUUID, Set<UUID> contacts) {
        if (contactsMap.containsKey(userUUID)) {
            contactsMap.replace(userUUID, contacts);
        } else {
            contactsMap.putIfAbsent(userUUID, contacts);
        }
    }

    /**
     * Sets/updates the requests(received) list of this particular User
     *
     * @param userUUID the userUUID of the person for whom we need to update the requests(received) set
     * @param requests the set of requests received by this user
     */
    public void setRequests(UUID userUUID, Set<UUID> requests) {
        if (requestsMap.containsKey(userUUID)) {
            requestsMap.replace(userUUID, requests);
        } else {
            requestsMap.putIfAbsent(userUUID, requests);
        }
    }

    /**
     * Sets/updates the request(sent) list of this particular User
     *
     * @param userUUID     the userUUID of the person for whom we need to update the requests(sent) set
     * @param sentRequests the set of requests sent by this user
     */
    public void setSentRequests(UUID userUUID, Set<UUID> sentRequests) {
        if (sentRequestsMap.containsKey(userUUID)) {
            sentRequestsMap.replace(userUUID, sentRequests);
        } else {
            sentRequestsMap.putIfAbsent(userUUID, sentRequests);
        }
    }
}
