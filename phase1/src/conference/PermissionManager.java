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

    public String generateAccessDeniedError(UUID conferenceUUID, UUID executorUUID, String permissionLevel) {
        return String.format("Access denied\n User: %s \n Conference: %s\n Required Permission: %s", executorUUID, conferenceUUID, permissionLevel);
    }

    /**
     * Validates that the current user can execute organizer actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void testIsOrganizer(UUID conferenceUUID, UUID executorUUID ) {
        if (!conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, executorUUID, ORGANIZER));
            throw new PermissionException();
        }
    }

    /**
     * Validates that the current user can execute speaker actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void testIsSpeaker(UUID conferenceUUID, UUID executorUUID ) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isSpeaker(conferenceUUID, executorUUID) && !conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, executorUUID, SPEAKER));
            throw new PermissionException();
        }
    }

    /**
     * Validates that the current user can execute attendee actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void testIsAttendee(UUID conferenceUUID, UUID executorUUID ) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isAttendee(conferenceUUID, executorUUID) && !conferenceManager.isSpeaker(conferenceUUID, executorUUID) && !conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, executorUUID, ATTENDEE));
            throw new PermissionException();
        }
    }
    
    /**
     * Validates that the current user is executing operations on themselves, or an admin is executing those commands on
     * their behalf.
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     */
    public void testIsAttendeeSelfOrAdmin(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        if (!conferenceManager.isOrganizer(conferenceUUID, executorUUID) && !executorUUID.equals(targetUserUUID)) {
            testIsAttendee(conferenceUUID, targetUserUUID);
        }

    }
}
