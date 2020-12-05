package gui.contacts;

import contact.ContactController;
import gui.user.picker.UserPickerDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;

import java.util.*;

public class ContactsPresenter {
    private IContactsView contactsView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private ContactController contanctController;
    private UserController userController;

    //private Map<String, Object> initializationArguments;

    private UUID signedInUserUUID;
    private UUID currentContactUUID;
    private int currentContactIndex;

    private List<UUID> contactsList;

    public ContactsPresenter(IFrame mainFrame, IContactsView contactsView,UUID defaultContactUUID) {
        this.mainFrame = mainFrame;
        this.contactsView = contactsView;

        this.currentContactUUID = defaultContactUUID;

        signedInUserUUID = userController.getCurrentUser();
        updateContactsList();

        this.panelFactory = mainFrame.getPanelFactory();
        this.dialogFactory = mainFrame.getDialogFactory();
    }

    private void updateContactsList() {
        contactsList = new ArrayList<>(contanctController.showContacts(signedInUserUUID));
    }

    public void sendRequest() {
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, contanctController.showContacts(signedInUserUUID), "Select User:");
        UUID potentialContactUUID = userPickerDialog.run();
        contanctController.sendRequest(signedInUserUUID, potentialContactUUID);
    }

    public void deleteContacts() {
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, contanctController.showContacts(signedInUserUUID), "Select User:");
        UUID pastContactUUID = userPickerDialog.run();
        contanctController.deleteContacts(signedInUserUUID, pastContactUUID);
    }

    public void respondToRequests() {
        Set<UUID> requestsList = contanctController.showRequests(signedInUserUUID);

    }
}