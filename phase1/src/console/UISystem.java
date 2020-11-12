package console;

import console.loginAndRegister.LoginAndRegisterUI;
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

    /**
     * Runs the main UI loop
     *
     * If the user is not logged in, present login/register prompts. Otherwise, send them to the main menu.
     */
    public void run() {
        LoginAndRegisterUI loginAndRegisterUI = new LoginAndRegisterUI(userController);
        MainMenuUI mainMenuUI = new MainMenuUI(userController, contactController, conversationController, roomController, eventController, conferenceController);

        boolean running = true;

        while (running) {
            if (userController.getCurrentUser() == null) {
                running = loginAndRegisterUI.run();
            } else {
                running = mainMenuUI.run();
            }
        }
    }
}
