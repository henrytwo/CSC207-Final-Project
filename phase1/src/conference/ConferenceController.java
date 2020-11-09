package conference;

import conference.calendar.TimeRange;
import conference.event.Event;
import conference.event.EventManager;
import conference.room.RoomManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    ConferenceManager conferenceManager = new ConferenceManager();
    RoomManager roomManager = new RoomManager();
    EventManager eventManager = new EventManager();
    PermissionManager permissionManager = new PermissionManager(conferenceManager);

    public ConferenceController() {
        // do some more magic here
        // gotta save the conversation controller here so that we can talk to it
    }

    /* Conference operations */

    /**
     * Get a all conference UUIDs.
     *
     * Required Permission: NONE
     */
    public Set<UUID> getConferences() {
        return conferenceManager.getConferences();
    }

    /**
     * Tests of a conference exists.
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @return
     */
    public boolean conferenceExists(UUID conferenceUUID) {
        return conferenceManager.conferenceExists(conferenceUUID);
    }

    /**
     * Get conference name
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @return
     */
    public String getConferenceName(UUID conferenceUUID) {
        return conferenceManager.getConferenceName(conferenceUUID);
    }

    /**
     * Get conference time range
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @return
     */
    public TimeRange getConferenceTimeRange(UUID conferenceUUID) {
        return conferenceManager.getTimeRange(conferenceUUID);
    }

    /**
     * Create a new conference.
     *
     * (This is a special case because there aren't organizers before a conference is created)
     *
     * Required Permission: NONE
     *
     * @param conferenceName
     * @param timeRange
     * @param organizerUUID
     * @return
     */
    public UUID createConference(String conferenceName, TimeRange timeRange, UUID organizerUUID) {
        UUID conferenceUUID = conferenceManager.createConference(conferenceName, timeRange, organizerUUID);
        LOGGER.log(Level.INFO, String.format("Conference Created\n UUID: %s\n Conference Name: %s\n Executor: %s\n Time Range: %s", conferenceUUID, conferenceName, organizerUUID, timeRange));
        return conferenceUUID;
    }

    /**
     * Set start date time
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @return
     */
    public void setConferenceTimeRange(UUID conferenceUUID, UUID executorUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setTimeRange(conferenceUUID, timeRange);
        LOGGER.log(Level.INFO, String.format("Conference Time Range Updated\n Conference UUID: %s\n Executor: %s\n Time Range: %s", conferenceUUID, executorUUID, timeRange));
    }

    /**
     * Set conference name
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @return
     */
    public void setConferenceName(UUID conferenceUUID, UUID executorUUID, String newName) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setConferenceName(conferenceUUID, newName);
        LOGGER.log(Level.INFO, String.format("Conference Name Updated\n Conference UUID: %s\n Executor: %s\n Name: %s", conferenceUUID, executorUUID, newName));
    }

    /**
     * Deletes a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @return
     */
    public void deleteConference(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.deleteConference(conferenceUUID);
        LOGGER.log(Level.INFO, String.format("Conference Deleted\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Get the conference calendar
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void getConferenceSchedule(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        /**
         * TODO: write this
         */
    }

    /**
     * Attempt to join a conference.
     *
     * (This is a special case because users aren't an attendee until after they join a conference)
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void joinConference(UUID conferenceUUID, UUID executorUUID) {
        conferenceManager.joinConference(conferenceUUID, executorUUID);
        LOGGER.log(Level.INFO, String.format("User joined conference\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Attempt to leave a conference.
     *
     * You can't leave a conference if you're the last organizer.
     *
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     */
    public void leaveConference(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        conferenceManager.leaveConference(conferenceUUID, targetUserUUID);

        /**
         * TODO: Unregister user from all their events
         */

        /**
         * TODO: Unregister user from all their speaker events
         */

        LOGGER.log(Level.INFO, String.format("User left conference\n Conference UUID: %s\n Target: %s\n Executor: %s", conferenceUUID, targetUserUUID, executorUUID));
    }

    /**
     * Creates a conversation between the organizer and any number of users with conference membership.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUUIDs
     */
    public void createConversationWithUsers(UUID conferenceUUID, UUID executorUUID, Set<UUID> targetUUIDs) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        permissionManager.testTargetsAreAttendee(conferenceUUID, executorUUID, targetUUIDs);

        // do stuff
    }

    /* Event operations */

    /**
     * Get a list of events. A user must be an attendee of the parent conference to view events.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public Set<UUID> getEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);
        // hm can we return the entire object, or should we do something else?

        return new HashSet<>();
    }

    /**
     * Sign up for an event. A user must be an attendee of the parent conference to sign up.
     *
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     * @param eventUUID
     */
    public void registerForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);

        // need to check for existing groupchat + add them and stuff
        // event manager does stuff
    }

    /**
     * Unregister for an event. A user must be an attendee of the parent conference to unregister.
     *
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     * @param eventUUID
     */
    public void unregisterForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);

        // event manager does stuff
        // revoke access to the gc
    }

    public void createEvent(UUID conferenceUUID, UUID executorUUID, String eventName, UUID roomUUID, Set<UUID> speakerUUIDs, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        /**
         * Optional room and speaker?
         */

        // if there's a speaker
        // do stuff here
    }

    public void setEventName(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, String eventName) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff here
    }

    public void setEventRoom(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff here
    }

    public void setEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff here
    }

    public void addEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff here
    }

    public void removeEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff here
    }

    public void deleteEvent(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff here
    }

    public String getEventName(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        // do stuff
        return "";
    }

    public Set<UUID> getEventSpeakers(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        // do stuff
        return new HashSet<>();
    }

    public TimeRange getEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        // do stuff
        return null;
    }

    public Set<UUID> getEventAttendees(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        // do stuff

        return null;
    }

    /**
     * Pulls a list of attendees for an event and creates a conversation with them
     *
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param eventUUID
     */
    public void createEventConversation(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);




        // do stuff
    }

    /* Room operations */
    public Set<UUID> getRooms(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        // do stuff

        return null;
    }

    public void createRoom(UUID conferenceUUID, UUID executorUUID, String roomNumber, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    public void setRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, String roomLocation) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    public void setRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    public void deleteRoom(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        /**
         * TODO: If a room is deleted while it's hooked up to an event, just delete it and keep the event
         */

        // do stuff
    }

    public String getRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        return null;
    }

    public int getRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        return 0;
    }

    /* Organizer operations */

    /**
     * Gets a set of UUIDs of organizers.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @return
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getOrganizers(conferenceUUID);
    }

    /**
     * Adds a user as an organizer for a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     * @return
     */
    public void addOrganizer(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.addOrganizer(conferenceUUID, targetUserUUID);
    }

    /**
     * Revokes a user's organizer permissions for a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     * @return
     */
    public void removeOrganizer(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.removeOrganizer(conferenceUUID, targetUserUUID);
    }

    /* Some more getters */

    /**
     * Gets a set of UUIDs of speakers.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @return
     */
    public Set<UUID> getSpeakers(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getSpeakers(conferenceUUID);
    }

    /**
     * Gets a set of UUIDs of attendees.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @return
     */
    public Set<UUID> getAttendees(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getAttendees(conferenceUUID);
    }
}
