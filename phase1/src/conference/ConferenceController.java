package conference;

import convention.calendar.CalendarManager;
import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.event.EventManager;
import convention.permission.PermissionManager;
import convention.room.RoomManager;
import util.exception.DoubleBookingException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ConferenceManager conferenceManager = new ConferenceManager();
    private PermissionManager permissionManager = new PermissionManager(conferenceManager);

    public ConferenceController() {
        // do some more magic here
        // gotta save the conversation controller here so that we can talk to it
    }

    /* Conference operations */

    /**
     * Get a all conference UUIDs.
     * <p>
     * Required Permission: NONE
     */
    public Set<UUID> getConferences() {
        return conferenceManager.getConferences();
    }

    /**
     * Tests of a conference exists.
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
     * (This is a special case because there aren't organizers before a conference is created)
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public Map<UUID, TimeRange> getConferenceSchedule(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        return conferenceManager.getConferenceSchedule(conferenceUUID);
    }

    /**
     * Attempt to join a conference.
     * <p>
     * (This is a special case because users aren't an attendee until after they join a conference)
     * <p>
     * Required Permission: NONE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public void addAttendee(UUID conferenceUUID, UUID executorUUID) {
        conferenceManager.addAttendee(conferenceUUID, executorUUID);
        LOGGER.log(Level.INFO, String.format("User joined conference\n Conference UUID: %s\n Executor: %s", conferenceUUID, executorUUID));
    }

    /**
     * Attempt to leave a conference.
     * <p>
     * You can't leave a conference if you're the last organizer.
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
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

            for (UUID eventUUID : getAttendeeEvents(conferenceUUID, targetUserUUID)) {
                doUnregisterForEvent(conferenceUUID, targetUserUUID, eventUUID);
            }
        }

        if (conferenceManager.isSpeaker(conferenceUUID, targetUserUUID)) {
            // We'll handle revoking speaker access in updateSpeakers, since having speaker permissions is linked to
            // whether or not a user is a speaker of an event.
            for (UUID eventUUID : getSpeakerEvents(conferenceUUID, targetUserUUID)) {
                eventManager.removeEventSpeaker(eventUUID, targetUserUUID);
            }

            // Refresh the list of speakers for this conference
            updateSpeakers(conferenceUUID);
        }

        LOGGER.log(Level.INFO, String.format("User left conference\n Conference UUID: %s\n Target: %s\n Executor: %s", conferenceUUID, targetUserUUID, executorUUID));
    }

    /**
     * Creates a conversation between the organizer and any number of users with conference membership.
     * <p>
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
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public Set<UUID> getEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        return eventManager.getEvents();
    }

    /**
     * Get a list of events an attendee is registered in. A user must be an attendee of the parent conference to view events.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public Set<UUID> getAttendeeEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        Set<UUID> registeredEventsUUIDs = new HashSet<>();

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        for (UUID eventUUID : eventManager.getEvents()) {
            if (eventManager.getEventAttendees(eventUUID).contains(executorUUID)) {
                registeredEventsUUIDs.add(eventUUID);
            }
        }

        return registeredEventsUUIDs;
    }

    /**
     * Get a list of events a speaker is registered in.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID
     * @param executorUUID
     */
    public Set<UUID> getSpeakerEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        Set<UUID> registeredEventsUUIDs = new HashSet<>();
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        for (UUID eventUUID : eventManager.getEvents()) {
            if (eventManager.getEventSpeakers(eventUUID).contains(executorUUID)) {
                registeredEventsUUIDs.add(eventUUID);
            }
        }

        return registeredEventsUUIDs;
    }

    /**
     * Sign up for an event. A user must be an attendee of the parent conference to sign up.
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     * @param eventUUID
     */
    public void registerForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        if (eventManager.getConversationUUID(eventUUID) != null) {
            /**
             * TODO: Add user to the group chat with write access
             */
        }

        eventManager.registerAttendee(eventUUID, targetUserUUID);
    }

    /**
     * Actually executes the unregister operation. We have a separate helper method here so that we don't forget
     * to run the check to remove the user from the event's conversation.
     *
     * @param conferenceUUID
     * @param targetUserUUID
     * @param eventUUID
     */
    private void doUnregisterForEvent(UUID conferenceUUID, UUID targetUserUUID, UUID eventUUID) {
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        if (eventManager.getConversationUUID(eventUUID) != null) {
            /**
             * TODO: Remove the user from the groupchat
             */
        }

        eventManager.unregisterAttendee(eventUUID, targetUserUUID);
    }

    /**
     * Unregister for an event. A user must be an attendee of the parent conference to unregister.
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param targetUserUUID
     * @param eventUUID
     */
    public void unregisterForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        doUnregisterForEvent(conferenceUUID, targetUserUUID, eventUUID);
    }

    /**
     * Helper function to keep the list of speakers at a conference in sync
     *
     * @param conferenceUUID
     */
    private void updateSpeakers(UUID conferenceUUID) {
        Set<UUID> speakerUUIDs = conferenceManager.getSpeakers(conferenceUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        speakerUUIDs.clear();

        for (UUID eventUUID : eventManager.getEvents()) {
            Set<UUID> eventSpeakerUUIDs = eventManager.getEventSpeakers(eventUUID);

            speakerUUIDs.addAll(eventSpeakerUUIDs);
        }
    }

    public void createEvent(UUID conferenceUUID, UUID executorUUID, String eventName, TimeRange timeRange, UUID roomUUID, Set<UUID> speakerUUIDs) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        CalendarManager roomCalendarManager = roomManager.getCalendarManager(roomUUID);

        /**
         * We need to make sure there is no other event booked at this time
         */
        if (roomCalendarManager.timeRangeOccupied(timeRange)) {
            throw new DoubleBookingException();
        } else {
            UUID eventUUID = eventManager.createEvent(eventName, timeRange, roomUUID, speakerUUIDs);

            roomCalendarManager.addTimeBlock(eventUUID, timeRange);
        }

        updateSpeakers(conferenceUUID);
    }

    public void addEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        /**
         * TODO: Test if there is a booking conflict for the speaker
         */

        eventManager.addEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    public void removeEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        eventManager.removeEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    public void deleteEvent(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        /**
         * TODO: Delete the booking from the room
         *       Update rooms method???
         */

        eventManager.deleteEvent(eventUUID);
        updateSpeakers(conferenceUUID);
    }

    public void setEventTitle(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, String eventName) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        eventManager.setEventTitle(eventUUID, eventName);
    }

    public void setEventRoom(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        /**
         * TODO: Check to make sure there are no booking conflicts
         */

        eventManager.setEventRoom(eventUUID, roomUUID);
    }

    public void setEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        /**
         * TODO: Check to make sure there are no booking conflicts
         */

        eventManager.setEventTimeRange(eventUUID, timeRange);
    }

    public String getEventTitle(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventTitle(eventUUID);
    }

    public Set<UUID> getEventSpeakers(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventSpeakers(eventUUID);
    }

    public TimeRange getEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventTimeRange(eventUUID);
    }

    public Set<UUID> getEventAttendees(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventAttendees(eventUUID);
    }

    /**
     * Pulls a list of attendees for an event and creates a conversation with them
     * <p>
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID
     * @param executorUUID
     * @param eventUUID
     */
    public void createEventConversation(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        // do stuff
        /**
         * TODO: Implement this
         */
    }

    /* Room operations */
    public Set<UUID> getRooms(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.getRoomManager(conferenceUUID).getRooms();
    }

    public void createRoom(UUID conferenceUUID, UUID executorUUID, String roomNumber, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.createRoom(roomNumber, capacity);
    }

    public void setRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, String roomLocation) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.setRoomLocation(roomUUID, roomLocation);
    }

    public void setRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, int capacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.setRoomCapacity(roomUUID, capacity);
    }

    public void deleteRoom(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        /**
         * TODO: Do not allow this room to be deleted if it is being used by some events
         *       Having an event without a room will cause all sorts of headaches
         */

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.deleteRoom(roomUUID);
    }

    public String getRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getRoomLocation(roomUUID);
    }

    public int getRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getRoomCapacity(roomUUID);
    }

    public Map<UUID, TimeRange> getRoomSchedule(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getCalendarManager(roomUUID).getUUIDtoTimeRanges();
    }

    /* Organizer operations */

    /**
     * Gets a set of UUIDs of organizers.
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
