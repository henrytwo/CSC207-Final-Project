import conference.ConferenceController;
import conference.calendar.TimeRange;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceSystem {
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void run() {
        // Setup logger
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.OFF);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        ConferenceController conferenceController = new ConferenceController();

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

        UUID conference1 = conferenceController.createConference("My conference", new TimeRange(dateA, dateB), me);

        for (UUID confUUID : conferenceController.getConferences()) {
            System.out.println(conferenceController.getConferenceName(confUUID));
        }

        conferenceController.addOrganizer(conference1, me, otherOrganizer);

        conferenceController.addAttendee(conference1, attendee1);
        conferenceController.addAttendee(conference1, attendee2);

        System.out.println(conferenceController.getEvents(conference1, attendee1));

        System.out.println(conferenceController.getAttendees(conference1, otherOrganizer));
        conferenceController.createEventConversation(conference1, me, me);
        //System.out.println(conferenceController.getEvents(conference1, attendee1));


        System.out.println(conferenceController.getAttendees(conference1, otherOrganizer));
    }
}
