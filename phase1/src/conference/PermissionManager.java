package conference;

import util.exception.PermissionException;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissionManager {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    ConferenceManager conferenceManager;

    final String ORGANIZER = "ORGANIZER";
    final String SPEAKER = "SPEAKER";
    final String ATTENDEE = "ATTENDEE";

    public PermissionManager(ConferenceManager conferenceManager) {
        this.conferenceManager = conferenceManager;
    }

    public String generateAccessDeniedError(UUID conferenceUUID, UUID userUUID, String permissionLevel) {
        return String.format("Access denied\n User: %s \n Conference: %s\n Required Permission: %s", userUUID, conferenceUUID, permissionLevel);
    }

    /**
     * Validates that the current user can execute organizer actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param userUUID
     */
    public void testIsOrganizer(UUID conferenceUUID, UUID userUUID ) {
        if (!conferenceManager.isOrganizer(conferenceUUID, userUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, userUUID, ORGANIZER));
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
        if (!conferenceManager.isSpeaker(conferenceUUID, userUUID) && !conferenceManager.isOrganizer(conferenceUUID, userUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, userUUID, SPEAKER));
            throw new PermissionException();
        }
    }

    /**
     * Validates that the current user can execute speaker actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param userUUID
     */
    public void testIsAttendee(UUID conferenceUUID, UUID userUUID ) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isAttendee(conferenceUUID, userUUID) && !conferenceManager.isSpeaker(conferenceUUID, userUUID) && !conferenceManager.isOrganizer(conferenceUUID, userUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, userUUID, ATTENDEE));
            throw new PermissionException();
        }
    }
}
