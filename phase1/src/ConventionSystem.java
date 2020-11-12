import contact.ContactController;
import contact.ContactManager;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.conference.ConferenceManager;
import messaging.ConversationController;
import messaging.ConversationManager;
import messaging.Message;

import java.util.HashSet;
import java.util.Set;
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

        ContactManager contactManager = new ContactManager();
        ConversationManager conversationManager = new ConversationManager();
        ContactController contactController = new ContactController(contactManager);
        ConversationController conversationController = new ConversationController(contactManager, conversationManager);

        RoomController roomController = new RoomController(conferenceManager);
        EventController eventController = new EventController(conferenceManager);
        ConferenceController conferenceController = new ConferenceController(conversationManager, eventController, conferenceManager);


        // Testing message sending
        /*
        UUID me = UUID.randomUUID();
        UUID otherUser = UUID.randomUUID();

        Set<UUID> myself = new HashSet<>() {
            {
                add(me);
            }
        };

        Set<UUID> bois = new HashSet<>() {
            {
                add(me);
                add(otherUser);
            }
        };

        UUID adminConvo = conversationManager.createConversation("blah", myself, bois, new Message(me, "you're here whether you like it or not"));

        // Create a chat with a single person
        UUID convo1 = conversationController.initiateConversation("My cool chat", me, new HashSet<>() {

        }, new Message(me, "Hi there"));

        //contactController.sendRequest(me, otherUser);

        // Send test messages
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "Cool message"), convo1);

        UUID convo2 = conversationController.initiateConversation("My cool chat", me, new HashSet<>() {

        }, new Message(me, "Nice convo bro"));

        conversationController.sendMessage(new Message(me, "Hi"), adminConvo);
        conversationController.sendMessage(new Message(me, "yes"), adminConvo);
        conversationController.sendMessage(new Message(me, "cool messages"), adminConvo);

        // Ok, now the user can take a look of the convos they're a part of
        Set<UUID> convoList = conversationController.getConversationlist(otherUser);

        for (UUID convoUUID : convoList) {
            System.out.println("\n\nConvo UUID:" + convoUUID + "\n" + conversationController.getMessages(me, convoUUID));
        }*/

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
