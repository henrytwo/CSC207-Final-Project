import console.LoginAndRegisterUI;
import console.MainMenuUI;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
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
        //RoomController roomController = new RoomController(conferenceManager);
        //EventController eventController = new EventController(conferenceManager);
        //ConferenceController conferenceController = new ConferenceController(/*conversationController,*/ eventController, conferenceManager);

        /*
         * User flow
         *
         * ** Make attendee operations executable by admins too
         * ** So it'd be check if organizer OR (if target == self AND is attendee)
         *
         * /
         * |-Login or Register                                           ** Ez clap menu thing (Shubhra)
         *   |- Go to messaging
         *   |  |-Look at the list of messages                           ** Ez clap menu thing
         *   |  | |-Select a conversation                                ** Custom message boi (Mahak)
         *   |  |   |-Send message
         *   |  |   |-Read message
         *   |  |   |-See users in conversation
         *   |  |-Start a new message with someone on your contact list
         *   |
         *   |- Go to contacts                                            ** Ez clap menu thing (Pranjal)
         *   |  |-View contacts                                           ** Custom stuff
         *   |  |-Request someone to connect
         *   |  |-View people who want to slide into your DMs
         *   |
         *   |- Go to conferences                                         ** Ez clap menu thing
         *  |-> Create a conference                                   ** Form boi
         *  |-> Join a conference                                     ** Ez clap menu thing
         *  |   |-> Find a conference from a list and join it
         *  |-> View a joined conference                              ** Ez clap menu thing (Ellie/Henry/Shubhra)
         *      |-> View the general conference details (start, end, name, etc.)
         *      |
         *      |-> View the event calendar
         *      |
         *      |-> Event stuff (Emre)
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
         *      |-> Room stuff (Antara)
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
         * UI Components to build
         * - n-column table with numbered rows (Talk to shubhra about it)
         * - messaging thing
         * - Form component
         */

        /**
         * Big thinking
         *
         * - Dealing with CompareBySpeaker
         * - Dealing with speaker conflicts
         */

        /*LoginAndRegisterUI loginAndRegisterUI = new LoginAndRegisterUI(userController);
        MainMenuUI mainMenuUI = new MainMenuUI();

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
        while (true) {
            if (userController.getCurrentUser() == null) {
                loginAndRegisterUI.run();
            } else {
                mainMenuUI.run();
            }
        }*/
    }
}
