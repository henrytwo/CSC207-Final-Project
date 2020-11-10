package contact;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ContactManager {
    private HashMap<UUID, Set<UUID>> contactsMap = new HashMap<UUID, Set<UUID>>();

    private HashMap<UUID, Set<UUID>> requestsMap = new HashMap<UUID, Set<UUID>>();

    private HashMap<UUID, Set<UUID>> sentRequestsMap = new HashMap<UUID, Set<UUID>>();

    public Set<UUID> getContacts(UUID user_id){
        return contactsMap.get(user_id);
    }

    public Set<UUID> getRequests(UUID user_id){
        return requestsMap.get(user_id);
    }

    public Set<UUID> getSentRequests(UUID user_id){
        return sentRequestsMap.get(user_id);
    }

    public void setContacts(UUID user_id, Set<UUID> contacts){
        contactsMap.put(user_id, contacts);
    }

    public void setRequests(UUID user_id, Set<UUID> requets){
        requestsMap.put(user_id, requets);
    }

    public void setSentRequests(UUID user_id, Set<UUID> sentreqests){
        sentRequestsMap.put(user_id, sentreqests);
    }

}
