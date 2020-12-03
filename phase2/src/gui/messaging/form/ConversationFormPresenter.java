package gui.messaging.form;


import contact.ContactController;
import gui.util.DateParser;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import messaging.ConversationController;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConversationFormPresenter {
    ConversationController conversationController;
    ContactController contactController;

    private boolean isExistingConversation;
    private UUID conversationUUID;
    private UUID userUUID;

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    private IConversationFormDialog conversationFormDialog;

    private String conversationName = "";
    private Set<UUID> availableUserUUIDs;
    private String messagecontent;
    private String peopleListString;

    private DateParser dateParser = new DateParser();

    ConversationFormPresenter(IFrame mainFrame, IConversationFormDialog conversationFormDialog) {
        this.conversationFormDialog = conversationFormDialog;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conversationController = controllerBundle.getConversationController();
        contactController = controllerBundle.getContactController();

        dialogFactory = mainFrame.getDialogFactory();

        conversationFormDialog.setDialogTitle("Create New Conversation");

        this.userUUID = controllerBundle.getUserController().getCurrentUser();

        availableUserUUIDs = contactController.showContacts(userUUID);
    }

    void submit() {
        conversationName = conversationFormDialog.getChatName();
        messagecontent = conversationFormDialog.getMessage();
        //peopleListString = conversationFormDialog.getPeopleList();
        String[] temparraystring = peopleListString.split(",");
        Set<UUID> tempSet = new HashSet<>();
        for (String friendsUuid : temparraystring) {
            tempSet.add(UUID.fromString(friendsUuid));
        }
        availableUserUUIDs = tempSet;
        conversationUUID = conversationController.initiateConversation(conversationName, userUUID, availableUserUUIDs, messagecontent);

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

        Set<UUID> selectedUserUUIDs = (Set<UUID>) chooseUsersDialog.run();

        if (selectedUserUUIDs != null) {
            selectedUserUUIDs.add(userUUID); // We need to add the signed in user in the conversation too



        }
    }
}