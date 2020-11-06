package conference;

import conference.event.EventManager;
import conference.room.RoomManager;
import java.time.LocalDateTime;
import java.util.UUID;

public class ConferenceController {

    ConferenceManager conferenceManager = new ConferenceManager();
    RoomManager roomManager = new RoomManager();
    EventManager eventManager = new EventManager();
    PermissionManager permissionManager = new PermissionManager(conferenceManager);

    /**
     * Create a new conference.
     *
     * Anyone can do this.
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

    public boolean deleteConference(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        return conferenceManager.deleteConference(conferenceUUID);
    }

    public boolean createEvent(UUID conferenceUUID, UUID userUUID, String eventName) {
        // do stuff here
        return true;
    }
}
