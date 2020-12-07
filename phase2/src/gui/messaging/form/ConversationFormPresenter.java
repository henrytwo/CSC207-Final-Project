package gui.messaging.form;

import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class ConversationFormPresenter extends AbstractPresenter {
    private IConversationFormDialog conversationFormDialog;

    private Set<UUID> availableUserUUIDs;
    private Set<UUID> selectedUserUUIDs = new HashSet<>();

    ConversationFormPresenter(IFrame mainFrame, IConversationFormDialog conversationFormDialog) {
        super(mainFrame);

        this.conversationFormDialog = conversationFormDialog;

        conversationFormDialog.setDialogTitle("Create New Conversation");

        // God users can message anyone
        this.availableUserUUIDs = userController.getUserIsGod(signedInUserUUID)
                ? userController.getUsers()
                : contactController.showContacts(signedInUserUUID);
    }

    void submit() {
        String conversationName = conversationFormDialog.getChatName();
        String messageContent = conversationFormDialog.getMessage();
        if (conversationName.length() == 0) {
            IDialog emptyChatNameDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Conversation name must be non-empty");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });
            emptyChatNameDialog.run();
        } else if (messageContent.length() == 0) {
            IDialog emptyMessageDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Message must be non-empty");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });
            emptyMessageDialog.run();
        } else {
            UUID conversationUUID = conversationController.initiateConversation(conversationName, signedInUserUUID, selectedUserUUIDs, messageContent);

            // Update conference UUID in case it has changed
            conversationFormDialog.setConversationUUID(conversationUUID);
            conversationFormDialog.setUpdated(true);

            // Close the dialog so it isn't blocking anymore
            conversationFormDialog.close();
        }

    }

    void selectUsers() {
        IDialog chooseUsersDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MULTI_USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select users to add to the new conversation. You may only add users that are on your contacts list.");
                put("availableUserUUIDs", availableUserUUIDs);
                put("selectedUserUUIDs", selectedUserUUIDs);
            }
        });

        Set<UUID> newSelectedUserUUIDs = (Set<UUID>) chooseUsersDialog.run();

        if (newSelectedUserUUIDs != null) {
            newSelectedUserUUIDs.add(signedInUserUUID); // We need to add the signed in user in the conversation too
            selectedUserUUIDs = newSelectedUserUUIDs;
        }
    }
}