package convention.event;

import convention.calendar.TimeRange;
import util.exception.InvalidNameException;
import util.exception.NullEventException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class EventManager {
    private Map<UUID, Event> events;

    public EventManager(Map<UUID, Event> events) {
        this.events = events;
    }

    public Set<UUID> getEvents() {
        return events.keySet();
    }

    public boolean eventExists(UUID eventUUID) {
        return events.containsKey(eventUUID);
    }

    private boolean validateEventTitle(String title) {
        return title.length() > 0;
    }

    public Event getEvent(UUID eventUUID) {
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }
        return events.get(eventUUID);
    }

    public UUID createEvent(String title, TimeRange timeRange, UUID roomUUID, Set<UUID> speakerUUIDs) {
        Event event = new Event(title, timeRange, roomUUID, speakerUUIDs);
        events.put(event.getUUID(), event);

        return event.getUUID();
    }

    public void deleteEvent(UUID eventUUID) {
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }

        events.remove(eventUUID);
    }

    public Set<UUID> getEventSpeakers(UUID eventUUID) {
        return getEvent(eventUUID).getSpeakers();
    }

    public void addEventSpeaker(UUID eventUUID, UUID speakerUUID) {
        if (!getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).addSpeaker(speakerUUID);
        }
    }

    public void removeEventSpeaker(UUID eventUUID, UUID speakerUUID) {
        if (getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).removeSpeaker(speakerUUID);
        }
    }

    public void setEventTitle(UUID eventUUID, String eventTitle) {
        if (!validateEventTitle(eventTitle)) {
            throw new InvalidNameException();
        }
        getEvent(eventUUID).setTitle(eventTitle);
    }

    public void setEventRoom(UUID eventUUID, UUID roomUUID) {
        getEvent(eventUUID).setRoomUUID(roomUUID);
    }

    public void setEventTimeRange(UUID eventUUID, TimeRange timeRange) {
        getEvent(eventUUID).setTimeRange(timeRange);
    }

    public String getEventTitle(UUID eventUUID) {
        return getEvent(eventUUID).getTitle();
    }

    public UUID getEventRoom(UUID eventUUID) {
        return getEvent(eventUUID).getRoomUUID();
    }

    public TimeRange getEventTimeRange(UUID eventUUID) {
        return getEvent(eventUUID).getTimeRange();
    }

    public UUID getConversationUUID(UUID eventUUID) {
        return getEvent(eventUUID).getConversationUUID();
    }

    public Set<UUID> getEventAttendees(UUID eventUUID) {
        return getEvent(eventUUID).getAttendeeUUIDs();
    }

    public void registerAttendee(UUID eventUUID, UUID attendeeUUID) {
        if (!getEvent(eventUUID).isAttendee(attendeeUUID)) {
            getEvent(eventUUID).addAttendee(attendeeUUID);
        }
    }

    public void unregisterAttendee(UUID eventUUID, UUID attendeeUUID) {
        if (getEvent(eventUUID).isAttendee(attendeeUUID)) {
            getEvent(attendeeUUID).removeAttendee(attendeeUUID);
        }
    }

}
