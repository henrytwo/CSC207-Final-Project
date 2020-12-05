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
    private UUID currentRequestUUID;

    private int currentContactIndex;
    private int currentRequestIndex;

    private List<UUID> contactsList;
    private List<UUID> requestsList;

    public ContactsPresenter(IFrame mainFrame, IContactsView contactsView, UUID defaultContactUUID, UUID defaultRequestUUID) {
        this.mainFrame = mainFrame;
        this.contactsView = contactsView;

        this.currentContactUUID = defaultContactUUID;
        this.currentRequestUUID = defaultRequestUUID;
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        this.contactController = controllerBundle.getContactController();
        this.userController = controllerBundle.getUserController();
        signedInUserUUID = userController.getCurrentUser();
        updateContactsList();

        this.panelFactory = mainFrame.getPanelFactory();
        this.dialogFactory = mainFrame.getDialogFactory();

        updateRequestsList();

        if (requestsList.size() > 0) {
            updateRequestsNames();

            int defaultRequestIndex = 0;

            if (defaultRequestUUID != null && requestsList.contains(defaultRequestUUID)) {
                defaultRequestIndex = requestsList.indexOf(defaultRequestUUID);
            }

            contactsView.setRequestsListSelection(defaultRequestIndex);
        }

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

    private void updateRequestsNames(){
        String[] requestNames = new String[requestsList.size()];
        for(int i = 0; i < requestsList.size(); i++){
            requestNames[i] = userController.getUserFullName(requestsList.get(i));
        }

        contactsView.setRequestsList(requestNames);
    }

    private void updateContactsList() {
        contactsList = new ArrayList<>(contactController.showContacts(signedInUserUUID));
    }

    private void updateRequestsList(){
        currentRequestIndex = -1;
        requestsList = new ArrayList<>(contactController.showRequests(signedInUserUUID));
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

    public  void requestSelectionUpdate(int selectedIndex){
        if(selectedIndex != currentRequestIndex){
            currentRequestIndex = selectedIndex;
            currentRequestUUID = requestsList.get(selectedIndex);
        }
    }

    public void acceptRequest(){
        IDialog confirmAcceptDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you want to connect with (%s) ?", userController.getUserFullName(currentRequestUUID)));
                put("title", "Confirm Accept Request");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if((boolean) confirmAcceptDialog.run()){
            contactController.acceptRequests(signedInUserUUID, currentRequestUUID);
            updateContactsList();
            updateContactNames();
        }

    }

    public void rejectRequest(){
        IDialog confirmRejectDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you don't want to connect with (%s) ?", userController.getUserFullName(currentRequestUUID)));
                put("title", "Confirm Reject Request");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if((boolean) confirmRejectDialog.run()){
            contactController.rejectRequests(signedInUserUUID, currentRequestUUID);
            updateRequestsList();
            updateRequestsNames();
        }
    }
}