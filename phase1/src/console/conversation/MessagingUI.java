package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;
import messaging.exception.MessageDeniedException;
import user.UserController;

import java.util.*;
import java.util.function.Function;

public class MessagingUI {
    ConsoleUtilities consoleUtilities;
    ConversationController conversationController;
    UserController userController;
    UUID signedInUserUUID;

    public MessagingUI(UserController userController, ConversationController conversationController) {
        this.conversationController = conversationController;
        this.userController = userController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    public void createConversation() {
        Scanner stdin = new Scanner(System.in);
        try {
            consoleUtilities.clearConsole();

            Set<UUID> userall = userController.getUsers();
            userall.remove(signedInUserUUID);
            if (userall.isEmpty()) {
                consoleUtilities.confirmBoxClear("There are no other users available to create a conversation with.");
                return;
            }

            System.out.print("Enter conversation name: ");
            String convoName = stdin.nextLine();

            Set<UUID> others = consoleUtilities.userPicker("Select users you want to create conversation with:", userall);

            if (others != null) {

//            System.out.println("Enter UUID of user you want to create conversation with:");
//            UUID receiverID = UUID.fromString(stdin.nextLine());
                System.out.print("Enter message: ");
                String message = stdin.nextLine();
//            HashSet<UUID> others = new HashSet<UUID>();
//            others.add(receiverID);
                UUID convoUUID = conversationController.initiateConversation(convoName, signedInUserUUID, others, message);
                showMenuOfMessages(convoUUID);
            }
        } catch (MessageDeniedException e) {
            consoleUtilities.confirmBoxClear("Cannot send messages to users not on contact list.");
        }

    }

    /**
     * Prints a list of messages sent in this Chat(Conversation)
     *
     * @param conversationUUID The Id of the selected conversation for which the messages are to be shown
     */
    public void showMenuOfMessages(UUID conversationUUID) {
        // prints list of messages in a particular conversation and then takes in an input from the user if they want to
        // send a message otherwise returns back to previous menu

        Scanner enteredMessage = new Scanner(System.in);
        String conversationName = conversationController.getConversationName(conversationUUID);

        while (true) {
            consoleUtilities.clearConsole();
            System.out.println("Showing messages for Chat group: " + conversationName);
            ArrayList<Map<String, String>> arrayListMessagesMap = conversationController.getMessages(signedInUserUUID, conversationUUID);
            ArrayList<String> messageSet = new ArrayList<>();
            for (Map<String, String> messageMap : arrayListMessagesMap) {
                UUID senderUUID = UUID.fromString(messageMap.get("sender"));
                String sender_name = userController.getUserUsername(senderUUID);
                String messageInfo = "[" + sender_name + '@' + messageMap.get("timestamp") + "] " +
                        messageMap.get("content");
                messageSet.add(messageInfo);
            }
            for (String message : messageSet) {
                System.out.println(message);
                System.out.println("------------------------------------------------------------------------------");
            }
            System.out.println("\n\n");
            System.out.print("[Send Message - Empty message to quit]> ");
            String newMessageToSend = enteredMessage.nextLine();
            if (!newMessageToSend.equals("")) {
                conversationController.sendMessage(signedInUserUUID, newMessageToSend, conversationUUID);
            } else {
                break;
            }
        }
    }

    public void selectConversation() {
        Set<UUID> conversationList = conversationController.getConversationlist(signedInUserUUID);

        if (conversationList.size() == 0) {
            consoleUtilities.confirmBoxClear("You currently do not have any active conversations.");
        } else {
            UUID selectedConversationUUID = conversationPickerMenu("Choose an active conversation to view.", conversationList);
            if (selectedConversationUUID != null) {
                showMenuOfMessages(selectedConversationUUID);
            }
        }
    }

    public UUID conversationPickerMenu(String instructions, Set<UUID> conversations) {
        Function<UUID, String> fetchRoomMetadata = convoUUID -> conversationController.getConversationName(convoUUID);

        return consoleUtilities.singleUUIDPicker(instructions, conversations, fetchRoomMetadata);
    }

    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.signedInUserUUID = userController.getCurrentUser();

        String[] options = new String[]{
                "View all active conversations",
                "Create a new conversation",
                "Back"
        };

        boolean running = true;
        while (running) {
            int choice = consoleUtilities.singleSelectMenu("Messaging Menu Options", options);
            switch (choice) {
                case 1:
                    selectConversation();
                    break;
                case 2:
                    createConversation();
                    break;
                case 3:
                    running = false;
            }
        }

    }

}
