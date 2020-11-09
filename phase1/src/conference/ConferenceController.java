package conference;

import conference.event.Event;
import conference.event.EventManager;
import conference.room.RoomManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConferenceController {

    ConferenceManager conferenceManager = new ConferenceManager();
    RoomManager roomManager = new RoomManager();
    EventManager eventManager = new EventManager();
    PermissionManager permissionManager = new PermissionManager(conferenceManager);

    public ConferenceController() {
        // do some more magic here
        // gotta save the conversation controller here so that we can talk to it
    }

    /**
     * TODO: If a room is deleted while it's hooked up to an event, just delete it and keep the event
     */

    /* General operations */

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
     * Get start date time
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @return
     */
    public LocalDateTime getConferenceStart(UUID conferenceUUID) {
        return conferenceManager.getStart(conferenceUUID);
    }

    /**
     * Get end date time
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @return
     */
    public LocalDateTime getConferenceEnd(UUID conferenceUUID) {
        return conferenceManager.getEnd(conferenceUUID);
    }

    /* Attendee/User operations */

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
    }

    /**
     * Attempt to leave a conference.
     *
     * You can't leave a conference if you're the last organizer.
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void leaveConference(UUID conferenceUUID, UUID executorUUID) {
        conferenceManager.leaveConference(conferenceUUID, executorUUID);
    }

    /**
     * Sign up for an event. A user must be an attendee of the parent conference to sign up.
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param eventUUID
     */
    public void registerForEvent(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);

        // need to check for existing groupchat + add them and stuff
        // event manager does stuff
    }

    /**
     * Unregister for an event. A user must be an attendee of the parent conference to unregister.
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param eventUUID
     */
    public void unregisterForEvent(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);

        // event manager does stuff
        // revoke access to the gc
    }

    /**
     * Get a list of events. A user must be an attendee of the parent conference to view events.
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public Set<Event> getEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);
        // hm can we return the entire object, or should we do something else?

        return new HashSet<>();
    }

    /**
     * TODO: Calendar stuff
     */

    /* Organizer operations */

    /**
     * Create a new conference.
     *
     * (This is a special case because there aren't organizers before a conference is created)
     *
     * Required Permission: NONE
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

    /**
     * Set start date time
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @return
     */
    public void setDates(UUID conferenceUUID, UUID executorUUID, LocalDateTime newStart, LocalDateTime newEnd) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        conferenceManager.setDates(conferenceUUID, newStart, newEnd);
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
    }

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

    public void createEvent(UUID conferenceUUID, UUID executorUUID, String eventName, UUID roomUUID, Set<UUID> speakerUUIDs) {
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

    public void createRoom(UUID conferenceUUID, UUID executorUUID, String roomNumber, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    public void setRoomNumber(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, String roomLocation) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    public void setRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    public void deleteRoom(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        // do stuff
    }

    /* Speaker operations */
    public Set<UUID> getEventAttendees(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        // do stuff

        return new HashSet<>();
    }

    public void createEventConversation(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        // do stuff
    }
}
