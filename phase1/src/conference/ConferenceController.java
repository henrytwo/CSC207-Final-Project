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
        // gotta save the conversation controller
    }

    /* General operations */

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
    public LocalDateTime getStart(UUID conferenceUUID) {
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
    public LocalDateTime getEnd(UUID conferenceUUID) {
        return conferenceManager.getEnd(conferenceUUID);
    }

    /* Attendee operations */

    /**
     * Attempt to join a conference.
     *
     * (This is a special case because users aren't an attendee until after they join a conference)
     *
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @param userUUID
     */
    public void joinConference(UUID conferenceUUID, UUID userUUID) {
        // Add invitation system?
        // private flag?
        // ok nvm, to keep it simple, just let the user see a list of conferences and let them join whatever they want

        if (conferenceManager.conferenceExists(conferenceUUID)) {

        }
    }

    /**
     * Sign up for an event. A user must be an attendee of the parent conference to sign up.
     *
     * @param conferenceUUID
     * @param userUUID
     * @param eventUUID
     */
    public void signupForEvent(UUID conferenceUUID, UUID userUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, userUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);

        // need to check for existing groupchat + add them and stuff
        // event manager does stuff
    }

    /**
     * Unregister for an event. A user must be an attendee of the parent conference to unregister.
     *
     * @param conferenceUUID
     * @param userUUID
     * @param eventUUID
     */
    public void unregisterForEvent(UUID conferenceUUID, UUID userUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, userUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);

        // event manager does stuff
        // revoke access to the gc
    }

    /**
     * Get a list of events. A user must be an attendee of the parent conference to view events.
     *
     * @param conferenceUUID
     * @param userUUID
     * @param eventUUID
     */
    public Set<Event> getEvents(UUID conferenceUUID, UUID userUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, userUUID);

        Set<Event> events = conferenceManager.getEventsFromConference(conferenceUUID);
        // hm can we return the entire object, or should we do something else?

        return new HashSet<>();
    }

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
    public void setDates(UUID conferenceUUID, UUID userUUID, LocalDateTime newStart, LocalDateTime newEnd) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
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
    public void setConferenceName(UUID conferenceUUID, UUID userUUID, String newName) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.setConferenceName(conferenceUUID, newName);
    }

    /**
     * Deletes a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
     */
    public void deleteConference(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.deleteConference(conferenceUUID);
    }

    /**
     * Gets a set of UUIDs of organizers.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        return conferenceManager.getOrganizers(conferenceUUID);
    }

    /**
     * Adds a user as an organizer for a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @param targetUserUUID
     * @return
     */
    public void addOrganizer(UUID conferenceUUID, UUID userUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.addOrganizer(conferenceUUID, targetUserUUID);
    }

    /**
     * Revokes a user's organizer permissions for a conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID
     * @param userUUID
     * @param targetUserUUID
     * @return
     */
    public void removeOrganizer(UUID conferenceUUID, UUID userUUID, UUID targetUserUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);
        conferenceManager.removeOrganizer(conferenceUUID, targetUserUUID);
    }

    public void createEvent(UUID conferenceUUID, UUID userUUID, String eventName) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff here
    }

    public void setEventName(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff here
    }

    public void deleteEvent(UUID conferenceUUID, UUID userUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff here
    }

    public void createRoom(UUID conferenceUUID, UUID userUUID, UUID roomUUID, String roomNumber, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff
    }

    public void setRoomNumber(UUID conferenceUUID, UUID userUUID, UUID roomUUID, String roomNumber) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff
    }

    public void setRoomCapacity(UUID conferenceUUID, UUID userUUID, UUID roomUUID, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff
    }

    public void deleteRoom(UUID conferenceUUID, UUID userUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, userUUID);

        // do stuff
    }

    /* Speaker operations */
    public Set<UUID> listAttendees(UUID conferenceUUID, UUID userUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, userUUID);

        // do stuff

        return new HashSet<>();
    }

    public void createEventConversation(UUID conferenceUUID, UUID userUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, userUUID);

        // do stuff
    }
}
