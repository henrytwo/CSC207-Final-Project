package gui.contacts;

import contact.ContactController;
import contact.exception.GhostAcceptDeniedException;
import gui.user.picker.UserPickerDialog;
import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import user.exception.NullUserException;

import java.util.*;

class ContactsPresenter extends AbstractPresenter {
    private IContactsView contactsView;

    private UUID currentContactUUID;
    private UUID currentRequestUUID;

    private int currentContactIndex = -1;
    private int currentRequestIndex = -1;

    private List<UUID> contactsList;
    private List<UUID> requestsList;

    /**
     * Constructor for contacts presenter.
     *
     * @param mainFrame          main GUI frame
     * @param contactsView       view object for contacts UI
     * @param defaultContactUUID UUID of the default contact that is selected when we open the contacts page
     * @param defaultRequestUUID UUID of the default request that is selected when we open the contacts page
     */
    ContactsPresenter(IFrame mainFrame, IContactsView contactsView, UUID defaultContactUUID, UUID defaultRequestUUID) {
        super(mainFrame);

        this.contactsView = contactsView;

        this.currentContactUUID = defaultContactUUID;
        this.currentRequestUUID = defaultRequestUUID;

        updateContactsList();
        updateRequestsList();

//        if (requestsList.size() > 0) {
//            updateRequestsNames();
//
//            int defaultRequestIndex = 0;
//
//            if (defaultRequestUUID != null && requestsList.contains(defaultRequestUUID)) {
//                defaultRequestIndex = requestsList.indexOf(defaultRequestUUID);
//            }
//
//            contactsView.setRequestsListSelection(defaultRequestIndex);
//            requestSelectionUpdate(defaultRequestIndex);
//        }
//
//        updateContactsList();
//
//        // Select default contact
//        if (contactsList.size() > 0) {
//            updateContactNames();
//
//            int defaultContactIndex = 0;
//
//            if (contactsList.contains(defaultContactUUID)) {
//                defaultContactIndex = contactsList.indexOf(defaultContactUUID);
//            }
//
//            contactsView.setContactsListSelection(defaultContactIndex);
//            contactSelectionUpdate(defaultContactIndex);
//        }
    }

    /**
     * Updates the contacts list that is visible to the user
     */
    private void updateContactNames() {
        String[] contactNames = new String[contactsList.size()];

        for (int i = 0; i < contactsList.size(); i++) {
            contactNames[i] = userController.getUserFullName(contactsList.get(i));
        }

        contactsView.setContactsList(contactNames);
    }

    /**
     * Updates the requests list that is visible to the user.
     */
    private void updateRequestsNames() {
        String[] requestNames = new String[requestsList.size()];
        for (int i = 0; i < requestsList.size(); i++) {
            requestNames[i] = userController.getUserFullName(requestsList.get(i));
        }

        contactsView.setRequestsList(requestNames);
    }

    /**
     * Updates contactsList attribute.
     */
    private void updateContactsList() {
        contactsList = new ArrayList<>(contactController.showContacts(signedInUserUUID));
    }

    /**
     * Updates requestsList attribute.
     */
    private void updateRequestsList() {
        requestsList = new ArrayList<>(contactController.showRequests(signedInUserUUID));
    }

//    /**
//     * Updates the index of the current selected request with the new selection.
//     *
//     * @param selectedIndex index of the new selection(request)
//     */
//    public void requestSelectionUpdate(int selectedIndex) {
//        if (selectedIndex != currentRequestIndex && currentRequestIndex > -1) {
//            this.currentRequestIndex = selectedIndex;
//            this.currentRequestUUID = requestsList.get(currentRequestIndex);
//        }
//    }
//
//
//    public void contactSelectionUpdate(int selectedIndex) {
//        if(selectedIndex != currentContactIndex){
//            currentContactIndex = selectedIndex;
//            currentContactUUID = contactsList.get(selectedIndex);
//        }
//    }

    /**
     * Validates the selected request in requests list. If no request is selected, produces an error dialog.
     */
    public boolean validateRequestContact(int requestIndex) {
        int selectedRequestIndex = contactsView.getRequestListIndex();
        if (selectedRequestIndex == -1) {
            IDialog noContactSelectedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Please select a request to accept/reject.");
                    put("title", "No Contact Selected");
                    put("messageType", DialogFactoryOptions.dialogType.WARNING);
                }
            });
            noContactSelectedDialog.run();

        }
        return true;
        }

    /**
     * Validates the selected contact in contacts list. If no contact is selected, produces an error dialog.
     */
    public boolean validateContact(int contactIndex) {
        int selectedContactIndex = contactsView.getContactListIndex();
        if (selectedContactIndex == -1) {
            IDialog noContactSelectedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Please select a contact to remove.");
                    put("title", "No Contact Selected");
                    put("messageType", DialogFactoryOptions.dialogType.WARNING);
                }
            });
            noContactSelectedDialog.run();
        }
        return true;
    }



    /**
     * Sends a request to the user that is selected from the pop up dialog.
     */
    public void sendRequest() {
        Set<UUID> potentialContacts = userController.getUsers();
        potentialContacts.removeAll(contactController.showContacts(signedInUserUUID));
        potentialContacts.removeAll(contactController.showSentRequests(signedInUserUUID));
        potentialContacts.remove(signedInUserUUID);
        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, potentialContacts, "Select User:");
        UUID potentialContactUUID = userPickerDialog.run();
        contactController.sendRequest(signedInUserUUID, potentialContactUUID);
        if (potentialContactUUID != null) {
            IDialog requestConfirmationDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                    put("title", "Confirmation");
                    put("message", String.format("Request has been sent to [%s].", userController.getUserFullName(potentialContactUUID)));
                }
            });

            requestConfirmationDialog.run();
        }

    }

    public void deleteContact() {
        int potentialDeleteContact = contactsView.getContactListIndex();
        try {
            if (validateContact(potentialDeleteContact)) {
                IDialog confirmDeletionDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                    {
                        put("message", String.format("Are you sure you want to delete (%s) ?", userController.getUserFullName(currentContactUUID)));
                        put("title", "Confirm Delete Contact");
                        put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                        put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                    }
                });

                if ((boolean) confirmDeletionDialog.run()) {
                    contactController.deleteContacts(signedInUserUUID, currentContactUUID);
                    updateContactsList();
                    updateContactNames();
                }
            }
        } catch (NullUserException e) {
            IDialog noContactSelectedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Please select a contact to remove.");
                    put("title", "No Contact Selected");
                    put("messageType", DialogFactoryOptions.dialogType.WARNING);
                }
            });
            noContactSelectedDialog.run();
        }
    }

    /**
     * Accepts the currently selected request, first takes a confirmation from the user through a popup dialog.
     */
    public void acceptRequest() {
        int potentialContactIndex = contactsView.getRequestListIndex();
        try {
            if (validateRequestContact(potentialContactIndex)) {
                IDialog confirmAcceptDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                    {
                        put("message", String.format("Are you sure you want to connect with (%s) ?", userController.getUserFullName(currentRequestUUID)));
                        put("title", "Confirm Accept Request");
                        put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                        put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                    }
                });

                if ((boolean) confirmAcceptDialog.run()) {
                    contactController.acceptRequests(signedInUserUUID, currentRequestUUID);
                    updateContactsList();
                    updateContactNames();
                    updateRequestsList();
                    updateRequestsNames();
                    currentRequestUUID = null;
                }
            }
        } catch (NullUserException e) {
            IDialog noContactSelectedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Please select a request to accept.");
                    put("title", "No Contact Selected");
                    put("messageType", DialogFactoryOptions.dialogType.WARNING);
                }
            });
            noContactSelectedDialog.run();
        }
    }

    /**
     * Rejects currently selected request, takes confirmation from the user first through a pop up dialog.
     */
    public void rejectRequest() {
        int potentialRejectIndex = contactsView.getRequestListIndex();
        try {
            if (validateRequestContact(potentialRejectIndex)) {
                IDialog confirmRejectDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                    {
                        put("message", String.format("Are you sure you don't want to connect with (%s) ?", userController.getUserFullName(currentRequestUUID)));
                        put("title", "Confirm Reject Request");
                        put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                        put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                    }
                });

                if ((boolean) confirmRejectDialog.run()) {
                    contactController.rejectRequests(signedInUserUUID, currentRequestUUID);
                    updateRequestsList();
                    updateRequestsNames();
                }
            }
        } catch (NullUserException e) {
            IDialog noContactSelectedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Please select a request to reject.");
                    put("title", "No Contact Selected");
                    put("messageType", DialogFactoryOptions.dialogType.WARNING);
                }
            });
            noContactSelectedDialog.run();
        }
    }
}