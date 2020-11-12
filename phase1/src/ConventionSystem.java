import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConventionSystem {
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void run() {
        // Setup logger
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.OFF);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        ConferenceManager conferenceManager = new ConferenceManager();

        //ConversationController conversationController = new ConversationController();
        RoomController roomController = new RoomController(conferenceManager);
        EventController eventController = new EventController(conferenceManager);
        ConferenceController conferenceController = new ConferenceController(/*conversationController,*/ eventController, conferenceManager);

        // Test stuff

        /**
         * Big thinking
         *
         * - Dealing with CompareBySpeaker
         * - Dealing with speaker conflicts
         */

        LocalDateTime dateA = LocalDateTime.of(2015,
                Month.JULY, 29, 19, 30, 40);
        LocalDateTime dateB = LocalDateTime.of(2018,
                Month.JULY, 29, 19, 30, 40);

        UUID me = UUID.randomUUID();
        UUID attendee1 = UUID.randomUUID();
        UUID attendee2 = UUID.randomUUID();
        UUID otherOrganizer = UUID.randomUUID();
        UUID speaker1 = UUID.randomUUID();

        UUID conference1 = conferenceController.createConference("My convention", new TimeRange(dateA, dateB), me);

        for (UUID confUUID : conferenceController.getConferences()) {
            System.out.println(conferenceController.getConferenceName(confUUID));
        }

        conferenceController.addOrganizer(conference1, me, otherOrganizer);

        conferenceController.addAttendee(conference1, attendee1);
        conferenceController.addAttendee(conference1, attendee2);

        System.out.println(conferenceController.getSpeakers(conference1, me));
        UUID newRoom = roomController.createRoom(conference1, me, "ur room", 2);

        UUID newEvent = eventController.createEvent(conference1, me, "Blah", new TimeRange(dateA, dateB), newRoom, new HashSet<>() {{
            add(attendee1);
        }});

        System.out.println("Events " + eventController.getEvents(conference1, attendee1));
        System.out.println("Rooms " + roomController.getRooms(conference1, attendee1));

        System.out.println("Event speakers " + eventController.getEventSpeakers(conference1, me, newEvent));

        conferenceController.leaveConference(conference1, attendee1, attendee1);

        System.out.println("Event speakers " + eventController.getEventSpeakers(conference1, me, newEvent));

        System.out.println(conferenceController.getAttendees(conference1, otherOrganizer));
        eventController.createEventConversation(conference1, me, me);
        //System.out.println(conferenceController.getEvents(conference1, attendee1));

        conferenceController.leaveConference(conference1, otherOrganizer, me);
        conferenceController.leaveConference(conference1, otherOrganizer, otherOrganizer);

        System.out.println(conferenceController.getAttendees(conference1, otherOrganizer));
    }
}
