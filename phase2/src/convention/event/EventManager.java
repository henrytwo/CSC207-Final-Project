package convention.event;

import convention.calendar.TimeRange;
import convention.exception.InvalidNameException;
import convention.exception.NullEventException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Manager for Event entities
 */
public class EventManager implements Serializable {
    private Map<UUID, Event> events;

    /**
     * Event Manager constructor
     * <p>
     * holds all events objects for this conference
     *
     * @param events map of UUIDs for all events in this conference
     */
    public EventManager(Map<UUID, Event> events) {
        this.events = events;
    }

    /**
     * Gets the set of all events in this conference
     *
     * @return the set of all events in this conference
     */
    public Set<UUID> getEvents() {
        return new HashSet<>(events.keySet());
    }

    /**
     * checks whether an events is in this conference
     *
     * @param eventUUID the events being checked
     * @return True if events is in this.events, false otherwise
     */
    public boolean eventExists(UUID eventUUID) {
        return events.containsKey(eventUUID);
    }

    /**
     * Event titles must be non-empty, this methods checks for this
     *
     * @param title
     * @return True iff the name is valid
     */
    private boolean validateEventTitle(String title) {
        return title.length() > 0;
    }

    /**
     * Gets the events with the given UUID
     *
     * @param eventUUID UUID of the events
     * @return The events with the given UUID
     */
    public Event getEvent(UUID eventUUID) {
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }
        return events.get(eventUUID);
    }

    /**
     * Creates a new events
     *
     * @param title        title of this events
     * @param timeRange    TimeRange of this events
     * @param roomUUID     room hosting this events
     * @param speakerUUIDs speakers of this events
     * @return the UUID of this events
     */
    public UUID createEvent(String title, TimeRange timeRange, UUID roomUUID, Set<UUID> speakerUUIDs) {
        if (!validateEventTitle(title)) {
            throw new InvalidNameException();
        }

        Event event = new Event(title, timeRange, roomUUID, speakerUUIDs);
        events.put(event.getUUID(), event);

        return event.getUUID();
    }

    /**
     * removes an events from this conference
     *
     * @param eventUUID UUID of events being removed
     */
    public void deleteEvent(UUID eventUUID) {
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }

        events.remove(eventUUID);
    }

    /**
     * gets the set of speakers for an events
     *
     * @param eventUUID UUID of events
     * @return set of UUIDs of speakers for this events
     */
    public Set<UUID> getEventSpeakers(UUID eventUUID) {
        return new HashSet<>(getEvent(eventUUID).getSpeakers());
    }

    /**
     * adds a speaker to an events
     *
     * @param eventUUID   UUID of this events
     * @param speakerUUID UUID of speaker being added
     */
    public void addEventSpeaker(UUID eventUUID, UUID speakerUUID) {
        if (!getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).addSpeaker(speakerUUID);
        }
    }

    /**
     * removes a speaker from an events
     *
     * @param eventUUID   UUID of this events
     * @param speakerUUID UUID of speaker being removed
     */
    public void removeEventSpeaker(UUID eventUUID, UUID speakerUUID) {
        if (getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).removeSpeaker(speakerUUID);
        }
    }

    /**
     * Changes the title of an events
     *
     * @param eventUUID  UUID of this events
     * @param eventTitle String of the new title
     */
    public void setEventTitle(UUID eventUUID, String eventTitle) {
        if (!validateEventTitle(eventTitle)) {
            throw new InvalidNameException();
        }
        getEvent(eventUUID).setTitle(eventTitle);
    }

    /**
     * Changes the room hosting an events
     *
     * @param eventUUID UUID of events in question
     * @param roomUUID  UUID of new room
     */
    public void setEventRoom(UUID eventUUID, UUID roomUUID) {
        getEvent(eventUUID).setRoomUUID(roomUUID);
    }

    /**
     * Changes the time of an events
     *
     * @param eventUUID UUID of this events
     * @param timeRange new TimeRange of this events
     */
    public void setEventTimeRange(UUID eventUUID, TimeRange timeRange) {
        getEvent(eventUUID).setTimeRange(timeRange);
    }

    /**
     * Get the title of an events
     *
     * @param eventUUID UUID of this events
     * @return String of title name
     */
    public String getEventTitle(UUID eventUUID) {
        return getEvent(eventUUID).getTitle();
    }

    /**
     * Gets the room holding this events
     *
     * @param eventUUID UUID of this events
     * @return UUID of the room hosting this events
     */
    public UUID getEventRoom(UUID eventUUID) {
        return getEvent(eventUUID).getRoomUUID();
    }

    /**
     * Gets the time of an events
     *
     * @param eventUUID UUID of this events
     * @return the TimeRange of this events
     */
    public TimeRange getEventTimeRange(UUID eventUUID) {
        return getEvent(eventUUID).getTimeRange();
    }


    /**
     * Gets the conversation for an events
     *
     * @param eventUUID UUID of events in question
     * @return UUID of this event's conversation
     */
    public UUID getEventConversationUUID(UUID eventUUID) {
        return getEvent(eventUUID).getConversationUUID();
    }

    /**
     * Change the conversation for an events
     *
     * @param eventUUID        UUID of this events
     * @param conversationUUID UUID of new conversation
     */
    public void setEventConversationUUID(UUID eventUUID, UUID conversationUUID) {
        getEvent(eventUUID).setConversationUUID(conversationUUID);
    }

    /**
     * Gets the set of all users registered for an events
     *
     * @param eventUUID UUID of this events
     * @return set of UUIDs of users registered for this events
     */
    public Set<UUID> getEventAttendees(UUID eventUUID) {
        return new HashSet<>(getEvent(eventUUID).getAttendeeUUIDs());
    }

    /**
     * registers a new attendee for this events
     *
     * @param eventUUID    UUID of this events
     * @param attendeeUUID UUID of the user being registered
     */
    public void registerAttendee(UUID eventUUID, UUID attendeeUUID) {
        getEvent(eventUUID).addAttendee(attendeeUUID);
    }

    /**
     * removes an attendee from an events
     *
     * @param eventUUID    UUID of this events
     * @param attendeeUUID UUID of user being removed
     */
    public void unregisterAttendee(UUID eventUUID, UUID attendeeUUID) {
        getEvent(eventUUID).removeAttendee(attendeeUUID);
    }

}
