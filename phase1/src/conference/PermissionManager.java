package conference;

import util.NullConferenceException;
import util.PermissionException;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissionManager {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    ConferenceManager conferenceManager;

    final String ORGANIZER = "ORGANIZER";
    final String SPEAKER = "SPEAKER";

    public PermissionManager(ConferenceManager conferenceManager) {
        this.conferenceManager = conferenceManager;
    }

    public boolean isOrganizer(UUID conferenceUUID, UUID userUUID) {
        return conferenceManager.getConference(conferenceUUID).isOrganizer(userUUID);
    }

    public boolean isSpeaker(UUID conferenceUUID, UUID userUUID) {
        return conferenceManager.getConference(conferenceUUID).isSpeaker(userUUID);
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
        if (!isOrganizer(conferenceUUID, userUUID)) {
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
        if (!isSpeaker(conferenceUUID, userUUID) && !isOrganizer(conferenceUUID, userUUID)) {
            LOGGER.log(Level.SEVERE, generateAccessDeniedError(conferenceUUID, userUUID, SPEAKER));
            throw new PermissionException();
        }
    }
}
