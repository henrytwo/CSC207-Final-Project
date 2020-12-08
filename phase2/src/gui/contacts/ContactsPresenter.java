package gui.contacts;

import contact.exception.RequestDeniedException;
import gui.user.picker.UserPickerDialog;
import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.*;

class ContactsPresenter extends AbstractPresenter {
    private IContactsView contactsView;

    private List<UUID> contactsList;
    private List<UUID> requestsList;

    /**
     * Constructor for contacts presenter.
     *
     * @param mainFrame    main GUI frame
     * @param contactsView view object for contacts UI
     */
    ContactsPresenter(IFrame mainFrame, IContactsView contactsView) {
        super(mainFrame);

        this.contactsView = contactsView;

        updateContactsList();
        updateRequestsList();
        updateContactNames();
        updateRequestsNames();
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
        currentContactIndex = 0;
        contactsList = new ArrayList<>(contactController.showContacts(signedInUserUUID));
    }

    /**
     * Updates requestsList attribute.
     */
    private void updateRequestsList() {
        currentRequestIndex = 0;
        requestsList = new ArrayList<>(contactController.showRequests(signedInUserUUID));
    }

    /**
     * Reloads the contacts panel.
     */
    private void reloadContactsPage() {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>() {
            {
                put("defaultTabIndex", 2);
            }
        }));
    }

    /**
     * Gets the UUID of the user selected in the requests list
     */
    private UUID getSelectedRequestUUID() {
        int selectedRequestIndex = contactsView.getRequestListIndex();

        if (selectedRequestIndex == -1 || requestsList.size() <= selectedRequestIndex) {
            return null;
        } else {
            return requestsList.get(selectedRequestIndex);
        }
    }

    /**
     * Gets the UUID of the user selected in the contacts list
     */
    private UUID getSelectedContactUUID() {
        int selectedContactIndex = contactsView.getContactListIndex();

        if (selectedContactIndex == -1 || contactsList.size() <= selectedContactIndex) {
            return null;
        } else {
            return contactsList.get(selectedContactIndex);
        }
    }

    /**
     * Sends a request to the user that is selected from the pop up dialog.
     */
    void sendRequest() {
        Set<UUID> potentialContacts = userController.getUsers();

        potentialContacts.removeAll(contactController.showContacts(signedInUserUUID));
        potentialContacts.removeAll(contactController.showSentRequests(signedInUserUUID));
        potentialContacts.remove(signedInUserUUID);

        UserPickerDialog userPickerDialog = new UserPickerDialog(mainFrame, potentialContacts, "Select User:");
        UUID potentialContactUUID = userPickerDialog.run();

        if (potentialContactUUID != null) {
            try {
                contactController.sendRequest(signedInUserUUID, potentialContactUUID);

                IDialog requestConfirmationDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                        put("title", "Confirmation");
                        put("message", String.format("Request has been sent to [%s].", userController.getUserFullName(potentialContactUUID)));
                    }
                });

                requestConfirmationDialog.run();
                reloadContactsPage();

            } catch (RequestDeniedException e) {
                IDialog requestDeniedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "Cannot send a request to this contact.");
                        put("title", "Request Denied");
                        put("messageType", DialogFactoryOptions.dialogType.WARNING);
                    }
                });
                requestDeniedDialog.run();
            }
        }
    }


    /**
     * Removes the selected contact from the users contact list and confirm with a dialog.
     * If no contact is selected, warning dialog pops up prompting user to select a contact.
     */
    void deleteContact() {
        UUID selectedContactUUID = getSelectedContactUUID();

        if (selectedContactUUID != null) {
            IDialog confirmDeletionDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to delete (%s) ?", userController.getUserFullName(selectedContactUUID)));
                    put("title", "Confirm Delete Contact");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmDeletionDialog.run()) {
                contactController.deleteContacts(signedInUserUUID, selectedContactUUID);
                reloadContactsPage();
            }
        } else {
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
    void acceptRequest() {
        UUID selectedUserUUID = getSelectedRequestUUID();

        if (selectedUserUUID != null) {
            IDialog confirmAcceptDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to connect with (%s) ?", userController.getUserFullName(selectedUserUUID)));
                    put("title", "Confirm Accept Request");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmAcceptDialog.run()) {
                contactController.acceptRequest(signedInUserUUID, selectedUserUUID);
                reloadContactsPage();
            }
        } else {
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
    void rejectRequest() {
        UUID selectedUserUUID = getSelectedRequestUUID();

        if (selectedUserUUID != null) {
            IDialog confirmRejectDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you don't want to connect with (%s) ?", userController.getUserFullName(selectedUserUUID)));
                    put("title", "Confirm Reject Request");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmRejectDialog.run()) {
                contactController.rejectRequest(signedInUserUUID, selectedUserUUID);
                reloadContactsPage();
            }
        } else {
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