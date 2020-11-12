
package convention.event;


import convention.calendar.TimeRange;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Event{
    private UUID uuid;
    private Set<UUID> speakerUUIDs = new HashSet<>();
    private Set<UUID> attendeeUUIDs = new HashSet<>();

    private TimeRange timeRange;

    private UUID conversationUUID;
    private UUID roomUUID;

    public Event(UUID speakerUUID, TimeRange timeRange, UUID conversationUUID, UUID roomUUID){
        this.uuid = UUID.randomUUID();
        this.speakerUUIDs.add(speakerUUID);
        this.timeRange = timeRange;
        this.conversationUUID = conversationUUID;
        this.roomUUID = roomUUID;
    }

    public UUID getUUID() { return uuid;}

    public Set<UUID> getSpeakers() { return this.speakerUUIDs; }
    public void addSpeaker(UUID speakerUUID) { speakerUUIDs.add(speakerUUID); }
    public void removeSpeaker(UUID speakerUUID) { speakerUUIDs.remove(speakerUUID); }
    public boolean isSpeaker(UUID speakerUUID) { return speakerUUIDs.contains(speakerUUID); }

    public Set<UUID> getAttendeeUUIDs() { return attendeeUUIDs; }
    public void addAttendee(UUID attendeeUUID) { attendeeUUIDs.add(attendeeUUID); }
    public void removeAttendee(UUID attendeeUUID) { attendeeUUIDs.remove(attendeeUUID); }
    public boolean isAttendee(UUID attendeeUUID) { return attendeeUUIDs.contains(attendeeUUID); }

    public TimeRange getTimeRange() { return timeRange; }
    public void setTimeRange(TimeRange timeRange) {this.timeRange = timeRange; }

    public UUID getRoomUUID() { return roomUUID;}
    public void setRoomUUID(UUID roomUUID) {this.roomUUID = roomUUID; }

    public UUID getConversationUUID() { return conversationUUID; }
    public void setConversationUUID(UUID conversationUUID) { this.conversationUUID = conversationUUID; }
}