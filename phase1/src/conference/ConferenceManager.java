package conference;

import conference.event.Event;
import conference.room.Room;
import util.exception.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ConferenceManager {
    private Map<UUID, Conference> conferences = new HashMap<>();

    private boolean validateTimeRange(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    private boolean validateConferenceName(String name) {
        return name.length() > 0;
    }

    /**
     * Creates a conference and assigns the authenticated user as an organizer.
     *
     * @param conferenceName
     * @param startTime
     * @param endTime
     * @param organizerUUID
     * @return
     */
    public UUID createConference(String conferenceName, LocalDateTime startTime, LocalDateTime endTime, UUID organizerUUID) {
        if (!validateTimeRange(startTime, endTime)) {
            throw new InvalidTimeRangeException();
        }

        if (!validateConferenceName(conferenceName)) {
            throw new InvalidNameException();
        }

        Conference newConference = new Conference(conferenceName, startTime, endTime, organizerUUID);
        conferences.put(newConference.getUuid(), newConference);

        return newConference.getUuid();
    }

    /**
     * Tests if a conference exists in the system.
     *
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
     *
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
    public Conference getConference(UUID conferenceUUID) {
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
    public Set<UUID> getConferenceUUIDs() {
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
     * Gets conference start datetime
     *
     * @return
     */
    public LocalDateTime getStart(UUID conferenceUUID) {
        return getConference(conferenceUUID).getStart();
    }

    /**
     * Gets conference end datetime
     *
     * @return
     */
    public LocalDateTime getEnd(UUID conferenceUUID) {
        return getConference(conferenceUUID).getEnd();
    }

    /**
     * Sets conference dates datetime
     *
     * @return
     */
    public void setDates(UUID conferenceUUID, LocalDateTime newStart, LocalDateTime newEnd) {
        if (!validateTimeRange(newStart, newEnd)) {
            throw new InvalidTimeRangeException();
        }

        getConference(conferenceUUID).setStart(newStart);
        getConference(conferenceUUID).setEnd(newEnd);
    }

    /**
     * Sets conference name
     *
     * @return
     */
    public void setConferenceName(UUID conferenceUUID, String newName) {
        if (!validateConferenceName(newName)) {
            throw new InvalidNameException();
        }

        getConference(conferenceUUID).setConferenceName(newName);
    }

    /**
     * Gets a set of all the events for a particular conference given its UUID.
     *
     * @param conferenceUUID
     * @return
     */
    public Set<Event> getEventsFromConference(UUID conferenceUUID) {
        return getConference(conferenceUUID).getEvents();
    }

    /**
     * Gets a set of all the rooms for a particular conference given its UUID.
     *
     * @param conferenceUUID
     * @return
     */
    public Set<Room> getRoomsFromConference(UUID conferenceUUID) {
        return getConference(conferenceUUID).getRooms();
    }

    /**
     * Gets a set of organizer UUIDs for a particular conference.
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
