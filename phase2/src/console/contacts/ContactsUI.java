package console.contacts;

import console.ConsoleUtilities;
import contact.ContactController;
import user.UserController;

import java.util.UUID;

/**
 * UI components related to contacts page
 */
public class ContactsUI {
    ConsoleUtilities consoleUtilities;
    ContactController contactController;
    UserController userController;
    UUID signedInUserUUID;

    /**
     * ContactsUI constructor
     *
     * @param userController
     * @param contactController
     */
    public ContactsUI(UserController userController, ContactController contactController) {
        this.contactController = contactController;
        this.userController = userController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    /**
     * Run contacts UI
     */
    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.signedInUserUUID = userController.getCurrentUser();


        consoleUtilities.confirmBoxClear("This should be the contacts menu");
    }
}
