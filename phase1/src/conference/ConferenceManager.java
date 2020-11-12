package conference;

import conference.calendar.CalendarManager;
import conference.calendar.TimeRange;
import conference.event.Event;
import conference.event.EventManager;
import conference.room.Room;
import conference.room.RoomManager;
import util.exception.*;

import java.util.*;

public class ConferenceManager {
    private Map<UUID, Conference> conferences = new HashMap<>();

    /**
     * Conference names must be non-empty; this method tests for that condition
     *
     * @param name name to test
     * @return true iff the conference name is valid
     */
    private boolean validateConferenceName(String name) {
        return name.length() > 0;
    }

    /**
     * Gets a map from Event UUID to their respective TimeRange
     *
     * @param conferenceUUID UUID of the conference to fetch from
     * @return a map of event UUIDs to their corresponding TimeRange
     */
    public Map<UUID, TimeRange> getConferenceSchedule(UUID conferenceUUID) {
        RoomManager roomManager = getRoomManager(conferenceUUID);
        Map<UUID, TimeRange> eventUUIDtoTimeRanges = new HashMap<>();

        for (UUID roomUUID : roomManager.getRooms()) {
            CalendarManager calendarManager = roomManager.getCalendarManager(roomUUID);

            eventUUIDtoTimeRanges.putAll(calendarManager.getUUIDtoTimeRanges());
        }

        return eventUUIDtoTimeRanges;
    }

    /**
     * Generates an EventManager object to control events for a conference
     *
     * @param conferenceUUID UUID of the conference to fetch from
     * @return the event manager object
     */
    public EventManager getEventManager(UUID conferenceUUID) {
        Map<UUID, Event> events = getConference(conferenceUUID).getEvents();

        return new EventManager(events);
    }

    public RoomManager getRoomManager(UUID conferenceUUID) {
        Map<UUID, Room> rooms = getConference(conferenceUUID).getRooms();

        return new RoomManager(rooms);
    }

    /**
     * Creates a conference and assigns the authenticated user as an organizer.
     *
     * @param conferenceName the desired conference name (Must be non-empty)
     * @param timeRange      time range of the conference
     * @param organizerUUID  UUID of the initial organier user
     * @return UUID of the new conference
     */
    public UUID createConference(String conferenceName, TimeRange timeRange, UUID organizerUUID) {
        if (!validateConferenceName(conferenceName)) {
            throw new InvalidNameException();
        }

        Conference newConference = new Conference(conferenceName, timeRange, organizerUUID);
        conferences.put(newConference.getUUID(), newConference);

        return newConference.getUUID();
    }

    /**
     * Tests if a conference exists in the system.
     * <p>
     * Returns if a conference exists the system.
     *
     * @param conferenceUUID UUID of the conference
     * @return true iff a conference with the corresponding UUID exists in the system
     */
    public boolean conferenceExists(UUID conferenceUUID) {
        return conferences.containsKey(conferenceUUID);
    }

    /**
     * Deletes a conference given its UUID.
     * <p>
     * Throws NullConferenceException if the conferenceID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     */
    public void deleteConference(UUID conferenceUUID) {
        if (!conferenceExists(conferenceUUID)) {
            throw new NullConferenceException(conferenceUUID);
        }

        conferences.remove(conferenceUUID);
    }

    /**
     * Gets a conference from its UUID. This method also checks if the UUID corresponds to a valid conference and raises
     * a NullConferenceException if not.
     *
     * @param conferenceUUID UUID of the conference
     * @return Conference object
     */
    private Conference getConference(UUID conferenceUUID) {
        if (!conferenceExists(conferenceUUID)) {
            throw new NullConferenceException(conferenceUUID);
        }

        return conferences.get(conferenceUUID);
    }

    /**
     * Gets a set of all the conference UUIDs in the system.
     *
     * @return Set of conference UUIDs
     */
    public Set<UUID> getConferences() {
        return conferences.keySet();
    }

    /**
     * Gets conference name
     *
     * @param conferenceUUID UUID of the conference
     * @return name of the conference
     */
    public String getConferenceName(UUID conferenceUUID) {
        return getConference(conferenceUUID).getConferenceName();
    }

    /**
     * Gets conference time range
     *
     * @param conferenceUUID UUID of the conference
     * @return time range of the conference
     */
    public TimeRange getTimeRange(UUID conferenceUUID) {
        return getConference(conferenceUUID).getTimeRange();
    }

    /**
     * Sets conference dates datetime
     *
     * @param conferenceUUID UUID of the conference
     * @param timeRange      time range to assign the conference
     */
    public void setTimeRange(UUID conferenceUUID, TimeRange timeRange) {
        getConference(conferenceUUID).setTimeRange(timeRange);
    }

    /**
     * Sets conference name
     *
     * @param conferenceUUID UUID of the conference
     * @param newName        name to assign the conference (Must be non-empty)
     */
    public void setConferenceName(UUID conferenceUUID, String newName) {
        if (!validateConferenceName(newName)) {
            throw new InvalidNameException();
        }

        getConference(conferenceUUID).setConferenceName(newName);
    }

    /**
     * Tests if a UUID belongs to an attendee user for this conference
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to test
     * @return true iff the userUUID belongs to an attendee
     */
    public boolean isAttendee(UUID conferenceUUID, UUID userUUID) {
        return getConference(conferenceUUID).isAttendee(userUUID);
    }

    /**
     * Tests if a UUID belongs to an organizer user for this conference
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to test
     * @return true iff the userUUID belongs to an organizer
     */
    public boolean isOrganizer(UUID conferenceUUID, UUID userUUID) {
        return getConference(conferenceUUID).isOrganizer(userUUID);
    }

    /**
     * Tests if a UUID belongs to a speaker user for this conference
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to test
     * @return true iff the userUUID belongs to a speaker
     */
    public boolean isSpeaker(UUID conferenceUUID, UUID userUUID) {
        return getConference(conferenceUUID).isSpeaker(userUUID);
    }

    /**
     * Gets a set of organizer UUIDs for a particular conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     * @return set of UUIDs of organizer users
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID) {
        return getConference(conferenceUUID).getOrganizerUUIDs();
    }

    /**
     * Adds an organizer to a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to add
     */
    public void addOrganizer(UUID conferenceUUID, UUID userUUID) {
        getConference(conferenceUUID).addOrganizer(userUUID);
    }

    /**
     * Removes an organizer from a conference. There must always be at least one organizer left.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     * Throws NullUserException if the userUUID does not correspond to a valid organizer.
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to remove
     */
    public void removeOrganizer(UUID conferenceUUID, UUID userUUID) {
        Conference conference = getConference(conferenceUUID);

        if (!conference.getOrganizerUUIDs().contains(userUUID)) {
            throw new NullUserException(userUUID);
        } else if (conference.getOrganizerUUIDs().size() == 1) {
            throw new LoneOrganizerException();
        } else {
            conference.removeOrganizer(userUUID);
        }
    }

    /**
     * Gets a set of attendee UUIDs for a particular conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     * @return set of UUIDs of attendee users
     */
    public Set<UUID> getAttendees(UUID conferenceUUID) {
        return getConference(conferenceUUID).getAttendeeUUIDs();
    }

    /**
     * Adds an attendee to a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to add
     */
    public void addAttendee(UUID conferenceUUID, UUID userUUID) {
        getConference(conferenceUUID).addAttendee(userUUID);
    }

    /**
     * Removes an attendee from a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     * Throws NullUserException if the userUUID does not correspond to a valid user.
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to remove
     */
    public void removeAttendee(UUID conferenceUUID, UUID userUUID) {
        Conference conference = getConference(conferenceUUID);

        if (!conference.getAttendeeUUIDs().contains(userUUID)) {
            throw new NullUserException(userUUID);
        } else {
            conference.removeAttendee(userUUID);
        }
    }

    /**
     * Gets a set of speaker UUIDs for a particular conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     * @return set of UUIDs speaker users
     */
    public Set<UUID> getSpeakers(UUID conferenceUUID) {
        return getConference(conferenceUUID).getSpeakerUUIDs();
    }

    /**
     * Adds an speaker to a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to add
     */
    public void addSpeaker(UUID conferenceUUID, UUID userUUID) {
        getConference(conferenceUUID).addSpeaker(userUUID);
    }

    /**
     * Removes an speaker from a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     * Throws NullUserException if the userUUID does not correspond to a valid user.
     *
     * @param conferenceUUID UUID of the conference
     * @param userUUID       UUID of the user to remove
     */
    public void removeSpeaker(UUID conferenceUUID, UUID userUUID) {
        Conference conference = getConference(conferenceUUID);

        if (!conference.getSpeakerUUIDs().contains(userUUID)) {
            throw new NullUserException(userUUID);
        } else {
            conference.removeSpeaker(userUUID);
        }
    }
}
