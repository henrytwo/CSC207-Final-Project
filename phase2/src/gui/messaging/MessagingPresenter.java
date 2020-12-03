package gui.messaging;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.*;
import messaging.ConversationController;
import user.UserController;
import util.ControllerBundle;

import java.util.*;

class MessagingPresenter {
    private IMessagingView messagingView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private ConversationController conversationController;
    private List<UUID> conversationUUIDs;

    private UUID signedInUserUUID;
    private UserController userController;

    private int currentConversationIndex = -1;
    private UUID currentConversationUUID;
    private Map<String, Object> initializationArguments;

    MessagingPresenter(IFrame mainFrame, IMessagingView messagingView, UUID defaultConversationUUID, Map<String, Object> initializationArguments) {
        this.messagingView = messagingView;
        this.mainFrame = mainFrame;
        this.initializationArguments = initializationArguments;

        panelFactory = mainFrame.getPanelFactory();
        dialogFactory = mainFrame.getDialogFactory();

//         Init controllers
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
            if (defaultConversationUUID != null && conversationUUIDs.contains(defaultConversationIndex)) {
                defaultConversationIndex = conversationUUIDs.indexOf(defaultConversationIndex);
            }

            // Set initial conference selection
            messagingView.setConversationListSelection(defaultConversationIndex); // makes it look like we select it
            updateSelection(defaultConversationIndex);
        }
    }

    void sendMessage(){
        String currentMessage = messagingView.getMessagefromtextbox();
        conversationController.sendMessage(signedInUserUUID, currentMessage, currentConversationUUID);
        updateMessage();

    }

    void createConversation() {
        IDialog conversationFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONVERSATION_FORM);

        UUID newConversationUUID = (UUID) conversationFormDialog.run();

        if (newConversationUUID != null) {
            updateAndSelectNewConversation(newConversationUUID);
        }
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
        conversationUUIDs = new ArrayList<>(conversationController.getConversationlist(signedInUserUUID));
    }

    void updateSelection(int selectedIndex){
        if (selectedIndex != currentConversationIndex){
        currentConversationIndex = selectedIndex;
        currentConversationUUID = conversationUUIDs.get(selectedIndex);
        updateMessage();
        }

    }

    private void updateMessage(){
        List<Map<String, String>> messagesListMap = conversationController.getMessages(signedInUserUUID, currentConversationUUID);
        List<String> messagesStringList = new ArrayList();
        for(Map<String, String> messageMap: messagesListMap){
            UUID senderId = UUID.fromString(messageMap.get("sender"));
            String timestamp = messageMap.get("timestamp");
            String content = messageMap.get("content");
            String messageString = String.format("[%s @ %s] %s\n", senderId, timestamp, content);
            messagesStringList.add(messageString);
        }
        String[] messageArray = (String[]) messagesStringList.toArray();
        messagingView.setMessages(messageArray);
    }
}