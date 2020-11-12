package console.contacts;

import console.ConsoleUtilities;
import contact.ContactController;

public class ContactsUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    ContactController contactController;

    public ContactsUI(ContactController contactController) {
        this.contactController = contactController;
    }

    public void run() {

        consoleUtilities.confirmBoxClear("This should be the contacts menu");
    }
}
