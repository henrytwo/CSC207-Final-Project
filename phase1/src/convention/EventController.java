package convention;

import convention.conference.ConferenceManager;
import convention.calendar.CalendarManager;
import convention.calendar.TimeRange;
import convention.event.EventManager;
import convention.permission.PermissionManager;
import convention.room.RoomManager;
import util.exception.DoubleBookingException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class EventController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ConferenceManager conferenceManager;
    private PermissionManager permissionManager;

    public EventController(ConferenceManager conferenceManager, PermissionManager permissionManager) {
        this.conferenceManager = conferenceManager;
        this.permissionManager = permissionManager;
    }

    /**
     * Get a list of events. A user must be an attendee of the parent convention to view events.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public Set<UUID> getEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        return eventManager.getEvents(conferenceUUID);
    }

    /**
     * Get a list of events an attendee is registered in. A user must be an attendee of the parent convention to view events.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
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
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public Set<UUID> getSpeakerEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        Set<UUID> registeredEventsUUIDs = new HashSet<>();
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        for (UUID eventUUID : conferenceManager.getEvents()) {
            if (eventManager.getEventSpeakers(eventUUID).contains(executorUUID)) {
                registeredEventsUUIDs.add(eventUUID);
            }
        }

        return registeredEventsUUIDs;
    }

    /**
     * Sign up for an event. A user must be an attendee of the parent convention to sign up.
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
     */
    public void registerForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        /**
         * TODO: VERIFY THAT THERE IS ENOUGH SPACE FIRST
         */

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
     * @param conferenceUUID UUID of the convention to operate on
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
     */
    void doUnregisterForEvent(UUID conferenceUUID, UUID targetUserUUID, UUID eventUUID) {
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        if (eventManager.getConversationUUID(eventUUID) != null) {
            /**
             * TODO: Remove the user from the groupchat
             */
        }

        eventManager.unregisterAttendee(eventUUID, targetUserUUID);
    }

    /**
     * Unregister for an event. A user must be an attendee of the parent convention to unregister.
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
     */
    public void unregisterForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        doUnregisterForEvent(conferenceUUID, targetUserUUID, eventUUID);
    }

    /**
     * Helper function to keep the list of speakers at a convention in sync
     *
     * @param conferenceUUID UUID of the convention to operate on
     */
    void updateSpeakers(UUID conferenceUUID) {
        Set<UUID> speakerUUIDs = conferenceManager.getSpeakers(conferenceUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        speakerUUIDs.clear();

        for (UUID eventUUID : eventManager.getEvents()) {
            Set<UUID> eventSpeakerUUIDs = eventManager.getEventSpeakers(conferenceUUID, eventUUID);

            speakerUUIDs.addAll(eventSpeakerUUIDs);
        }
    }

    /**
     * Create a new event for this convention. This method will test for scheduling conflicts for both rooms, and speakers.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventName      name of the event
     * @param timeRange      TimeRange of the event
     * @param roomUUID       UUID of the room to host this event in
     * @param speakerUUIDs   set of UUIDs of speakers to assign to this event
     * @return UUID of the new event
     */
    public UUID createEvent(UUID conferenceUUID, UUID executorUUID, String eventName, TimeRange timeRange, UUID roomUUID, Set<UUID> speakerUUIDs) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        CalendarManager roomCalendarManager = roomManager.getCalendarManager(roomUUID);

        /**
         * TODO: Test if there is a booking conflict for the speaker
         */

        /**
         * We need to make sure there is no other event booked at this time
         */
        if (roomCalendarManager.timeRangeOccupied(timeRange)) {
            throw new DoubleBookingException();
        } else {
            UUID eventUUID = eventManager.createEvent(eventName, timeRange, roomUUID, speakerUUIDs);

            roomCalendarManager.addTimeBlock(eventUUID, timeRange);
            updateSpeakers(conferenceUUID);

            return eventUUID;
        }
    }

    /**
     * Add a speaker to the event. This method will perform a check to ensure there are no scheduling conflicts.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param speakerUUID    UUID of the speaker to add
     */
    public void addEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        /**
         * TODO: Test if there is a booking conflict for the speaker
         */

        /**
         * TODO: Add the speaker to the conversation (if created)
         */

        eventManager.addEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    /**
     * Remove a speaker from the event. If this was their only event, then speaker permissions will be revoked for the
     * convention.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param speakerUUID    UUID of the speaker to add
     */
    public void removeEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        /**
         * TODO: Remove the speaker to the conversation (if created)
         */

        eventManager.removeEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    /**
     * Deletes an event from the convention. Room bookings linked to this event will be cancelled.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     */
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

    /**
     * Set a new name for this event.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param eventName      new event name
     */
    public void setEventName(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, String eventName) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        eventManager.setEventName(eventUUID, eventName);
    }

    /**
     * Set a new room for this event. This method will perform a check to ensure there are no booking conflicts.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param roomUUID       UUID of the new room
     */
    public void setEventRoom(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        /**
         * TODO: Check to make sure there are no booking conflicts
         */

        eventManager.setEventRoom(eventUUID, roomUUID);
    }

    /**
     * Set a new time range for this event. This method will perform tests to ensure there are no booking conflicts.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param timeRange      new time range
     */
    public void setEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        /**
         * TODO: Check to make sure there are no booking conflicts
         */

        eventManager.setEventTimeRange(eventUUID, timeRange);
    }

    /**
     * Get the event name.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return event name
     */
    public String getEventName(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventName(eventUUID);
    }

    /**
     * Gets a set of UUIDs of speakers at this event.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return set of speaker UUIDs
     */
    public Set<UUID> getEventSpeakers(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventSpeakers(eventUUID);
    }

    /**
     * Get the TimeRange for this event.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return get the time range for this event
     */
    public TimeRange getEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventTimeRange(eventUUID);
    }

    /**
     * Get a set of attendees for this event.
     *
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return set of attendee UUIDs
     */
    public Set<UUID> getEventAttendees(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventAttendees(eventUUID);
    }

    /**
     * Creates a conversation between all attendees of an event and the speakers. Subsequent changes to the roster of
     * speakers and attendees will be reflected in the conversations. (i.e. we will update who is in the chat)
     * <p>
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID UUID of the convention to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     */
    public void createEventConversation(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        // do stuff
        /**
         * TODO: Implement this
         */
    }
}