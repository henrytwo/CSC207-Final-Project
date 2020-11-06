package conference;

import conference.event.Event;
import conference.room.Room;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ConferenceManager {
    private Map<UUID, Conference> conferences = new HashMap<>();

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
        Conference newConference = new Conference(conferenceName, startTime, endTime, organizerUUID);

        conferences.put(newConference.getUuid(), newConference);

        return newConference.getUuid();
    }

    /**
     * Deletes a conference given its UUID.
     *
     * Returns true if operation was successful.
     *
     * @param conferenceUUID
     * @return
     */
    public boolean deleteConference(UUID conferenceUUID) {
        return conferences.remove(conferenceUUID) != null;
    }

    /**
     * Gets a map of all the conferences in the system.
     *
     * @return
     */
    public Map<UUID, Conference> getConferences() {
        return conferences;
    }

    public Conference getConference(UUID conferenceUUID) {
        return conferences.get(conferenceUUID);
    }

    public Set<Event> getEventsFromConference(UUID conferenceUUID) {
        return conferences.get(conferenceUUID).getEvents();
    }

    public Set<Room> getRoomsFromConference(UUID conferenceUUID) {
        return conferences.get(conferenceUUID).getRooms();
    }
}
