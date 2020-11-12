package conference;

import conference.calendar.CalendarManager;
import conference.calendar.TimeRange;
import conference.event.EventManager;
import conference.room.RoomManager;
import util.exception.DoubleBookingException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceController {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ConferenceManager conferenceManager = new ConferenceManager();
    private PermissionManager permissionManager = new PermissionManager(conferenceManager);

    /**
     * TODO: Uncomment this when the code is merged and conversation controller is finished
     */
    //private ConversationController conversationController;

    /**
     * Creates an instance of ConferenceController. We store an instance of conversationController so we can
     * send instructions to it to create or mutate conversations that are created for conferences.
     *
     * @param conversationController
     */
    /*
    public ConferenceController(ConferenceController conversationController) {
        this.conversationController = conversationController;
    }*/

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
     * Get all the event UUID to TimeRange pairs for this conference.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return map from event UUID to their respective time ranges.
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
                eventManager.removeSpeaker(eventUUID, targetUserUUID);
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
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param targetUUIDs    UUIDs of the users to add to the conversation
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
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     */
    public Set<UUID> getEvents(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        return eventManager.getEvents(conferenceUUID);
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

        for (UUID eventUUID : conferenceManager.getEvents()) {
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
     * @param conferenceUUID UUID of the conference to operate on
     * @param targetUserUUID UUID of the user to operate on
     * @param eventUUID      UUID of the event to register to
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
    private void updateSpeakers(UUID conferenceUUID) {
        Set<UUID> speakerUUIDs = conferenceManager.getSpeakers(conferenceUUID);
        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        speakerUUIDs.clear();

        for (UUID eventUUID : eventManager.getEvents()) {
            Set<UUID> eventSpeakerUUIDs = eventManager.getEventSpeakers(conferenceUUID, eventUUID);

            speakerUUIDs.addAll(eventSpeakerUUIDs);
        }
    }

    /**
     * Create a new event for this conference. This method will test for scheduling conflicts for both rooms, and speakers.
     *
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
     * @param conferenceUUID UUID of the conference to operate on
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
     * conference.
     *
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

        /**
         * TODO: Remove the speaker to the conversation (if created)
         */

        eventManager.removeEventSpeaker(eventUUID, speakerUUID);
        updateSpeakers(conferenceUUID);
    }

    /**
     * Deletes an event from the conference. Room bookings linked to this event will be cancelled.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
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
     *
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
     *
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
     * Creates a conversation between all attendees of an event and the speakers. Subsequent changes to the roster of
     * speakers and attendees will be reflected in the conversations. (i.e. we will update who is in the chat)
     * <p>
     * Required Permission: SPEAKER
     *
     * @param conferenceUUID UUID of the conference to operate on
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

    /* Room operations */

    /**
     * Gets a set of UUIDs of rooms associated with this conference.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of UUIDs of rooms associated with the conference
     */
    public Set<UUID> getRooms(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.getRoomManager(conferenceUUID).getRooms();
    }

    /**
     * Create a new room for this conference.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomLocation   location of the room
     * @param roomCapacity   capacity of the room
     * @return UUID of the new room
     */
    public UUID createRoom(UUID conferenceUUID, UUID executorUUID, String roomLocation, int roomCapacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.createRoom(roomLocation, roomCapacity);
    }

    /**
     * Sets a new room location.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @param roomLocation   new room location
     */
    public void setRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, String roomLocation) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.setRoomLocation(roomUUID, roomLocation);
    }

    /**
     * Sets a new room capacity.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @param roomCapacity   new room capacity
     */
    public void setRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, int roomCapacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.setRoomCapacity(roomUUID, roomCapacity);
    }

    /**
     * Delete a room. This operation will be rejected if the room is currently used by one or more events.
     *
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     */
    public void deleteRoom(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        /**
         * TODO: Do not allow this room to be deleted if it is being used by some events
         *       Having an event without a room will cause all sorts of headaches
         */

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.deleteRoom(roomUUID);
    }

    /**
     * Gets a room's location.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @return room location
     */
    public String getRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getRoomLocation(roomUUID);
    }

    /**
     * Gets a room's capacity.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @return room capacity
     */
    public int getRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getRoomCapacity(roomUUID);
    }

    /**
     * Get a map of event UUIDs and their respective time ranges for a specific room.
     *
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @return map of event UUIDs to their time range
     */
    public Map<UUID, TimeRange> getRoomSchedule(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getCalendarManager(roomUUID).getUUIDtoTimeRanges();
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
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
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
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of attendee UUIDs
     */
    public Set<UUID> getAttendees(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);
        return conferenceManager.getAttendees(conferenceUUID);
    }
}
