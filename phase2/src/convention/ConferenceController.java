package convention;

import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.event.Event;
import convention.event.EventManager;
import convention.exception.InvalidSortMethodException;
import convention.permission.PermissionManager;
import convention.schedule.ScheduleManager;
import messaging.ConversationManager;
import user.UserManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operations on Conferences
 */
public class ConferenceController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private UserManager userManager;
    private ConversationManager conversationManager;
    private EventController eventController;
    private ConferenceManager conferenceManager;
    private PermissionManager permissionManager;

    /**
     * Creates an instance of ConferenceController. We store an instance of conversationController so we can
     * send instructions to it to create or mutate conversations that are created for conferences.
     *
     * @param conversationManager
     * @param eventController
     * @param conferenceManager
     */
    public ConferenceController(ConversationManager conversationManager, EventController eventController, ConferenceManager conferenceManager, UserManager userManager) {
        this.conversationManager = conversationManager;
        this.eventController = eventController;
        this.conferenceManager = conferenceManager;
        this.userManager = userManager;
        this.permissionManager = new PermissionManager(conferenceManager, userManager);
    }

    /* Conference operations */

    /**
     * Get a set of all conference UUIDs.
     * <p>
     * Required Permission: NONE
     *
     * @return set of conference UUIDs
     */
    public Set<UUID> getConferences() {
        return conferenceManager.getConferences();
    }

    /**
     * Get a set of all conference that a user is part of.
     * <p>
     * Required Permission: NONE
     *
     * @return set of conference UUIDs
     */
    public Set<UUID> getUserConferences(UUID userUUID) {
        Set<UUID> myConferences = new HashSet<>();

        for (UUID conferenceUUID : conferenceManager.getConferences()) {
            if (conferenceManager.isAffiliated(conferenceUUID, userUUID, userManager)) {
                myConferences.add(conferenceUUID);
            }
        }

        return myConferences;
    }

    /**
     * Get a set of all conference that a user is not a part of.
     * <p>
     * Required Permission: NONE
     *
     * @return set of conference UUIDs
     */
    public Set<UUID> getNotUserConferences(UUID userUUID) {
        Set<UUID> myNotConferences = new HashSet<>();

        for (UUID conferenceUUID : conferenceManager.getConferences()) {
            if (!conferenceManager.isAffiliated(conferenceUUID, userUUID, userManager)) {
                myNotConferences.add(conferenceUUID);
            }
        }

        return myNotConferences;
    }

    /**
     * Tests if a conference exists.
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @return true iff a conference exists with the given UUID
     */
    public boolean conferenceExists(UUID conferenceUUID) {
        return conferenceManager.conferenceExists(conferenceUUID);
    }

    /**
     * Get conference name.
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @return the name of the conference
     */
    public String getConferenceName(UUID conferenceUUID) {
        return conferenceManager.getConferenceName(conferenceUUID);
    }

    /**
     * Get conference time range.
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @return the TimeRange of the conference
     */
    public TimeRange getConferenceTimeRange(UUID conferenceUUID) {
        return conferenceManager.getTimeRange(conferenceUUID);
    }

    /**
     * Create a new conference.
     * <p>
     * (This is a special case because there aren't organizers before a conference is created)
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceName name of the new conference (must be non-empty)
     * @param timeRange      time range of the new conference
     * @param organizerUUID  UUID of the initial organizer user
     * @return UUID of the new conference
     */
    public UUID createConference(String conferenceName, TimeRange timeRange, UUID organizerUUID) {
        UUID conferenceUUID = conferenceManager.createConference(conferenceName, timeRange, organizerUUID);
        LOGGER.log(Level.INFO, String.format("Conference Created\n UUID: %s\n Conference Name: %s\n Executor: %s\n Time Range: %s", conferenceUUID, conferenceName, organizerUUID, timeRange));
        return conferenceUUID;
    }

    /**
     * Set a new time range for a conference.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param timeRange      new TimeRange for the conference
     */
    public void setConferenceTimeRange(UUID conferenceUUID, UUID executorUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setTimeRange(conferenceUUID, timeRange);
        LOGGER.log(Level.INFO, String.format("Conference Time Range Updated\n Conference UUID: %s\n Executor: %s\n Time Range: %s", conferenceUUID, executorUUID, timeRange));
    }

    /**
     * Set conference name
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param newName        new name for the conference (must be non-empty)
     */
    public void setConferenceName(UUID conferenceUUID, UUID executorUUID, String newName) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setConferenceName(conferenceUUID, newName);
        LOGGER.log(Level.INFO, String.format("Conference Name Updated\n Conference UUID: %s\n Executor: %s\n Name: %s", conferenceUUID, executorUUID, newName));
    }

    /**
     * Deletes a conference.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public void deleteConference(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.deleteConference(conferenceUUID);
        LOGGER.log(Level.INFO, String.format("Conference Deleted\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Get all the events UUID to TimeRange pairs for this conference.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return map from events UUID to their respective time ranges.
     */
    public Map<UUID, TimeRange> getConferenceSchedule(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        return conferenceManager.getConferenceSchedule(conferenceUUID);
    }

    /**
     * Join a conference as an attendee.
     * <p>
     * (This is a special case because users aren't an attendee until after they join a conference)
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public void addAttendee(UUID conferenceUUID, UUID executorUUID) {
        conferenceManager.addAttendee(conferenceUUID, executorUUID);
        LOGGER.log(Level.INFO, String.format("User joined conference\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Attempt to leave a conference.
     * <p>
     * You can't leave a conference if you're the last organizer. This method will initiate a process to decouple the conference
     * from the user. This means unregistering from all events, removing this user as a speaker (if applicable), and verifying that
     * the conference won't be bricked if the user leaves (for an organizer).
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     */
    public void leaveConference(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        // We must revoke all their roles
        // We must check that the target user is part of the organizer set in case they are an organizer thru god mode,
        // in which case, they aren't actually registered to this conference.
        if (conferenceManager.getOrganizers(conferenceUUID).contains(targetUserUUID) && conferenceManager.isOrganizer(conferenceUUID, targetUserUUID, userManager)) {
            conferenceManager.removeOrganizer(conferenceUUID, targetUserUUID);

            // Update the conversation list for each event
            for (UUID eventUUID : eventController.getEvents(conferenceUUID, executorUUID)) {
                eventController.updateEventConversationMembers(conferenceUUID, eventUUID);
            }
        }

        if (conferenceManager.isSpeaker(conferenceUUID, targetUserUUID)) {
            // We'll handle revoking speaker access in updateSpeakers, since having speaker permissions is linked to
            // whether or not a user is a speaker of an events.
            for (UUID eventUUID : eventController.getSpeakerEvents(conferenceUUID, targetUserUUID)) {
                eventManager.removeEventSpeaker(eventUUID, targetUserUUID);
            }

            // Refresh the list of speakers for this conference
            eventController.updateSpeakers(conferenceUUID);
        }

        if (conferenceManager.isAttendee(conferenceUUID, targetUserUUID)) {
            for (UUID eventUUID : eventController.getAttendeeEvents(conferenceUUID, targetUserUUID)) {
                eventController.doUnregisterForEvent(conferenceUUID, targetUserUUID, eventUUID);
            }

            conferenceManager.removeAttendee(conferenceUUID, targetUserUUID);
        }

        LOGGER.log(Level.INFO, String.format("User left conference\n Conference UUID: %s\n Target: %s\n Executor: %s", conferenceUUID, targetUserUUID, executorUUID));
    }

    /**
     * Creates a conversation between the organizer and any number of users with conference membership.
     * <p>
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUUIDs    UUIDs of the users to add to the conversation
     * @return UUID of the new conversation
     */
    public UUID createConversationWithUsers(UUID conferenceUUID, UUID executorUUID, Set<UUID> targetUUIDs) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);
        permissionManager.testTargetsAreAttendee(conferenceUUID, executorUUID, targetUUIDs);

        // Allow all the target users + the organizer running this to have read/write access to the new convo
        Set<UUID> conversationUsers = new HashSet<>(targetUUIDs);
        conversationUsers.add(executorUUID);

        String executorName = userManager.getUserFirstName(executorUUID);
        String conversationName = String.format("Executive chat with %s @ %s", executorName, getConferenceName(conferenceUUID));

        return conversationManager.createConversation(conversationName, conversationUsers, conversationUsers, executorUUID, String.format("Hi, this is %s.", executorName));
    }

    /* Organizer operations */

    /**
     * Adds a user as an organizer for a conference.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     */
    public void addOrganizer(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.addOrganizer(conferenceUUID, targetUserUUID);
    }

    /**
     * Revokes a user's organizer permissions for a conference. This operation will be rejected if this is the last
     * organizer of the conference.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
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
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of organizer UUIDs
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.getOrganizers(conferenceUUID);
    }

    /**
     * Checks if the target user is an organizer.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUUID     UUID of the user to operate on
     * @return true iff target user is an organizer
     */
    public boolean isOrganizer(UUID conferenceUUID, UUID executorUUID, UUID targetUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.isOrganizer(conferenceUUID, targetUUID, userManager);
    }

    /* Some more getters */

    /**
     * Gets a set of UUIDs of speakers.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of speaker UUIDs
     */
    public Set<UUID> getSpeakers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.getSpeakers(conferenceUUID);
    }

    /**
     * Checks if the target user is an speaker.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUUID     UUID of the user to operate on
     * @return true iff target user is a speaker
     */
    public boolean isSpeaker(UUID conferenceUUID, UUID executorUUID, UUID targetUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.isSpeaker(conferenceUUID, targetUUID);
    }

    /**
     * Gets a set of UUIDs of attendees.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of attendee UUIDs
     */
    public Set<UUID> getAttendees(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.getAttendees(conferenceUUID);
    }

    /**
     * Checks if the target user is an attendee.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUUID     UUID of the user to operate on
     * @return true iff target user is a attendee
     */
    public boolean isAttendee(UUID conferenceUUID, UUID executorUUID, UUID targetUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.isAttendee(conferenceUUID, targetUUID);
    }

    /**
     * Gets a set of UUIDs of all users affiliated with this conference.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of user UUIDs
     */
    public Set<UUID> getUsers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        Set<UUID> userUUIDs = new HashSet<>();
        userUUIDs.addAll(conferenceManager.getAttendees(conferenceUUID));
        userUUIDs.addAll(conferenceManager.getSpeakers(conferenceUUID));
        userUUIDs.addAll(conferenceManager.getOrganizers(conferenceUUID));

        return userUUIDs;
    }

    /**
     * @param userId UUID of a speaker if sortBy == "speaker", UUID of the user if sortBy == "registered"
     * @param sortBy can either be "speaker" or "registered"
     * @throws IOException promps a file download for an events schedule sorted by speaker or events user signed up for
     */
    public void printSchedule(UUID userId, String sortBy, String fileName) throws IOException {
        if (!(sortBy.equals("speaker") || sortBy.equals("registered"))) {
            throw new InvalidSortMethodException();
        }
        String userName = userManager.getUserUsername(userId);
        ScheduleManager scheduleManager = new ScheduleManager();
        Set<UUID> conferenceUUIDSet;
        if (sortBy.equals("speaker")) {
            conferenceUUIDSet = getConferences();
        } else {
            conferenceUUIDSet = getUserConferences(userId);
        }
        for (UUID conferenceID : conferenceUUIDSet) {
            EventManager em = conferenceManager.getEventManager(conferenceID);
            Set<UUID> EventsInConference = eventController.getAttendeeEvents(conferenceID, userId);
            for (UUID eventUUID : EventsInConference) {
                Event event = em.getEvent(eventUUID);
                String speakers = "";
                for (UUID speakerUUID : event.getSpeakers()) {
                    speakers = speakers.concat(userManager.getUserUsername(speakerUUID).concat(", "));
                }
                scheduleManager.addEventStringList(
                        event.getTitle(),
                        event.getTimeRange().toString(),
                        conferenceManager.getRoomManager(conferenceID).getRoomLocation(event.getRoomUUID()),
                        speakers
                );
            }
        }
        scheduleManager.setScheduleTitle(sortBy, userName);
        scheduleManager.print(fileName);
    }

    /**
     * @param userid UUID of the user requesting the printable schedule
     * @param date   a day on which events schedule is printed
     * @throws IOException Overloading the printSchedule method for when the user want to sort by date. A sortBy parameter is not needed
     *                     as input
     */
    public void printSchedule(UUID userid, LocalDate date, String fileName) throws IOException {
        Set<UUID> conferenceUUIDSet = getConferences();
        ScheduleManager scheduleManager = new ScheduleManager();

        for (UUID conferenceID : conferenceUUIDSet) {
            EventManager em = conferenceManager.getEventManager(conferenceID);
            Set<UUID> eventsOnDayInConference = eventController.getDayEvents(conferenceID, userid, date);
            for (UUID eventUUID : eventsOnDayInConference) {
                Event event = em.getEvent(eventUUID);
                String speakers = "";
                for (UUID speakerUUID : event.getSpeakers()) {
                    speakers = speakers.concat(userManager.getUserUsername(speakerUUID).concat(", "));
                }
                scheduleManager.addEventStringList(
                        event.getTitle(),
                        event.getTimeRange().toString(),
                        conferenceManager.getRoomManager(conferenceID).getRoomLocation(event.getRoomUUID()),
                        speakers
                );
            }
        }
        scheduleManager.setScheduleTitle("day", date.toString());
        scheduleManager.print(fileName);
    }
}
