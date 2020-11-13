package convention.permission;

import convention.conference.ConferenceManager;
import convention.exception.PermissionException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissionManager {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    ConferenceManager conferenceManager;

    private final String ORGANIZER = "ORGANIZER";
    private final String SPEAKER = "SPEAKER";
    private final String ATTENDEE = "ATTENDEE";
    private final String SELF_OR_ORGANIZER = "ATTENDEE (SELF) OR ORGANIZER";

    /**
     * Constructor for the PermissionManager
     *
     * @param conferenceManager stores a copy of ConferenceManager so that we can check a user's permission level
     */
    public PermissionManager(ConferenceManager conferenceManager) {
        this.conferenceManager = conferenceManager;
    }

    /**
     * Generates an access denied error.
     *
     * @param conferenceUUID  UUID of the conference to operate on
     * @param executorUUID    UUID of the user running the command
     * @param permissionLevel the required permission level of the target
     * @return error message
     */
    public String generateExecutorAccessDeniedError(UUID conferenceUUID, UUID executorUUID, String permissionLevel) {
        return String.format("Access denied\n Executor: %s\n Conference: %s\n Required Executor Permission: %s", executorUUID, conferenceUUID, permissionLevel);
    }

    /**
     * Generates an access denied error for operations that involve execution by a different user than the target.
     *
     * @param conferenceUUID  UUID of the conference to operate on
     * @param executorUUID    UUID of the user running the command
     * @param targetUserUUID  set of UUIDs of the user the command is operating on
     * @param permissionLevel the required permission level of the target
     * @return error message
     */
    public String generateTargetAccessDeniedError(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, String permissionLevel) {
        return String.format("Access denied\n Executor: %s\n Target: %s\n Conference: %s\n Required Target Permission: %s", executorUUID, targetUserUUID, conferenceUUID, permissionLevel);
    }

    /**
     * Validates that the current user can execute organizer actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user running the command
     */
    public void testIsOrganizer(UUID conferenceUUID, UUID executorUUID) {
        if (!conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            String errorMessage = generateExecutorAccessDeniedError(conferenceUUID, executorUUID, ORGANIZER);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }

    /**
     * Validates that the current user can execute speaker actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user running the command
     */
    public void testIsSpeaker(UUID conferenceUUID, UUID executorUUID) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isSpeaker(conferenceUUID, executorUUID) && !conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            String errorMessage = generateExecutorAccessDeniedError(conferenceUUID, executorUUID, SPEAKER);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }

    /**
     * Validates that the current user can execute attendee actions for a conference. Raises a PermissionException otherwise.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user running the command
     */
    public void testIsAttendee(UUID conferenceUUID, UUID executorUUID) {
        // Organizers can perform speaker actions too
        if (!conferenceManager.isAttendee(conferenceUUID, executorUUID) && !conferenceManager.isSpeaker(conferenceUUID, executorUUID) && !conferenceManager.isOrganizer(conferenceUUID, executorUUID)) {
            String errorMessage = generateExecutorAccessDeniedError(conferenceUUID, executorUUID, ATTENDEE);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }

    /**
     * Validates that a target user is an attendee (i.e. a user different than the executor). Raises a PermissionException otherwise.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user running the command
     * @param targetUserUUID UUID of the user the command is operating on
     */
    public void testTargetIsAttendee(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        testTargetsAreAttendee(conferenceUUID, executorUUID, new HashSet<UUID>() {
            {
                add(targetUserUUID);
            }
        });
    }

    /**
     * Validates that a set of target users can execute attendee actions for a conference. Raises a PermissionException otherwise.
     * <p>
     * Used for operations such as an organizer creating a conversation with a conference attendee. We need to make sure
     * that the attendee is actually affiliated with this conference, otherwise the organizer can slide into the DMs
     * of whoever they want.
     *
     * @param conferenceUUID  UUID of the conference to operate on
     * @param executorUUID    UUID of the user running the command
     * @param targetUserUUIDs set of UUIDs of the user the command is operating on
     */
    public void testTargetsAreAttendee(UUID conferenceUUID, UUID executorUUID, Set<UUID> targetUserUUIDs) {
        for (UUID targetUserUUID : targetUserUUIDs) {
            try {
                testIsAttendee(conferenceUUID, targetUserUUID);
            } catch (PermissionException e) {
                String errorMessage = generateTargetAccessDeniedError(conferenceUUID, executorUUID, targetUserUUID, ATTENDEE);

                LOGGER.log(Level.SEVERE, errorMessage);
                throw new PermissionException(errorMessage);
            }
        }
    }

    /**
     * Validates that the current user is executing operations on themselves, or an admin is executing those commands on
     * their behalf.
     * <p>
     * For example, a user may leave a conference, or an organizer can remove them. In either case, they should both call
     * the same "leave conference" method. Instead of writing duplicate code, we can simply check if the executor is
     * the user (so running it as themself), or if it's an admin performing the action.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user running the command
     * @param targetUserUUID UUID of the user the command is operating on
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
            String errorMessage = generateExecutorAccessDeniedError(conferenceUUID, executorUUID, SELF_OR_ORGANIZER);

            LOGGER.log(Level.SEVERE, errorMessage);
            throw new PermissionException(errorMessage);
        }
    }
}
