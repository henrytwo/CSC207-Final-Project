package gui.messaging.form;


import contact.ContactController;
import gui.user.multipicker.IMultiUserPickerDialog;
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
    private Set<UUID> peopleSet;
    private String messagecontent;
    private String peopleListString;

    private DateParser dateParser = new DateParser();

    ConversationFormPresenter(IFrame mainFrame, IConversationFormDialog conversationFormDialog, UUID conversationUUID) {
        this.conversationFormDialog = conversationFormDialog;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conversationController = controllerBundle.getConversationController();

        dialogFactory = mainFrame.getDialogFactory();

        this.conversationUUID = conversationUUID;
        this.userUUID = controllerBundle.getUserController().getCurrentUser();

        // Existing conferences will have a non-null UUID
        isExistingConversation = conversationUUID != null;

        conversationFormDialog.setDialogTitle(String.format("Create New Conversation (%s)", conversationUUID));

        conversationName = conversationController.getConversationName(conversationUUID);
        peopleSet = contactController.showContacts(userUUID);

        createPopUp(peopleSet);



        conversationFormDialog.setChatName(conversationName);
        conversationFormDialog.setPeopleList(peopleSet.toString());

    }

    void submit() {
        conversationName = conversationFormDialog.getChatName();
        messagecontent = conversationFormDialog.getMessage();
        peopleListString = conversationFormDialog.getPeopleList();
        String[] temparraystring = peopleListString.split(",");
        Set<UUID> tempSet = new HashSet<>();
        for (String friendsUuid: temparraystring){
            tempSet.add(UUID.fromString(friendsUuid));
        }
        peopleSet = tempSet;
        conversationUUID = conversationController.initiateConversation(conversationName, userUUID, peopleSet, messagecontent);

        // Update conference UUID in case it has changed
        conversationFormDialog.setConversationUUID(conversationUUID);
        conversationFormDialog.setUpdated(true);

        // Close the dialog so it isn't blocking anymore
        conversationFormDialog.close();
    }

    void createPopUp(Set<UUID> peopleSet) {
        IDialog chooseUsersDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MULTI_USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select users to add to the new conversation");
                put("availableUserUUIDs", peopleSet);
            }
        });

        Set<UUID> selectedUserUUIDs = (Set<UUID>) chooseUsersDialog.run();

        if (selectedUserUUIDs != null) {
            selectedUserUUIDs.add(userUUID); // We need to add the signed in user in the conversation too

            UUID conversationUUID = conversationController.initiateConversation("Create new Conversation", userUUID, selectedUserUUIDs, "");

            IDialog conversationCreatedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", String.format("Conversation with %d users created successfully (%s)", selectedUserUUIDs.size(), conversationUUID));
                    put("title", "Conversation created");
                    put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                }
            });

            conversationCreatedDialog.run();

        }
    }
}