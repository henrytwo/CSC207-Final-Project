package gui.contacts;

import contact.ContactController;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import gui.user.picker.UserPickerDialog;

import java.util.*;

public class ContactsPresenter {
    private IContactsView contactsView;
    private IFrame mainFrame;
    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;
    private ContactController contanctController;
    private UserController userController;
    private Map<String, Object> initializationArguments;
    private UUID signedInUserUUID;
    private List<UUID> contactsList;

    public ContactsPresenter(IFrame mainFrame, IContactsView contactsView, Map<String, Object> initializationArguments) {
        this.mainFrame = mainFrame;
        this.contactsView = contactsView;
        this.initializationArguments = initializationArguments;

        signedInUserUUID = userController.getCurrentUser();
        updateContactsList();

        panelFactory = mainFrame.getPanelFactory();
        dialogFactory = mainFrame.getDialogFactory();
    }

    private void updateContactsList() {
        contactsList = new ArrayList<>(contanctController.showContacts(signedInUserUUID));
    }

    public  void sendRequest(){
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, contanctController.showContacts(signedInUserUUID), "Select User:");
        UUID potentialContactUUID = userPickerDialog.run();
        contanctController.sendRequest(signedInUserUUID, potentialContactUUID);
    }

    public void deleteContacts(){
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, contanctController.showContacts(signedInUserUUID), "Select User:");
        UUID pastContactUUID = userPickerDialog.run();
        contanctController.deleteContacts(signedInUserUUID, pastContactUUID);
    }

    public void respondToRequests(){
        Set<UUID> requestsList = contanctController.showRequests(signedInUserUUID);

    }
}