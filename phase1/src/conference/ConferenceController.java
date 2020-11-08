package conference;

import conference.event.EventManager;
import conference.room.RoomManager;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class ConferenceController {

    ConferenceManager conferenceManager = new ConferenceManager();
    RoomManager roomManager = new RoomManager();
    EventManager eventManager = new EventManager();
    PermissionManager permissionManager = new PermissionManager(conferenceManager);

    /**
     * Tests of a conference exists.
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @return
     */
    public boolean conferenceExists(UUID conferenceUUID) {
        return conferenceManager.getConference(conferenceUUID) != null;
    }

    /**
     * Create a new conference.
     *
     * Required Permission: NONE
     *
     * @param conferenceName
     * @param startTime
     * @param endTime
     * @param organizerUUID
     * @return
     */
    public UUID createConference(String conferenceName, LocalDateTime startTime, LocalDateTime endTime, UUID organizerUUID) {
        return conferenceManager.createConference(conferenceName, startTime, endTime, organizerUUID);
    }

    /**
     * Deletes a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
     */
    public void deleteConference(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.deleteConference(conferenceUUID);
    }

    /**
     * Gets a set of UUIDs of organizers.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        return conferenceManager.getOrganizers(conferenceUUID);
    }

    /**
     * Adds a user as an organizer for a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @param targetUserUUID
     * @return
     */
    public void addOrganizer(UUID conferenceUUID, UUID userUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.addOrganizer(conferenceUUID, targetUserUUID);
    }

    /**
     * Revokes a user's organizer permissions for a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @param targetUserUUID
     * @return
     */
    public void removeOrganizer(UUID conferenceUUID, UUID userUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.removeOrganizer(conferenceUUID, targetUserUUID);
    }

    public void createEvent(UUID conferenceUUID, UUID userUUID, String eventName) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff here
    }

    public void createRoom(UUID conferenceUUID, UUID userUUID, String roomNumber) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

    }

}
