package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;
import user.UserController;

import java.util.*;
import java.util.function.Function;

import messaging.Message;

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

    /**
     * Prints a list of messages sent in this Chat(Conversation)
     * @param conversationUUID The Id of the selected conversation for which the messages are to be shown
     */
    public void showMenuOfMessages(UUID conversationUUID){
        // prints list of messages in a particular conversation and then takes in an input from the user if they want to
        // send a message otherwise returns back to previous menu
        Scanner enteredMessage = new Scanner(System.in);
        String conversationName = conversationController.getConversationName(conversationUUID);
        System.out.println("Showing messages for Chat group: " + conversationName);
        ArrayList<Map<String, String>> arrayListMessagesMap = conversationController.getMessages(signedInUserUUID, conversationUUID);
        ArrayList<String> messageSet = new ArrayList<>();
        for(Map<String, String> messageMap: arrayListMessagesMap){
            String messageInfo = "[" + messageMap.get("sender") + '@'+ messageMap.get("timestamp") + "] " +
                    messageMap.get("content");
            messageSet.add(messageInfo);
        }
        for(String message: messageSet){
            System.out.println(message);
            System.out.println("------------------------------------------------------------------------------");
        }
        System.out.println("\n\n");
        System.out.print("Enter your message here");
        String newMessageToSend = enteredMessage.nextLine();
        if (newMessageToSend == ""){
            conversationController.sendMessage(signedInUserUUID, newMessageToSend, conversationUUID);
            System.out.println("  Message Sent  ");
        }
    }

    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.signedInUserUUID = userController.getCurrentUser();

        /**
         * TODO: Remove this placeholder code
         */

        consoleUtilities.clearConsole();
        Set<UUID> conversationList = conversationController.getConversationlist(signedInUserUUID);

        for (UUID conversationUUID : conversationList) {
            System.out.printf("Conversation: %s\n", conversationController.getConversationName(conversationUUID));
            System.out.println(conversationController.getMessages(signedInUserUUID, conversationUUID));
        }

        consoleUtilities.confirmBox("This should be the messaging menu");

    }

}
