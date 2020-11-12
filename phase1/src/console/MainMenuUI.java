package console;

import console.conferences.ConferencesUI;
import console.contacts.ContactsUI;
import console.conversation.MessagingUI;
import contact.ContactController;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import messaging.ConversationController;
import user.UserController;

public class MainMenuUI {

    ConsoleUtilities consoleUtilities = new ConsoleUtilities();

    MessagingUI messagingUI;
    ContactsUI contactsUI;
    ConferencesUI conferencesUI;

    // User controller
    UserController userController;

    public MainMenuUI(UserController userController, ContactController contactController, ConversationController conversationController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;

        this.messagingUI = new MessagingUI(conversationController);
        this.contactsUI = new ContactsUI(contactController);
        this.conferencesUI = new ConferencesUI(userController, roomController, eventController, conferenceController);
    }

    /**
     * Run the MainMenuUI
     *
     * @return false iff the user wants to quit the program in the following UI loop
     */
    public boolean run() {

        String[] options = new String[]{
                "Messaging",
                "Contacts",
                "Conferences",
                "Log Out",
                "Exit System"
        };

        String userFullName = userController.getUserFullName(userController.getCurrentUser());

        while (true) {
            int selection = consoleUtilities.singleSelectMenu(String.format("Signed in as %s", userFullName), options);

            switch (selection) {
                case 1:
                    messagingUI.run();
                    break;
                case 2:
                    contactsUI.run();
                    break;
                case 3:
                    conferencesUI.run();
                    break;
                case 4:
                    userController.logout();
                    return true; // Logout (i.e. return to parent menu without terminating program)
                case 5:
                    return false; // Terminate program
            }
        }
    }
}
