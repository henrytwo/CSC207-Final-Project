package console.contacts;

import console.ConsoleUtilities;
import contact.ContactController;
import user.UserController;

public class ContactsUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    ContactController contactController;
    UserController userController;

    public ContactsUI(UserController userController, ContactController contactController) {
        this.contactController = contactController;
        this.userController = userController;
    }

    public void run() {

        consoleUtilities.confirmBoxClear("This should be the contacts menu");
    }
}
