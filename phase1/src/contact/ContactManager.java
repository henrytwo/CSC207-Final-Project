package contact;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ContactManager {
    private HashMap<UUID, Set<UUID>> contactsMap = new HashMap<UUID, Set<UUID>>();

    private HashMap<UUID, Set<UUID>> requestsMap = new HashMap<UUID, Set<UUID>>();

    private HashMap<UUID, Set<UUID>> sentRequestsMap = new HashMap<UUID, Set<UUID>>();
    /**
     *  returns the Contacts (friends) list of a particular user
     * @param user_id the userId of the person for whom we need a list of contacts
     * @return The Set of UUID's of the contacts
     */
    public Set<UUID> getContacts(UUID user_id){
        return contactsMap.get(user_id);
    }

    /**
     *  returns connection requests received by a user
     * @param user_id the userId of the person for whom we want to show the requests
     * @return The Set of UUID's of the users who have made a connection request
     */
    public Set<UUID> getRequests(UUID user_id){
        return requestsMap.get(user_id);
    }

    /**
     *  returns connection requests sent by a user
     * @param user_id the userId of the person for whom we need to show the requests
     * @return The Set of UUID's of the users to whom this particular user have made a connection request
     */
    public Set<UUID> getSentRequests(UUID user_id){
        return sentRequestsMap.get(user_id);
    }

    /**
     *  Sets/updates the contact list of this particular User
     * @param user_id the userId of the person for whom we need to update the contacts set
     */
    public void setContacts(UUID user_id, Set<UUID> contacts){
        contactsMap.put(user_id, contacts);
    }

    /**
     *  Sets/updates the requests(received) list of this particular User
     * @param user_id the userId of the person for whom we need to update the requests(received) set
     * @param requests the set of requests received by this user
     */
    public void setRequests(UUID user_id, Set<UUID> requests){
        requestsMap.put(user_id, requests);
    }

    /**
     *  Sets/updates the request(sent) list of this particular User
     * @param user_id the userId of the person for whom we need to update the requests(sent) set
     * @param sentrequests the set of requests sent by this user
     */
    public void setSentRequests(UUID user_id, Set<UUID> sentrequests){
        sentRequestsMap.put(user_id, sentrequests);
    }

}
