package conference;

import util.PermissionException;

import java.util.UUID;

public class PermissionManager {

    ConferenceManager conferenceManager;

    public PermissionManager(ConferenceManager conferenceManager) {
        this.conferenceManager = conferenceManager;
    }

    public boolean isOrganizer(UUID conferenceUUID, UUID userUUID) {
        return conferenceManager.getConference(conferenceUUID).getOrganizerUUIDs().contains(userUUID);
    }

    public boolean isSpeaker(UUID conferenceUUID, UUID userUUID) {
        return conferenceManager.getConference(conferenceUUID).getSpeakerUUIDs().contains(userUUID);
    }

    /**
     * Validates that the current user can execute organizer actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param userUUID
     */
    public void testIsOrganizer(UUID conferenceUUID, UUID userUUID ) {
        if (!isOrganizer(conferenceUUID, userUUID)) {
            throw new PermissionException();
        }
    }

    /**
     * Validates that the current user can execute speaker actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param userUUID
     */
    public void testIsSpeaker(UUID conferenceUUID, UUID userUUID ) {
        // Organizers can perform speaker actions too
        if (!isSpeaker(conferenceUUID, userUUID) && !isOrganizer(conferenceUUID, userUUID)) {
            throw new PermissionException();
        }
    }
}
