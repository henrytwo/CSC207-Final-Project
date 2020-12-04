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
public class  EventManager implements Serializable {
    private Map<UUID, Event> events;

    /**
     * Event Manager constructor
     * <p>
     * holds all event objects for this conference
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
     * checks whether an event is in this conference
     *
     * @param eventUUID the event being checked
     * @return True if event is in this.events, false otherwise
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
     * Gets the event with the given UUID
     *
     * @param eventUUID UUID of the event
     * @return The event with the given UUID
     */
    public Event getEvent(UUID eventUUID) {
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }
        return events.get(eventUUID);
    }

    /**
     * Creates a new event
     *
     * @param title        title of this event
     * @param timeRange    TimeRange of this event
     * @param roomUUID     room hosting this event
     * @param speakerUUIDs speakers of this event
     * @return the UUID of this event
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
     * removes an event from this conference
     *
     * @param eventUUID UUID of event being removed
     */
    public void deleteEvent(UUID eventUUID) {
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }

        events.remove(eventUUID);
    }

    /**
     * gets the set of speakers for an event
     *
     * @param eventUUID UUID of event
     * @return set of UUIDs of speakers for this event
     */
    public Set<UUID> getEventSpeakers(UUID eventUUID) {
        return new HashSet<>(getEvent(eventUUID).getSpeakers());
    }

    /**
     * adds a speaker to an event
     *
     * @param eventUUID   UUID of this event
     * @param speakerUUID UUID of speaker being added
     */
    public void addEventSpeaker(UUID eventUUID, UUID speakerUUID) {
        if (!getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).addSpeaker(speakerUUID);
        }
    }

    /**
     * removes a speaker from an event
     *
     * @param eventUUID   UUID of this event
     * @param speakerUUID UUID of speaker being removed
     */
    public void removeEventSpeaker(UUID eventUUID, UUID speakerUUID) {
        if (getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).removeSpeaker(speakerUUID);
        }
    }

    /**
     * Changes the title of an event
     *
     * @param eventUUID  UUID of this event
     * @param eventTitle String of the new title
     */
    public void setEventTitle(UUID eventUUID, String eventTitle) {
        if (!validateEventTitle(eventTitle)) {
            throw new InvalidNameException();
        }
        getEvent(eventUUID).setTitle(eventTitle);
    }

    /**
     * Changes the room hosting an event
     *
     * @param eventUUID UUID of event in question
     * @param roomUUID  UUID of new room
     */
    public void setEventRoom(UUID eventUUID, UUID roomUUID) {
        getEvent(eventUUID).setRoomUUID(roomUUID);
    }

    /**
     * Changes the time of an event
     *
     * @param eventUUID UUID of this event
     * @param timeRange new TimeRange of this event
     */
    public void setEventTimeRange(UUID eventUUID, TimeRange timeRange) {
        getEvent(eventUUID).setTimeRange(timeRange);
    }

    /**
     * Get the title of an event
     *
     * @param eventUUID UUID of this event
     * @return String of title name
     */
    public String getEventTitle(UUID eventUUID) {
        return getEvent(eventUUID).getTitle();
    }

    /**
     * Gets the room holding this event
     *
     * @param eventUUID UUID of this event
     * @return UUID of the room hosting this event
     */
    public UUID getEventRoom(UUID eventUUID) {
        return getEvent(eventUUID).getRoomUUID();
    }

    /**
     * Gets the time of an event
     *
     * @param eventUUID UUID of this event
     * @return the TimeRange of this event
     */
    public TimeRange getEventTimeRange(UUID eventUUID) {
        return getEvent(eventUUID).getTimeRange();
    }


    /**
     * Gets the conversation for an event
     *
     * @param eventUUID UUID of event in question
     * @return UUID of this event's conversation
     */
    public UUID getEventConversationUUID(UUID eventUUID) {
        return getEvent(eventUUID).getConversationUUID();
    }

    /**
     * Change the conversation for an event
     *
     * @param eventUUID        UUID of this event
     * @param conversationUUID UUID of new conversation
     */
    public void setEventConversationUUID(UUID eventUUID, UUID conversationUUID) {
        getEvent(eventUUID).setConversationUUID(conversationUUID);
    }

    /**
     * Gets the set of all users registered for an event
     *
     * @param eventUUID UUID of this event
     * @return set of UUIDs of users registered for this event
     */
    public Set<UUID> getEventAttendees(UUID eventUUID) {
        return new HashSet<>(getEvent(eventUUID).getAttendeeUUIDs());
    }

    /**
     * registers a new attendee for this event
     *
     * @param eventUUID    UUID of this event
     * @param attendeeUUID UUID of the user being registered
     */
    public void registerAttendee(UUID eventUUID, UUID attendeeUUID) {
        getEvent(eventUUID).addAttendee(attendeeUUID);
    }

    /**
     * removes an attendee from an event
     *
     * @param eventUUID    UUID of this event
     * @param attendeeUUID UUID of user being removed
     */
    public void unregisterAttendee(UUID eventUUID, UUID attendeeUUID) {
        getEvent(eventUUID).removeAttendee(attendeeUUID);
    }

}
