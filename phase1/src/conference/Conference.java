package conference;

import calendar.Calendar;
import conference.event.Event;
import conference.room.Room;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Conference {
    private Set<UUID> organizerUUIDs = new HashSet<>();
    private Set<UUID> speakerUUIDs = new HashSet<>();

    private Set<Event> events = new HashSet<>();
    private Set<Room> rooms = new HashSet<>();

    private String conferenceName;
    private UUID uuid;

    private Calendar calendar = new Calendar();

    private LocalDateTime start;
    private LocalDateTime end;

    public Conference(String conferenceName, LocalDateTime start, LocalDateTime end, UUID organizerUUID) {
        this.conferenceName = conferenceName;
        this.uuid = UUID.randomUUID();
        this.start = start;
        this.end = end;
        this.organizerUUIDs.add(organizerUUID);
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public UUID getUuid() {
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
}
