package convention;

import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.event.EventManager;
import convention.permission.PermissionManager;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private ConversationController conversationController;
    private EventController eventController;
    private ConferenceManager conferenceManager;
    private PermissionManager permissionManager;

    /**
     * Creates an instance of ConferenceController. We store an instance of conversationController so we can
     * send instructions to it to create or mutate conversations that are created for conferences.
     *
     * @param conversationController
     */
    public ConferenceController(ConversationController conversationController, EventController eventController, ConferenceManager conferenceManager, PermissionManager permissionManager) {
        this.conversationController = conversationController;
        this.eventController = eventController;
        this.conferenceManager = conferenceManager;
        this.permissionManager = permissionManager;
    }

    /* Conference operations */

    /**
     * Get a set of all convention UUIDs.
     * <p>
     * Required Permission: NONE
     *
     * @return set of convention UUIDs
     */
    public Set<UUID> getConferences() {
        return conferenceManager.getConferences();
    }

    /**
     * Tests if a convention exists.
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @return true iff a convention exists with the given UUID
     */
    public boolean conferenceExists(UUID conferenceUUID) {
        return conferenceManager.conferenceExists(conferenceUUID);
    }

    /**
     * Get convention name.
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @return the name of the convention
     */
    public String getConferenceName(UUID conferenceUUID) {
        return conferenceManager.getConferenceName(conferenceUUID);
    }

    /**
     * Get convention time range.
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @return the TimeRange of the convention
     */
    public TimeRange getConferenceTimeRange(UUID conferenceUUID) {
        return conferenceManager.getTimeRange(conferenceUUID);
    }

    /**
     * Create a new convention.
     * <p>
     * (This is a special case because there aren't organizers before a convention is created)
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceName name of the new convention (must be non-empty)
     * @param timeRange      time range of the new convention
     * @param organizerUUID  UUID of the initial organizer user
     * @return UUID of the new convention
     */
    public UUID createConference(String conferenceName, TimeRange timeRange, UUID organizerUUID) {
        UUID conferenceUUID = conferenceManager.createConference(conferenceName, timeRange, organizerUUID);
        LOGGER.log(Level.INFO, String.format("Conference Created\n UUID: %s\n Conference Name: %s\n Executor: %s\n Time Range: %s", conferenceUUID, conferenceName, organizerUUID, timeRange));
        return conferenceUUID;
    }

    /**
     * Set a new time range for a convention.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param timeRange      new TimeRange for the convention
     */
    public void setConferenceTimeRange(UUID conferenceUUID, UUID executorUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setTimeRange(conferenceUUID, timeRange);
        LOGGER.log(Level.INFO, String.format("Conference Time Range Updated\n Conference UUID: %s\n Executor: %s\n Time Range: %s", conferenceUUID, executorUUID, timeRange));
    }

    /**
     * Set convention name
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param newName        new name for the convention (must be non-empty)
     */
    public void setConferenceName(UUID conferenceUUID, UUID executorUUID, String newName) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setConferenceName(conferenceUUID, newName);
        LOGGER.log(Level.INFO, String.format("Conference Name Updated\n Conference UUID: %s\n Executor: %s\n Name: %s", conferenceUUID, executorUUID, newName));
    }

    /**
     * Deletes a convention.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public void deleteConference(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.deleteConference(conferenceUUID);
        LOGGER.log(Level.INFO, String.format("Conference Deleted\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Get all the event UUID to TimeRange pairs for this convention.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return map from event UUID to their respective time ranges.
     */
    public Map<UUID, TimeRange> getConferenceSchedule(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        return conferenceManager.getConferenceSchedule(conferenceUUID);
    }

    /**
     * Join a convention as an attendee.
     * <p>
     * (This is a special case because users aren't an attendee until after they join a convention)
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public void addAttendee(UUID conferenceUUID, UUID executorUUID) {
        conferenceManager.addAttendee(conferenceUUID, executorUUID);
        LOGGER.log(Level.INFO, String.format("User joined convention\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Attempt to leave a convention.
     * <p>
     * You can't leave a convention if you're the last organizer. This method will initiate a process to decouple the convention
     * from the user. This means unregistering from all events, removing this user as a speaker (if applicable), and verifying that
     * the convention won't be bricked if the user leaves (for an organizer).
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     */
    public void leaveConference(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        // We must revoke all their roles
        if (conferenceManager.isOrganizer(conferenceUUID, targetUserUUID)) {
            conferenceManager.removeOrganizer(conferenceUUID, targetUserUUID);
        }

        if (conferenceManager.isAttendee(conferenceUUID, targetUserUUID)) {
            conferenceManager.removeAttendee(conferenceUUID, targetUserUUID);

            for (UUID eventUUID : eventController.getAttendeeEvents(conferenceUUID, targetUserUUID)) {
                eventController.doUnregisterForEvent(conferenceUUID, targetUserUUID, eventUUID);
            }
        }

        if (conferenceManager.isSpeaker(conferenceUUID, targetUserUUID)) {
            // We'll handle revoking speaker access in updateSpeakers, since having speaker permissions is linked to
            // whether or not a user is a speaker of an event.
            for (UUID eventUUID : eventController.getSpeakerEvents(conferenceUUID, targetUserUUID)) {
                eventManager.removeSpeaker(eventUUID, targetUserUUID);
            }

            // Refresh the list of speakers for this convention
            eventController.updateSpeakers(conferenceUUID);
        }

        LOGGER.log(Level.INFO, String.format("User left convention\n Conference UUID: %s\n Target: %s\n Executor: %s", conferenceUUID, targetUserUUID, executorUUID));
    }

    /**
     * Creates a conversation between the organizer and any number of users with convention membership.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUUIDs    UUIDs of the users to add to the conversation
     */
    public void createConversationWithUsers(UUID conferenceUUID, UUID executorUUID, Set<UUID> targetUUIDs) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        permissionManager.testTargetsAreAttendee(conferenceUUID, executorUUID, targetUUIDs);

        // do stuff
    }

    /* Organizer operations */

    /**
     * Adds a user as an organizer for a convention.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     */
    public void addOrganizer(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.addOrganizer(conferenceUUID, targetUserUUID);
    }

    /**
     * Revokes a user's organizer permissions for a convention. This operation will be rejected if this is the last
     * organizer of the convention.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     */
    public void removeOrganizer(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.removeOrganizer(conferenceUUID, targetUserUUID);
    }

    /**
     * Gets a set of UUIDs of organizers.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of organizer UUIDs
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getOrganizers(conferenceUUID);
    }

    /* Some more getters */

    /**
     * Gets a set of UUIDs of speakers.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of speaker UUIDs
     */
    public Set<UUID> getSpeakers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getSpeakers(conferenceUUID);
    }

    /**
     * Gets a set of UUIDs of attendees.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of attendee UUIDs
     */
    public Set<UUID> getAttendees(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getAttendees(conferenceUUID);
    }
}
