package conference;

import util.exception.PermissionException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissionManager {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    ConferenceManager conferenceManager;

    // This uuid is granted all permissions, to be used by the system
    /**
     * TODO: Maybe move this to user controller?
     * i wrote this at 2:47:38AM so it's proabbly trash
     */
    private final UUID systemUserUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final String ORGANIZER = "ORGANIZER";
    private final String SPEAKER = "SPEAKER";
    private final String ATTENDEE = "ATTENDEE";
    private final String SELF_OR_ORGANIZER = "ATTENDEE (SELF) OR ORGANIZER";

    public PermissionManager(ConferenceManager conferenceManager) {
        this.conferenceManager = conferenceManager;
    }

    public String generateAccessDeniedError(UUID conferenceUUID, UUID executorUUID, String permissionLevel) {
        return String.format("Access denied\n Executor: %s\n Conference: %s\n Required Executor Permission: %s", executorUUID, conferenceUUID, permissionLevel);
    }

    public String generateAccessDeniedError(UUID conferenceUUID, UUID executorUUID, UUID targetUUID, String permissionLevel) {
        return String.format("Access denied\n Executor: %s\n Target: %s\n Conference: %s\n Required Target Permission: %s", executorUUID, targetUUID, conferenceUUID, permissionLevel);
    }

    public UUID getsystemUserUUID() {
        return systemUserUUID;
    }

    /**
     * Validates that the current user can execute organizer actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void testIsOrganizer(UUID conferenceUUID, UUID executorUUID) {
        if (!conferenceManager.isOrganizer(conferenceUUID, executorUUID) && !systemUserUUID.equals(executorUUID)) {
            String errorMessage = generateAccessDeniedError(conferenceUUID, executorUUID, ORGANIZER);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }

    /**
     * Validates that the current user can execute speaker actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void testIsSpeaker(UUID conferenceUUID, UUID executorUUID) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isSpeaker(conferenceUUID, executorUUID) && !conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            String errorMessage = generateAccessDeniedError(conferenceUUID, executorUUID, SPEAKER);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }

    /**
     * Validates that the current user can execute attendee actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void testIsAttendee(UUID conferenceUUID, UUID executorUUID) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isAttendee(conferenceUUID, executorUUID) && !conferenceManager.isSpeaker(conferenceUUID, executorUUID) && !conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            String errorMessage = generateAccessDeniedError(conferenceUUID, executorUUID, ATTENDEE);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }

    /**
     * Validates that a target user is an attendee (i.e. a user different than the executor). Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUUID
     */
    public void testTargetIsAttendee(UUID conferenceUUID, UUID executorUUID, UUID targetUUID) {
        testTargetsAreAttendee(conferenceUUID, executorUUID, new HashSet<>() {
            {
                add(targetUUID);
            }
        });
    }

    /**
     * Validates that a set of target users can execute attendee actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUUIDs
     */
    public void testTargetsAreAttendee(UUID conferenceUUID, UUID executorUUID, Set<UUID> targetUUIDs) {
        for (UUID targetUUID : targetUUIDs) {
            try {
                testIsAttendee(conferenceUUID, targetUUID);
            } catch (PermissionException e) {
                String errorMessage = generateAccessDeniedError(conferenceUUID, executorUUID, targetUUID, ATTENDEE);

                LOGGER.log(Level.SEVERE, errorMessage);
                throw new PermissionException(errorMessage);
            }
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
        if (executorUUID.equals(targetUserUUID)) {
            // If the executor is the target, then we can treat this as a normal attendee operation
            testIsAttendee(conferenceUUID, targetUserUUID);
        } else if (conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            // If the executor is an organizer, still need to check that the target is actually an attendee
            testTargetIsAttendee(conferenceUUID, executorUUID, targetUserUUID);
        } else {
            // Otherwise, no can do. The executor is not an organizer, and is not operating on themselves.
            String errorMessage = generateAccessDeniedError(conferenceUUID, executorUUID, SELF_OR_ORGANIZER);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }
}
