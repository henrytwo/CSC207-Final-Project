import contact.ContactController;
import contact.ContactManager;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.conference.ConferenceManager;
import gateway.Serializer;
import messaging.ConversationController;
import messaging.ConversationManager;
import user.UserController;
import user.UserManager;
import gui.GUISystem;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main convention system. This where the fun begins.
 */
public class ConventionSystem {
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Runs the Convention System
     */
    public void run() {
        // Setup logger
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.OFF);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        // Create serializers
        Serializer<UserManager> userManagerSerializer = new Serializer<>("userManager.ser");
        Serializer<ContactManager> contactManagerSerializer = new Serializer<>("contactManager.ser");
        Serializer<ConversationManager> conversationManagerSerializer = new Serializer<>("conversationManager.ser");
        Serializer<ConferenceManager> conferenceManagerSerializer = new Serializer<>("conferenceManager.ser");

        // Create managers
        // These store the entities and other important stuff
        System.out.println("Reading from disk...");
        UserManager userManager = userManagerSerializer.load(new UserManager());
        ContactManager contactManager = contactManagerSerializer.load(new ContactManager());
        ConversationManager conversationManager = conversationManagerSerializer.load(new ConversationManager());
        ConferenceManager conferenceManager = conferenceManagerSerializer.load(new ConferenceManager());

        // User controller
        UserController userController = new UserController(userManager);

        // Messaging controllers
        ContactController contactController = new ContactController(contactManager);
        ConversationController conversationController = new ConversationController(contactManager, conversationManager);

        // Convention controllers
        RoomController roomController = new RoomController(conferenceManager);
        EventController eventController = new EventController(conferenceManager, conversationManager);
        ConferenceController conferenceController = new ConferenceController(conversationManager, eventController, conferenceManager, userManager);

        GUISystem uiSystem = new GUISystem(userController, contactController, conversationController, roomController, eventController, conferenceController);
        uiSystem.run();

        // Serialize everything for the next run
        System.out.println("Writing to disk...");
        userManagerSerializer.save(userManager);
        contactManagerSerializer.save(contactManager);
        conversationManagerSerializer.save(conversationManager);
        conferenceManagerSerializer.save(conferenceManager);
    }
}
