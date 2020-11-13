package console.contacts;

import console.ConsoleUtilities;
import contact.ContactController;
import user.UserController;

import java.util.UUID;

public class ContactsUI {
    ConsoleUtilities consoleUtilities;
    ContactController contactController;
    UserController userController;
    UUID signedInUserUUID;

    public ContactsUI(UserController userController, ContactController contactController) {
        this.contactController = contactController;
        this.userController = userController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.signedInUserUUID = userController.getCurrentUser();


        consoleUtilities.confirmBoxClear("This should be the contacts menu");
    }
}
