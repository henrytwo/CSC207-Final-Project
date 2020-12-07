package gui.messaging.menu;

import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.*;

class MessagingPresenter extends AbstractPresenter {
    private IMessagingView messagingView;

    private List<UUID> conversationUUIDs;

    private int currentConversationIndex = -1;
    private UUID currentConversationUUID;

    MessagingPresenter(IFrame mainFrame, IMessagingView messagingView, UUID defaultConversationUUID) {
        super(mainFrame);

        this.messagingView = messagingView;

        updateConversationList();

        // Make initial selection
        if (conversationUUIDs.size() > 0) {
            updateConversationNames();

            int defaultConversationIndex = 0;

            // Choose the specified default conference UUID
            if (defaultConversationUUID != null) {
                if (conversationUUIDs.contains(defaultConversationUUID)) {
                    defaultConversationIndex = conversationUUIDs.indexOf(defaultConversationUUID);
                } else {
                    IDialog unableToOpenMessageDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                        {
                            put("title", "Access Denied");
                            put("message", String.format("You don't have permission to read this conversation. (%s)", defaultConversationUUID));
                            put("messageType", DialogFactoryOptions.dialogType.ERROR);
                        }
                    });
                    unableToOpenMessageDialog.run();
                }
            }

            // Set initial conference selection
            messagingView.setConversationListSelection(defaultConversationIndex); // makes it look like we select it
            updateSelection(defaultConversationIndex);
        } else {
            if (messagingView.getNumMessages() == 0) {
                String[] firstMessage = new String[]{"Create a New Conversation to View or Send Messages"};
                messagingView.setMessages(firstMessage);
            }

            messagingView.setEnableSendButton(false);
            messagingView.setEnableTextField(false);
            String[] messageInUsersList = new String[]{"Create New Conversation to Add Users"};
            messagingView.setUsersList(messageInUsersList);
        }
    }

    void archiveConversation() {
        System.out.println("asdasd");

        IDialog testDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", "are u sure u wanna do this?");
                put("title", "this is title");
                put("messageType", DialogFactoryOptions.dialogType.ERROR);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if ((boolean) testDialog.run()) {
            System.out.println("you said yes");
        }
    }

    void unreadConversation() {

    }

    void sendMessage() {
        String currentMessage = messagingView.getTextBoxContent();
        if (!currentMessage.equals("")) {
            conversationController.sendMessage(signedInUserUUID, currentMessage, currentConversationUUID);
            updateMessage();
            messagingView.clearTextBox();
            messagingView.scrollToLastMessage();
        }

    }

    void createConversation() {
        IDialog conversationFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONVERSATION_FORM);

        UUID newConversationUUID = (UUID) conversationFormDialog.run();

        if (newConversationUUID != null) {
            updateAndSelectNewConversation(newConversationUUID);
            messagingView.setEnableSendButton(true);
            messagingView.setEnableTextField(true);

            updateUserList(newConversationUUID);
        }
    }

    private void updateUserList(UUID conversationUUID) {
        Set<UUID> usersUUIDList = conversationController.getUsersInConversation(conversationUUID);
        String[] userNames = new String[usersUUIDList.size()];
        int i = 0;
        for (UUID userUUID : usersUUIDList) {
            userNames[i] = userController.getUserFullName(userUUID);
            i++;
        }
        messagingView.setUsersList(userNames);
    }


    private void updateAndSelectNewConversation(UUID selectedConversationUUID) {
        // Update the local list with the new conference
        updateConversationList();
        updateConversationNames();

        // Select the latest conference
        int index = conversationUUIDs.indexOf(selectedConversationUUID);

        messagingView.setConversationListSelection(index);
    }


    private void updateConversationNames() {
        String[] conversationNames = new String[conversationUUIDs.size()];

        for (int i = 0; i < conversationUUIDs.size(); i++) {
            conversationNames[i] = conversationController.getConversationName(conversationUUIDs.get(i));
        }

        messagingView.setConversationList(conversationNames);
    }

    private void updateConversationList() {
        currentConversationIndex = -1;
        conversationUUIDs = new ArrayList<>(conversationController.getConversationList(signedInUserUUID));
    }

    void updateSelection(int selectedIndex) {
        if (selectedIndex != currentConversationIndex) {
            currentConversationIndex = selectedIndex;
            currentConversationUUID = conversationUUIDs.get(selectedIndex);
            updateMessage();
            messagingView.scrollToLastMessage();

            updateUserList(currentConversationUUID);
        }

    }

    private void updateMessage() {
        List<Map<String, String>> messagesListMap = conversationController.getMessages(signedInUserUUID, currentConversationUUID);

        String[] messageArray = new String[messagesListMap.size()];
        int index = 0;

        for (Map<String, String> messageMap : messagesListMap) {
            UUID senderId = UUID.fromString(messageMap.get("sender"));
            String senderName = userController.getUserFullName(senderId);
            String timestamp = messageMap.get("timestamp");
            String content = messageMap.get("content");
            String messageString = String.format("[%s @ %s] %s\n", senderName, timestamp, content);

            messageArray[index] = messageString;
            index++;
        }

        messagingView.setMessages(messageArray);
    }
}