package gui.messaging.form;

import contact.ContactController;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import messaging.ConversationController;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class ConversationFormPresenter {
    private ConversationController conversationController;
    private ContactController contactController;

    private UUID userUUID;

    private IDialogFactory dialogFactory;

    private IConversationFormDialog conversationFormDialog;

    private Set<UUID> availableUserUUIDs;
    private Set<UUID> selectedUserUUIDs = new HashSet<>();


    ConversationFormPresenter(IFrame mainFrame, IConversationFormDialog conversationFormDialog) {
        this.conversationFormDialog = conversationFormDialog;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conversationController = controllerBundle.getConversationController();
        contactController = controllerBundle.getContactController();

        dialogFactory = mainFrame.getDialogFactory();

        conversationFormDialog.setDialogTitle("Create New Conversation");

        this.userUUID = controllerBundle.getUserController().getCurrentUser();
        this.availableUserUUIDs = contactController.showContacts(userUUID);
    }

    void submit() {
        String conversationName = conversationFormDialog.getChatName();
       String  messagecontent = conversationFormDialog.getMessage();

        UUID conversationUUID = conversationController.initiateConversation(conversationName, userUUID, selectedUserUUIDs, messagecontent);

        // Update conference UUID in case it has changed
        conversationFormDialog.setConversationUUID(conversationUUID);
        conversationFormDialog.setUpdated(true);

        // Close the dialog so it isn't blocking anymore
        conversationFormDialog.close();
    }

    void selectUsers() {
        IDialog chooseUsersDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MULTI_USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select users to add to the new conversation");
                put("availableUserUUIDs", availableUserUUIDs);
            }
        });

        Set<UUID> newSelectedUserUUIDs = (Set<UUID>) chooseUsersDialog.run();

        if (newSelectedUserUUIDs != null) {
            newSelectedUserUUIDs.add(userUUID); // We need to add the signed in user in the conversation too
            selectedUserUUIDs = newSelectedUserUUIDs;
        }
    }
}