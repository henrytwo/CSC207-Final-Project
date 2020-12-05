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
     * @param user_id the userId of the person for whom we need a list of contacts
     * @return The Set of UUID's of the contacts
     */
    public Set<UUID> getContacts(UUID user_id) {
        if (contactsMap.get(user_id) == null) {
            contactsMap.put(user_id, new HashSet<>());
        }
        return new HashSet<>(contactsMap.get(user_id));
    }

    /**
     * Returns connection requests received by a user
     *
     * @param user_id the userId of the person for whom we want to run the requests
     * @return The Set of UUID's of the users who have made a connection request
     */
    public Set<UUID> getRequests(UUID user_id) {
        if (requestsMap.get(user_id) == null) {
            requestsMap.put(user_id, new HashSet<>());
        }
        return new HashSet<>(requestsMap.get(user_id));
    }

    /**
     * Returns connection requests sent by a user
     *
     * @param user_id the userId of the person for whom we need to run the requests
     * @return The Set of UUID's of the users to whom this particular user have made a connection request
     */
    public Set<UUID> getSentRequests(UUID user_id) {
        if (sentRequestsMap.get(user_id) == null) {
            sentRequestsMap.put(user_id, new HashSet<>());
        }
        return new HashSet<>(sentRequestsMap.get(user_id));
    }

    /**
     * Sets/updates the contact list of this particular User
     *
     * @param user_id the userId of the person for whom we need to update the contacts set
     */
    public void setContacts(UUID user_id, Set<UUID> contacts) {
        if (contactsMap.containsKey(user_id)) {
            contactsMap.replace(user_id, contacts);
        } else {
            contactsMap.putIfAbsent(user_id, contacts);
        }
    }

    /**
     * Sets/updates the requests(received) list of this particular User
     *
     * @param user_id  the userId of the person for whom we need to update the requests(received) set
     * @param requests the set of requests received by this user
     */
    public void setRequests(UUID user_id, Set<UUID> requests) {
        if (requestsMap.containsKey(user_id)) {
            requestsMap.replace(user_id, requests);
        } else {
            requestsMap.putIfAbsent(user_id, requests);
        }
    }

    /**
     * Sets/updates the request(sent) list of this particular User
     *
     * @param user_id      the userId of the person for whom we need to update the requests(sent) set
     * @param sentrequests the set of requests sent by this user
     */
    public void setSentRequests(UUID user_id, Set<UUID> sentrequests) {
        if (sentRequestsMap.containsKey(user_id)) {
            sentRequestsMap.replace(user_id, sentrequests);
        } else {
            sentRequestsMap.putIfAbsent(user_id, sentrequests);
        }
    }

}
