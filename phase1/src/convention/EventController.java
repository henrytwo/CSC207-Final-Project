package convention;

import convention.calendar.CalendarManager;
import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.event.EventManager;
import convention.exception.CalendarDoubleBookingException;
import convention.exception.FullEventException;
import convention.exception.SpeakerDoubleBookingException;
import convention.permission.PermissionManager;
import convention.room.RoomManager;
import messaging.ConversationManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Operations on Events
 */
public class EventController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ConferenceManager conferenceManager;
    private PermissionManager permissionManager;
    private ConversationManager conversationManager;

    public EventController(ConferenceManager conferenceManager, ConversationManager conversationManager) {
        this.conferenceManager = conferenceManager;
        this.conversationManager = conversationManager;
        this.permissionManager = new PermissionManager(conferenceManager);
    }

    /**
     * Get a list of events. A user must be an attendee of the parent conference to view events.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
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
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
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
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
     */
    public void registerForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        int currentEventAttendeeCount = eventManager.getEventAttendees(eventUUID).size();
        UUID roomUUID = eventManager.getEventRoom(eventUUID);

        // Verify the event can take additional attendees
        if (currentEventAttendeeCount + 1 > roomManager.getRoomCapacity(roomUUID)) {
            throw new FullEventException();
        }

        // If this event has a conversation between the speaker and attendees, add this user to it
        UUID eventConversationUUID = eventManager.getEventConversationUUID(eventUUID);

        if (eventConversationUUID != null) {
            conversationManager.addUser(targetUserUUID, eventConversationUUID);
        }

        eventManager.registerAttendee(eventUUID, targetUserUUID);
    }

    /**
     * Actually executes the unregister operation. We have a separate helper method here so that we don't forget
     * to run the check to remove the user from the event's conversation.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
     */
    void doUnregisterForEvent(UUID conferenceUUID, UUID targetUserUUID, UUID eventUUID) {
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        // If this event has a conversation between the speaker and attendees, remove the user from it
        UUID eventConversationUUID = eventManager.getEventConversationUUID(eventUUID);

        if (eventConversationUUID != null) {
            conversationManager.removeUser(targetUserUUID, eventConversationUUID);
        }

        eventManager.unregisterAttendee(eventUUID, targetUserUUID);
    }

    /**
     * Unregister for an event. A user must be an attendee of the parent conference to unregister.
     * <p>
     * Required Permission: ATTENDEE (self) or ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
     */
    public void unregisterForEvent(UUID conferenceUUID, UUID executorUUID, UUID targetUserUUID, UUID eventUUID) {
        permissionManager.testIsAttendeeSelfOrAdmin(conferenceUUID, executorUUID, targetUserUUID);
        doUnregisterForEvent(conferenceUUID, targetUserUUID, eventUUID);
    }

    /**
     * Helper function to keep the list of speakers at a conference in sync
     *
     * @param conferenceUUID UUID of the conference to operate on
     */
    void updateSpeakers(UUID conferenceUUID) {
        Set<UUID> speakerUUIDs = new HashSet<>();
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        for (UUID eventUUID : eventManager.getEvents()) {
            Set<UUID> eventSpeakerUUIDs = eventManager.getEventSpeakers(eventUUID);

            speakerUUIDs.addAll(eventSpeakerUUIDs);
        }

        conferenceManager.setSpeakers(conferenceUUID, speakerUUIDs);
    }

    /**
     * Tests whether a speaker is scheduled to talk at a given time range.
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param speakerUUID    UUID of the speaker to test
     * @param timeRange      TimeRange to test for overlap
     * @return true iff the speaker is not available at the given time range
     */
    private boolean speakerTimeRangeOccupied(UUID conferenceUUID, UUID speakerUUID, TimeRange timeRange) {
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        for (UUID eventUUID : eventManager.getEvents()) {
            Set<UUID> speakerUUIDs = eventManager.getEventSpeakers(eventUUID);

            // If the speaker is linked to this event, we want to test if there's a conflict
            if (speakerUUIDs.contains(speakerUUID)) {
                TimeRange eventTimeRange = eventManager.getEventTimeRange(eventUUID);

                if (eventTimeRange.hasOverlap(timeRange)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Tests whether a set of speakers have scheduling conflicts
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param speakerUUIDs   UUIDs of the speakers to test
     * @param timeRange      TimeRange to test for overlap
     * @throws SpeakerDoubleBookingException iff there is at least one speaker which has a double booking
     */
    private void testSpeakersTimeRangeOccupied(UUID conferenceUUID, Set<UUID> speakerUUIDs, TimeRange timeRange) {
        for (UUID speakerUUID : speakerUUIDs) {
            if (speakerTimeRangeOccupied(conferenceUUID, speakerUUID, timeRange)) {
                throw new SpeakerDoubleBookingException();
            }
        }
    }

    /**
     * Create a new event for this conference. This method will test for scheduling conflicts for both rooms, and speakers.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
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

        // Test that the speakers are not being double booked
        testSpeakersTimeRangeOccupied(conferenceUUID, speakerUUIDs, timeRange);

        // Test that the room is not being double booked
        if (roomCalendarManager.timeRangeOccupied(timeRange)) {
            throw new CalendarDoubleBookingException();
        } else {
            UUID eventUUID = eventManager.createEvent(eventName, timeRange, roomUUID, speakerUUIDs);

            roomCalendarManager.addTimeBlock(eventUUID, timeRange);
            updateSpeakers(conferenceUUID);

            return eventUUID;
        }
    }

    /**
     * Add a speaker to the event. This method will perform a check to ensure there are no scheduling conflicts.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param speakerUUID    UUID of the speaker to add
     */
    public void addEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        TimeRange eventTimeRange = eventManager.getEventTimeRange(eventUUID);

        // Test that the speaker is not being double booked
        testSpeakersTimeRangeOccupied(conferenceUUID, new HashSet<UUID>() {
            {
                add(speakerUUID);
            }
        }, eventTimeRange);

        // Add speaker to the existing conversation for this event
        UUID eventConversationUUID = eventManager.getEventConversationUUID(eventUUID);

        if (eventConversationUUID != null) {
            conversationManager.addUser(speakerUUID, eventConversationUUID);
        }

        eventManager.addEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    /**
     * Remove a speaker from the event. If this was their only event, then speaker permissions will be revoked for the
     * conference.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param speakerUUID    UUID of the speaker to add
     */
    public void removeEventSpeaker(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID speakerUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        // Remove speaker to the existing conversation for this event
        UUID eventConversationUUID = eventManager.getEventConversationUUID(eventUUID);

        if (eventConversationUUID != null) {
            conversationManager.removeUser(speakerUUID, eventConversationUUID);
        }
        eventManager.removeEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    /**
     * Deletes an event from the conference. Room bookings linked to this event will be cancelled.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     */
    public void deleteEvent(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        // Free up the room booking
        UUID roomUUID = eventManager.getEventRoom(eventUUID);
        CalendarManager roomCalendarManager = roomManager.getCalendarManager(roomUUID);
        roomCalendarManager.removeTimeBlock(eventUUID);

        // Delete the conversation corresponding to this event
        UUID eventConversationUUID = eventManager.getEventConversationUUID(eventUUID);

        if (eventConversationUUID != null) {
            conversationManager.deleteConversation(eventConversationUUID);
        }

        eventManager.deleteEvent(eventUUID);
        updateSpeakers(conferenceUUID);
    }

    /**
     * Set a new title for this event.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param eventTitle     new event title
     */
    public void setEventTitle(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, String eventTitle) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        eventManager.setEventTitle(eventUUID, eventTitle);
    }

    /**
     * Set a new room for this event. This method will perform a check to ensure there are no booking conflicts.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param newRoomUUID    UUID of the new room
     */
    public void setEventRoom(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, UUID newRoomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        TimeRange eventTimeRange = eventManager.getEventTimeRange(eventUUID);
        UUID oldRoomUUID = eventManager.getEventRoom(eventUUID);

        CalendarManager newRoomCalendarManager = roomManager.getCalendarManager(newRoomUUID);
        CalendarManager oldRoomCalendarManager = roomManager.getCalendarManager(oldRoomUUID);

        // Test that the room is not being double booked
        if (newRoomCalendarManager.timeRangeOccupied(eventTimeRange)) {
            throw new CalendarDoubleBookingException();
        } else {
            // Cancel the booking from the old room
            oldRoomCalendarManager.removeTimeBlock(eventUUID);

            // Create the new booking
            newRoomCalendarManager.addTimeBlock(eventUUID, eventTimeRange);

            // Update the event with the new room UUID
            eventManager.setEventRoom(eventUUID, newRoomUUID);
        }
    }

    /**
     * Set a new time range for this event. This method will perform tests to ensure there are no booking conflicts.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @param timeRange      new time range
     */
    public void setEventTimeRange(UUID conferenceUUID, UUID executorUUID, UUID eventUUID, TimeRange timeRange) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        UUID roomUUID = eventManager.getEventRoom(eventUUID);
        Set<UUID> speakerUUIDs = eventManager.getEventSpeakers(eventUUID);

        CalendarManager roomCalendarManager = roomManager.getCalendarManager(roomUUID);

        // Test that there are no speaker conflicts
        testSpeakersTimeRangeOccupied(conferenceUUID, speakerUUIDs, timeRange);

        // Test that the room is not being double booked
        if (roomCalendarManager.timeRangeOccupied(timeRange)) {
            throw new CalendarDoubleBookingException();
        } else {
            // Cancel the booking
            roomCalendarManager.removeTimeBlock(eventUUID);

            // Create the new booking
            roomCalendarManager.addTimeBlock(eventUUID, timeRange);
        }

        eventManager.setEventTimeRange(eventUUID, timeRange);
    }

    /**
     * Get the room UUID for this event;
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return UUID of the event room
     */
    public UUID getEventRoom(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        return eventManager.getEventRoom(eventUUID);
    }

    /**
     * Get the event conversation UUID for this event;
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return UUID of the event conversation, or null if not available
     */
    public UUID getEventConversationUUID(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        return eventManager.getEventConversationUUID(eventUUID);
    }

    /**
     * Returns if the executor is registered for this event
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     */
    public boolean isRegistered(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        return eventManager.getEventAttendees(eventUUID).contains(executorUUID);
    }

    /**
     * Get the event title.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return event title
     */
    public String getEventTitle(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventTitle(eventUUID);
    }

    /**
     * Gets a set of UUIDs of speakers at this event.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
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
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
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
     * <p>
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID UUID of the conference to operate on
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
     * Get the number of attendees registered for an event.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return set of attendee UUIDs
     */
    public int getNumRegistered(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);

        return eventManager.getEventAttendees(eventUUID).size();
    }

    /**
     * Creates a conversation between all attendees of an event and the speakers. Subsequent changes to the roster of
     * speakers and attendees will be reflected in the conversations. (i.e. we will update who is in the chat)
     * <p>
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param eventUUID      UUID of the event to operate on
     * @return UUID of the new conversation
     */
    public UUID createEventConversation(UUID conferenceUUID, UUID executorUUID, UUID eventUUID) {
        permissionManager.testIsSpeaker(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        String eventTitle = eventManager.getEventTitle(eventUUID);
        String conferenceName = conferenceManager.getConferenceName(conferenceUUID);

        String conversationName = String.format("%s Event Chat @ %s", eventTitle, conferenceName);

        // Give all event speaker and attendees read and write access to the converastion
        Set<UUID> conversationUsers = new HashSet<>();
        conversationUsers.addAll(eventManager.getEventAttendees(eventUUID));
        conversationUsers.addAll(eventManager.getEventSpeakers(eventUUID));

        UUID conversationUUID = conversationManager.createConversation(conversationName, conversationUsers, conversationUsers, executorUUID, String.format("Welcome to the event: %s", eventTitle));

        // Save the conversation for future reference
        eventManager.setEventConversationUUID(eventUUID, conversationUUID);

        return conversationUUID;
    }
}
