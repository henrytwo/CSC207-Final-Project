package console;

import contact.ContactController;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import messaging.ConversationController;
import user.UserController;

public class UISystem {

    // User controller
    UserController userController;

    // Messaging controllers
    ContactController contactController;
    ConversationController conversationController;

    // Convention controllers
    RoomController roomController;
    EventController eventController;
    ConferenceController conferenceController;

    public UISystem(UserController userController, ContactController contactController, ConversationController conversationController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;
        this.contactController = contactController;
        this.conversationController = conversationController;
        this.roomController = roomController;
        this.eventController = eventController;
        this.conferenceController = conferenceController;
    }

    public void run() {
        LoginAndRegisterUI loginAndRegisterUI = new LoginAndRegisterUI(userController);
        MainMenuUI mainMenuUI = new MainMenuUI(userController, contactController, conversationController, roomController, eventController, conferenceController);

        boolean exit = false;

        while (!exit) {
            if (userController.getCurrentUser() == null) {
                exit = loginAndRegisterUI.run();
            } else {
                exit = mainMenuUI.run();
            }
        }
    }
}
