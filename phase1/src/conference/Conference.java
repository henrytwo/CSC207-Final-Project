package conference;

import conference.event.Event;
import conference.room.Room;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Conference {
    Set organizerUIDs = new HashSet<String>();
    Set speakerUIDs = new HashSet<String>();

    Set events = new HashSet<Event>();
    Set rooms = new HashSet<Room>();

    String conferenceName;
    UUID id;

    LocalDateTime start;
    LocalDateTime end;

    public Conference(String conferenceName) {
        this.conferenceName = conferenceName;
        this.id = UUID.randomUUID();
    }
}
