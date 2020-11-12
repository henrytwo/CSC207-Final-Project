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

    private boolean validateConferenceName(String name) {
        return name.length() > 0;
    }

    /**
     * TODO: Maybe add a method to getEventManager, which takes in a conferenceUUID and returns an EventManager
     * which is initialized with the hashmap of events for that conference
     * <p>
     * Similar idea for room.
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
     * @param conferenceName
     * @param timeRange
     * @param organizerUUID
     * @return
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
     * @param conferenceUUID
     * @return
     */
    public boolean conferenceExists(UUID conferenceUUID) {
        return conferences.containsKey(conferenceUUID);
    }

    /**
     * Deletes a conference given its UUID.
     * <p>
     * Throws NullConferenceException if the conferenceID does not correspond to a valid conference.
     *
     * @param conferenceUUID
     * @return
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
     * @param conferenceUUID
     * @return
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
     * @return
     */
    public Set<UUID> getConferences() {
        return conferences.keySet();
    }

    /**
     * Gets conference name
     *
     * @return
     */
    public String getConferenceName(UUID conferenceUUID) {
        return getConference(conferenceUUID).getConferenceName();
    }

    /**
     * Gets conference time range
     *
     * @return
     */
    public TimeRange getTimeRange(UUID conferenceUUID) {
        return getConference(conferenceUUID).getTimeRange();
    }

    /**
     * Sets conference dates datetime
     */
    public void setTimeRange(UUID conferenceUUID, TimeRange timeRange) {
        getConference(conferenceUUID).setTimeRange(timeRange);
    }

    /**
     * Sets conference name
     */
    public void setConferenceName(UUID conferenceUUID, String newName) {
        if (!validateConferenceName(newName)) {
            throw new InvalidNameException();
        }

        getConference(conferenceUUID).setConferenceName(newName);
    }

    public boolean isAttendee(UUID conferenceUUID, UUID userUUID) {
        return getConference(conferenceUUID).isAttendee(userUUID);
    }

    public boolean isOrganizer(UUID conferenceUUID, UUID userUUID) {
        return getConference(conferenceUUID).isOrganizer(userUUID);
    }

    public boolean isSpeaker(UUID conferenceUUID, UUID userUUID) {
        return getConference(conferenceUUID).isSpeaker(userUUID);
    }

    /**
     * Gets a set of organizer UUIDs for a particular conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID
     * @return
     */
    public Set<UUID> getOrganizers(UUID conferenceUUID) {
        return getConference(conferenceUUID).getOrganizerUUIDs();
    }

    /**
     * Adds an organizer to a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
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
     * @param conferenceUUID
     * @param userUUID
     * @return
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
     * @param conferenceUUID
     * @return
     */
    public Set<UUID> getAttendees(UUID conferenceUUID) {
        return getConference(conferenceUUID).getAttendeeUUIDs();
    }

    /**
     * Adds an attendee to a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
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
     * @param conferenceUUID
     * @param userUUID
     * @return
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
     * @param conferenceUUID
     * @return
     */
    public Set<UUID> getSpeakers(UUID conferenceUUID) {
        return getConference(conferenceUUID).getSpeakerUUIDs();
    }

    /**
     * Adds an speaker to a conference.
     * <p>
     * Throws NullConferenceException if the conferenceUUID does not correspond to a valid conference.
     *
     * @param conferenceUUID
     * @param userUUID
     * @return
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
     * @param conferenceUUID
     * @param userUUID
     * @return
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
