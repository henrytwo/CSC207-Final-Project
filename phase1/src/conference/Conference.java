package conference;

import conference.calendar.Calendar;
import conference.calendar.TimeRange;
import conference.event.Event;
import conference.room.Room;

import java.time.LocalDateTime;
import java.util.*;

public class Conference {
    private Set<UUID> organizerUUIDs = new HashSet<>();
    private Set<UUID> speakerUUIDs = new HashSet<>();
    private Set<UUID> attendeeUUIDs = new HashSet<>();

    private Map<UUID, Event> events = new HashMap<>();
    private Map<UUID, Room> rooms = new HashMap<>();

    private String conferenceName;
    private UUID uuid;

    private Calendar calendar = new Calendar();

    private TimeRange timeRange;

    public Conference(String conferenceName, TimeRange timeRange, UUID organizerUUID) {
        this.conferenceName = conferenceName;
        this.uuid = UUID.randomUUID();
        this.timeRange = timeRange;
        this.organizerUUIDs.add(organizerUUID);
    }

    public Map<UUID, Event> getEvents() {
        return events;
    }

    public Map<UUID, Room> getRooms() {
        return rooms;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean isOrganizer(UUID organizerUUID) {
        return organizerUUIDs.contains(organizerUUID);
    }

    public Set<UUID> getOrganizerUUIDs() {
        return organizerUUIDs;
    }

    public void addOrganizer(UUID organizerUUID) {
        organizerUUIDs.add(organizerUUID);
    }

    public boolean removeOrganizer(UUID organizerUUID) {
        return organizerUUIDs.remove(organizerUUID);
    }

    public boolean isSpeaker(UUID speakerUUID) {
        return speakerUUIDs.contains(speakerUUID);
    }

    public Set<UUID> getSpeakerUUIDs() {
        return speakerUUIDs;
    }

    public void addSpeaker(UUID speakerUUID) {
        speakerUUIDs.add(speakerUUID);
    }

    public boolean removeSpeaker(UUID speakerUUID) {
        return speakerUUIDs.remove(speakerUUID);
    }

    public boolean isAttendee(UUID attendeeUUID) {
        return attendeeUUIDs.contains(attendeeUUID);
    }

    public Set<UUID> getAttendeeUUIDs() {
        return attendeeUUIDs;
    }

    public void addAttendee(UUID attendeeUUID) {
        attendeeUUIDs.add(attendeeUUID);
    }

    public boolean removeAttendee(UUID attendeeUUID) {
        return attendeeUUIDs.remove(attendeeUUID);
    }
}
