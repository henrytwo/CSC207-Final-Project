package convention.event;

import conference.calendar.TimeRange;
import util.exception.NullEventException;

import java.util.*;

public class EventManager {
    private Map<UUID, Event> events = new HashMap<>();

    public EventManager(Map<UUID, Event> events) { this.events = events; }

    public Set<UUID> getEvents() { return events.keySet(); }

    public boolean eventExists(UUID eventUUID) { return events.containsKey(eventUUID); }

    public Event getEvent(UUID eventUUID){
        if(!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        return events.get(eventUUID);
    }

    public UUID createEvent(String title, TimeRange timeRange, UUID roomUUID, Set<UUID> speakerUUIDs){
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
        if (!eventExists(eventUUID)) {
            throw new NullEventException(eventUUID);
        }

        return getEvent(eventUUID).getSpeakers();
    }

    public void addEventSpeaker(UUID eventUUID, UUID speakerUUID){
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        if (!getEvent(eventUUID).isSpeaker(speakerUUID)) {
            getEvent(eventUUID).addSpeaker(speakerUUID);
        }
    }

    public void removeEventSpeaker(UUID eventUUID, UUID speakerUUID){
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        if (getEvent(eventUUID).isSpeaker(speakerUUID)){
            getEvent(eventUUID).removeSpeaker(speakerUUID);
        }
    }

    public void setEventTitle(UUID eventUUID, String eventTitle) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }

        getEvent(eventUUID).setTitle(eventTitle);
    }

    public void setEventRoom(UUID eventUUID, UUID roomUUID) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        getEvent(eventUUID).setRoomUUID(roomUUID);
    }

    public void setEventTimeRange(UUID eventUUID, TimeRange timeRange) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        getEvent(eventUUID).setTimeRange(timeRange);
    }

    public UUID getConversationUUID(UUID eventUUID) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        return getEvent(eventUUID).getConversationUUID();
    }

    public Set<UUID> getEventAttendees(UUID eventUUID) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        return getEvent(eventUUID).getAttendeeUUIDs();
    }

    public void registerAttendee(UUID eventUUID, UUID attendeeUUID) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        if (!getEvent(eventUUID).isAttendee(attendeeUUID)) {
            getEvent(eventUUID).addAttendee(attendeeUUID);
        }
    }

    public void unregisterAttendee(UUID eventUUID, UUID attendeeUUID) {
        if (!eventExists(eventUUID)){
            throw new NullEventException(eventUUID);
        }
        if (getEvent(eventUUID).isAttendee(attendeeUUID)) {
            getEvent(attendeeUUID).removeAttendee(attendeeUUID);
        }
    }

}
