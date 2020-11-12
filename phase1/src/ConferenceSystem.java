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

        /*
        * User flow
        *
        * ** Make attendee operations executable by admins too
        * ** So it'd be check if organizer OR (if target == self AND is attendee)
        *
        * /
        * |-Login or Register
        *   |- Go to messaging
        *   |  |-Look at the list of messages
        *   |  | |-Select a conversation
        *   |  |   |-Send message
        *   |  |   |-Read message
        *   |  |   |-See users in conversation
        *   |  |-Start a new message with someone on your contact list
        *   |
        *   |- Go to contacts
        *   |  |-View contacts
        *   |  |-Request someone to connect
        *   |  |-View people who want to slide into your DMs
        *   |
        *   |- Go to conferences
            *  |-> Create a conference
            *  |-> Join a conference
            *  |   |-> Find a conference from a list and join it
            *  |-> View a joined conference
            *      |-> View the general conference details (start, end, name, etc.)
            *      |
            *      |-> View the event calendar
            *      |
            *      |-> Event stuff
            *      |   |-> View list of events (Attendee)
            *      |   |-> View list of events (Speaker)
            *      |   |-> View event room
            *      |   |-> View event time stuff
            *      |   |-> Register for event
            *      |   |-> Unregister from event
            *      |   |-> Make a conversation for everyone in this event (Speaker)
            *      |   |-> View a list of attendees, speakers
            *      |   |-> Organizer related operations
            *      |       |-> Edit the name, dates, etc.
            *      |       |-> Create event
            *      |       |-> Delete event
            *      |
            *      |-> Room stuff
            *      |   |-> View calendar
            *      |   |-> Organizer related operations
            *      |        |-> Edit the room capacity, location, etc.
            *      |        |-> Create room
            *      |        |-> Delete room
            *      |
            *      |-> Conference management stuff
            *          |-> Delete the conference
            *          |-> Slide into the DM of any attendee
            *          |-> Add/Remove organizer
        * */

        /**
         * *      |-> Get conference name
         *             *      |-> Get conference start
         *             *      |-> Get conference end
         *             *      |-> Set conference name
         *             *      |-> Set conference start
         *             *      |-> Set conference end
         *             *      |
         *             *      |-> View Conference schedule
         *             *      |
         *             *      |-> View Events UUIDs
         *             *      |-> Register for event (Admin/Self)
         *             *      |-> Unregister for event (Admin/Self)
         *             *      |-> Edit event (Admin)
         *             *      |    |-> Set name
         *             *      |    |-> Set start time/date
         *             *      |    |-> Set end time/date
         *             *      |    |-> Add speaker(s)
         *             *      |    |-> Remove speaker(s)
         *             *      |    |-> Set room
         *             *      |-> Create event (Admin)
         *             *      |-> Delete event (Admin)
         *             *      |-> Create event conversation (Speaker)
         *             *      |-> Get event name
         *             *      |-> Get event speakers
         *             *      |-> Get event time range
         *             *      |-> Get event room
         *             *      |
         *             *      |-> Create room (Admin)
         *             *      |-> Edit room (Admin)
         *             *      |    |-> Set room location
         *             *      |    |-> Set room capacity
         *             *      |-> Delete room (Admin)
         *             *      |-> Get rooms
         *             *      |-> Get room location
         *             *      |-> Get room capacity
         *             *      |
         *             *      |-> Get organizer UUIDs (Admin)
         *             *      |-> Add organizer (Admin)
         *             *      |-> Remove as organizer (Admin)
         *             *      |
         *             *      |-> Get speaker UUIDs (Admin)
         *             *      |-> Get attendee UUIDs (Admin)
         *             *      |
         *             *      |-> Message any registered users (Admin)
         *             *      |-> Remove from conference (Admin/Self)
         *
         */

        /**
         * Big thinking
         *
         * - Storing calendar
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
