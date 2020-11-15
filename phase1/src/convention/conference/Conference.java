package convention.conference;

import convention.calendar.TimeRange;
import convention.event.Event;
import convention.room.Room;

import java.io.Serializable;
import java.util.*;

public class Conference implements Serializable {
    private Set<UUID> organizerUUIDs = new HashSet<>();
    private Set<UUID> speakerUUIDs = new HashSet<>();
    private Set<UUID> attendeeUUIDs = new HashSet<>();

    private Map<UUID, Event> events = new HashMap<>();
    private Map<UUID, Room> rooms = new HashMap<>();

    private String conferenceName;
    private UUID uuid;

    private TimeRange timeRange;

    /**
     * Conference constructor.
     *
     * @param conferenceName name of new convention
     * @param timeRange      TimeRange of the convention
     * @param organizerUUID  UUID of the initial organizer
     */
    public Conference(String conferenceName, TimeRange timeRange, UUID organizerUUID) {
        this.conferenceName = conferenceName;
        this.uuid = UUID.randomUUID();
        this.timeRange = timeRange;
        this.organizerUUIDs.add(organizerUUID);
    }

    /**
     * Gets events associated with this convention
     *
     * @return Map from Event UUID to Event object
     */
    public Map<UUID, Event> getEvents() {
        return events;
    }

    /**
     * Gets rooms associated with this convention
     *
     * @return Map from Room UUID to Room object
     */
    public Map<UUID, Room> getRooms() {
        return rooms;
    }

    /**
     * Gets the convention name
     *
     * @return convention name
     */
    public String getConferenceName() {
        return conferenceName;
    }

    /**
     * Gets the time range of the convention
     *
     * @return convention time range
     */
    public TimeRange getTimeRange() {
        return timeRange;
    }

    /**
     * Sets the time range for the convention
     *
     * @param timeRange new time range
     */
    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    /**
     * Sets the new name for the convention
     *
     * @param conferenceName new convention name
     */
    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    /**
     * Gets the UUID for this convention
     *
     * @return UUID object
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Tests if a UUID belongs to a user with organizer permissions
     *
     * @param organizerUUID UUID to test
     * @return true iff the UUID belongs to an organizer
     */
    public boolean isOrganizer(UUID organizerUUID) {
        return organizerUUIDs.contains(organizerUUID);
    }

    /**
     * Gets a set of all organizer UUIDs
     *
     * @return set of UUIDs
     */
    public Set<UUID> getOrganizerUUIDs() {
        return organizerUUIDs;
    }

    /**
     * Grant a user organizer permissions
     *
     * @param organizerUUID UUID of the user to promote
     */
    public void addOrganizer(UUID organizerUUID) {
        organizerUUIDs.add(organizerUUID);
    }

    /**
     * Revokes a user organizer permissions
     *
     * @param organizerUUID UUID of the user to demote
     */
    public boolean removeOrganizer(UUID organizerUUID) {
        return organizerUUIDs.remove(organizerUUID);
    }

    /**
     * Tests if a UUID belongs to a user with speaker permissions
     *
     * @param speakerUUID UUID to test
     * @return true iff the UUID belongs to an speaker
     */
    public boolean isSpeaker(UUID speakerUUID) {
        return speakerUUIDs.contains(speakerUUID);
    }

    /**
     * Gets a set of all speaker UUIDs
     *
     * @return set of UUIDs
     */
    public Set<UUID> getSpeakerUUIDs() {
        return speakerUUIDs;
    }

    /**
     * Sets speaker UUIDs
     */
    public void setSpeakerUUIDs(Set<UUID> speakerUUIDs) {
        this.speakerUUIDs = speakerUUIDs;
    }

    /**
     * Grant a user organizer permissions
     *
     * @param speakerUUID UUID of the user to promote
     */
    public void addSpeaker(UUID speakerUUID) {
        speakerUUIDs.add(speakerUUID);
    }

    /**
     * Revokes a user organizer permissions
     *
     * @param speakerUUID UUID of the user to demote
     */
    public boolean removeSpeaker(UUID speakerUUID) {
        return speakerUUIDs.remove(speakerUUID);
    }

    /**
     * Gets a set of all attendee UUIDs
     *
     * @return set of UUIDs
     */
    public boolean isAttendee(UUID attendeeUUID) {
        return attendeeUUIDs.contains(attendeeUUID);
    }

    /**
     * Gets a set of all attendee UUIDs
     *
     * @return set of UUIDs
     */
    public Set<UUID> getAttendeeUUIDs() {
        return new HashSet<>(attendeeUUIDs);
    }

    /**
     * Grant a user organizer permissions
     *
     * @param attendeeUUID UUID of the user to promote
     */
    public void addAttendee(UUID attendeeUUID) {
        attendeeUUIDs.add(attendeeUUID);
    }

    /**
     * Revokes a user organizer permissions
     *
     * @param attendeeUUID UUID of the user to demote
     */
    public boolean removeAttendee(UUID attendeeUUID) {
        return attendeeUUIDs.remove(attendeeUUID);
    }
}
