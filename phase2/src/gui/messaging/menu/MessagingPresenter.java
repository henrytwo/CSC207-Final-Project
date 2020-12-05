package gui.messaging.menu;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import messaging.ConversationController;
import user.UserController;
import util.ControllerBundle;

import java.util.*;

class MessagingPresenter {
    private IMessagingView messagingView;

    private IDialogFactory dialogFactory;

    private ConversationController conversationController;
    private List<UUID> conversationUUIDs;

    private UUID signedInUserUUID;
    private UserController userController;

    private int currentConversationIndex = -1;
    private UUID currentConversationUUID;


    MessagingPresenter(IFrame mainFrame, IMessagingView messagingView, UUID defaultConversationUUID) {
        this.messagingView = messagingView;

        dialogFactory = mainFrame.getDialogFactory();

        // Init controllers
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conversationController = controllerBundle.getConversationController();
        userController = controllerBundle.getUserController();

        signedInUserUUID = userController.getCurrentUser();

        updateConversationList();

        // Make initial selection
        if (conversationUUIDs.size() > 0) {
            updateConversationNames();

            int defaultConversationIndex = 0;

            // Choose the specified default conference UUID
            if (defaultConversationUUID != null) {
                if( conversationUUIDs.contains(defaultConversationUUID)) {
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
            if (messagingView.getMessagesFromJList() == 0) {
                String[] firstMessage = new String[]{"Create a New Conversation to View or Send Messages"};
                messagingView.setMessages(firstMessage);
            }
            messagingView.setEnableSendButton(false);
            messagingView.setEnableTextField(false);
            String[] messageInUsersList = new String[]{"Create New Conversation to Add Users"};
            messagingView.setUsersList(messageInUsersList);
        }
    }

    void sendMessage() {
        String currentMessage = messagingView.getTextboxContent();
        if (!currentMessage.equals("")) {
            conversationController.sendMessage(signedInUserUUID, currentMessage, currentConversationUUID);
            updateMessage();
            messagingView.setTextFieldToNull();
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
        Set<UUID> usersUUIDList = conversationController.getUsersInConvo(conversationUUID);
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