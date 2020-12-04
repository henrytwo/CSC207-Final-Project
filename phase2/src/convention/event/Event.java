package convention.event;

import convention.calendar.TimeRange;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Event object. Stores details about the room, attendees, speakers, etc.
 */
public class Event implements Serializable {
    private UUID uuid;
    private String title;
    private Set<UUID> speakerUUIDs;
    private Set<UUID> attendeeUUIDs = new HashSet<>();

    private TimeRange timeRange;

    private UUID conversationUUID;
    private UUID roomUUID;

    /**
     * Event constructor.
     *
     * @param title        name of new events
     * @param timeRange    TimeRange of the events
     * @param roomUUID     UUID of the room in which events is held
     * @param speakerUUIDs set of speakers for this events
     */
    public Event(String title, TimeRange timeRange, UUID roomUUID, Set<UUID> speakerUUIDs) {
        this.title = title;
        this.uuid = UUID.randomUUID();
        this.speakerUUIDs = speakerUUIDs;
        this.timeRange = timeRange;
        this.roomUUID = roomUUID;
    }

    /**
     * Gets the UUID of this events
     *
     * @return UUID of the events being referred to
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the title of this events
     *
     * @return String title of the events being referred to
     */
    public String getTitle() {
        return title;
    }

    /**
     * changes the title of this events
     *
     * @param title new title of events
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the set of all speakers for this events
     *
     * @return a set of UUIDs of all speakers for the events
     */
    public Set<UUID> getSpeakers() {
        return this.speakerUUIDs;
    }

    /**
     * Adds a speaker to the set of speakers
     *
     * @param speakerUUID the UUID of the speaker being added
     */
    public void addSpeaker(UUID speakerUUID) {
        speakerUUIDs.add(speakerUUID);
    }

    /**
     * removes a speaker from the set of speakers
     *
     * @param speakerUUID the UUID of the speaker being removed
     */
    public void removeSpeaker(UUID speakerUUID) {
        speakerUUIDs.remove(speakerUUID);
    }

    /**
     * Checks if a user is a speaker for this events
     *
     * @param speakerUUID UUID of user in question
     * @return True if the user is a speaker in this events, False otherwise
     */
    public boolean isSpeaker(UUID speakerUUID) {
        return speakerUUIDs.contains(speakerUUID);
    }

    /**
     * Gets the set of  all users registered for this events
     *
     * @return the set of UUIDs of users registered for this events
     */
    public Set<UUID> getAttendeeUUIDs() {
        return attendeeUUIDs;
    }

    /**
     * Registers a user for this events
     *
     * @param attendeeUUID user being registered
     */
    public void addAttendee(UUID attendeeUUID) {
        attendeeUUIDs.add(attendeeUUID);
    }

    /**
     * removes a user from this events
     *
     * @param attendeeUUID user being removed
     */
    public void removeAttendee(UUID attendeeUUID) {
        attendeeUUIDs.remove(attendeeUUID);
    }

    /**
     * Checks if a user is registered for this events
     *
     * @param attendeeUUID UUID of the user in question
     * @return True if the user is registered for this events, False otherwise
     */
    public boolean isAttendee(UUID attendeeUUID) {
        return attendeeUUIDs.contains(attendeeUUID);
    }

    /**
     * Gets the TimeRange of this events
     *
     * @return TimeRange of this events
     */
    public TimeRange getTimeRange() {
        return timeRange;
    }

    /**
     * Changes the time of this events
     *
     * @param timeRange the new TimeRange of this events
     */
    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    /**
     * gets the the room holding this events
     *
     * @return the UUID of the room holding this events
     */
    public UUID getRoomUUID() {
        return roomUUID;
    }

    /**
     * Changes the room the events is being held in
     *
     * @param roomUUID the UUID of the new room
     */
    public void setRoomUUID(UUID roomUUID) {
        this.roomUUID = roomUUID;
    }

    /**
     * Gets the UUID for the conversation for this events
     *
     * @return the UUID of the conversation for this events
     */
    public UUID getConversationUUID() {
        return conversationUUID;
    }

    /**
     * Change the chat for this events
     *
     * @param conversationUUID the UUID of the new conversation
     */
    public void setConversationUUID(UUID conversationUUID) {
        this.conversationUUID = conversationUUID;
    }
}