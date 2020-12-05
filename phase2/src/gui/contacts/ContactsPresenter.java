package gui.contacts;

import contact.ContactController;
import gui.user.picker.UserPickerDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.*;

public class ContactsPresenter {
    private IContactsView contactsView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private ContactController contactController;
    private UserController userController;

    private Map<String, Object> initializationArguments;

    private UUID signedInUserUUID;
    private UUID currentContactUUID;

    private int currentContactIndex;

    private List<UUID> contactsList;

    public ContactsPresenter(IFrame mainFrame, IContactsView contactsView, UUID defaultContactUUID) {
        this.mainFrame = mainFrame;
        this.contactsView = contactsView;

        this.currentContactUUID = defaultContactUUID;
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        this.contactController = controllerBundle.getContactController();
        this.userController = controllerBundle.getUserController();
        signedInUserUUID = userController.getCurrentUser();
        updateContactsList();

        this.panelFactory = mainFrame.getPanelFactory();
        this.dialogFactory = mainFrame.getDialogFactory();

        // Select default contact
        if (contactsList.size() > 0) {
            updateContactNames();

            int defaultContactIndex = 0;

            if (contactsList.contains(defaultContactUUID)) {
                defaultContactIndex = contactsList.indexOf(defaultContactUUID);
            }

            contactsView.setContactsListSelection(defaultContactIndex);
        }
    }

    private void updateContactNames() {
        String[] contactNames = new String[contactsList.size()];

//        for (int i = 0; i < contactsList.size(); i++) {
//            contactNames[i] = contactController.;
//        }

        contactsView.setContactsList(contactNames);
    }

    private void updateContactsList() {
        contactsList = new ArrayList<>(contactController.showContacts(signedInUserUUID));
    }


    public void sendRequest() {
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, contactController.showContacts(signedInUserUUID), "Select User:");
        UUID potentialContactUUID = userPickerDialog.run();
        contactController.sendRequest(signedInUserUUID, potentialContactUUID);
    }

    public void deleteContact() {
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, contactController.showContacts(signedInUserUUID), "Select User:");
        UUID pastContactUUID = userPickerDialog.run();
        contactController.deleteContacts(signedInUserUUID, pastContactUUID);
    }

    public void respondToRequests() {
        Set<UUID> requestsList = contactController.showRequests(signedInUserUUID);

    }

}