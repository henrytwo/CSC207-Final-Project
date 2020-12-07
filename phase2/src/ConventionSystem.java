import contact.ContactController;
import contact.ContactManager;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.ScheduleController;
import convention.conference.ConferenceManager;
import gateway.CSVReader;
import gateway.Serializer;
import gui.MainFrame;
import messaging.ConversationController;
import messaging.ConversationManager;
import user.UserController;
import user.UserManager;
import util.ControllerBundle;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main convention system. This where the fun begins.
 */
public class ConventionSystem {
    private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

        // Create god mode accounts
        try {
            Set<UUID> newGodUUIDs = userManager.loadGodUsers(new CSVReader("godUsers.csv").read());

            if (newGodUUIDs.size() > 0) {
                System.out.printf("Added %d new god users: %s\n", newGodUUIDs.size(), newGodUUIDs.toString());
            } else {
                System.out.println("No new god users added.");
            }
        } catch (IOException e) {
            System.out.println("Unable to load god mode users" + e);
        }

        // Create the document writer
        // TODO: IDocumentPrinter documentPrinter = ...

        // User controller
        UserController userController = new UserController(userManager);

        // Messaging controllers
        ContactController contactController = new ContactController(contactManager);
        ConversationController conversationController = new ConversationController(contactManager, conversationManager, userManager);

        // Convention controllers
        RoomController roomController = new RoomController(conferenceManager, userManager);
        EventController eventController = new EventController(conferenceManager, conversationManager, userManager);
        // TODO: pass the document printer as a param
        ScheduleController scheduleController = new ScheduleController(userManager, conferenceManager, eventController);
        ConferenceController conferenceController = new ConferenceController(conversationManager, eventController, conferenceManager, userManager);

        // Packages up all the controllers in a nice bundle to make it easy to pass around UI components
        // without super long parameter lists
        ControllerBundle controllerBundle = new ControllerBundle(userController, contactController, conversationController, roomController, eventController, conferenceController);

        Runnable shutdown = () -> {
            // Serialize everything for the next run
            System.out.println("Writing to disk...");
            userManagerSerializer.save(userManager);
            contactManagerSerializer.save(contactManager);
            conversationManagerSerializer.save(conversationManager);
            conferenceManagerSerializer.save(conferenceManager);
        };

        MainFrame uiSystem = new MainFrame(controllerBundle, shutdown);
        uiSystem.run();
    }
}
