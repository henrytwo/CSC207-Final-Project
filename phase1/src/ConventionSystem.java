import console.UISystem;
import contact.ContactController;
import contact.ContactManager;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.conference.ConferenceManager;
import messaging.ConversationController;
import messaging.ConversationManager;
import user.UserController;
import user.UserManager;

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

        // Create managers
        // These store the entities and other important stuff
        UserManager userManager = new UserManager();
        ContactManager contactManager = new ContactManager();
        ConversationManager conversationManager = new ConversationManager();
        ConferenceManager conferenceManager = new ConferenceManager();

        // User controller
        UserController userController = new UserController(userManager);

        // Messaging controllers
        ContactController contactController = new ContactController(contactManager);
        ConversationController conversationController = new ConversationController(contactManager, conversationManager);

        // Convention controllers
        RoomController roomController = new RoomController(conferenceManager);
        EventController eventController = new EventController(conferenceManager);
        ConferenceController conferenceController = new ConferenceController(conversationManager, eventController, conferenceManager);

        /**
         * TODO: Remove test code here
         */
        userController.registerUser("Test", "Testerson", "test", "password");
        userController.logout();

        // If we were to change this to a GUI, here is what we would switch out
        UISystem uiSystem = new UISystem(userController, contactController, conversationController, roomController, eventController, conferenceController);
        uiSystem.run();
    }
}
