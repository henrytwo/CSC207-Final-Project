package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;
import messaging.Message;
import messaging.exception.MessageDeniedException;
import user.UserController;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class MessagingUI {
    ConsoleUtilities consoleUtilities;
    ConversationController conversationController;
    UserController userController;
    UUID signedInUserUUID;

    private Scanner stdin = new Scanner(System.in);

    public MessagingUI(UserController userController, ConversationController conversationController) {
        this.conversationController = conversationController;
        this.userController = userController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    // sends a message to user with given UUID
    public void sendMessage() {
        String message_content = "test_message";
        Message message = new Message(this.signedInUserUUID, message_content);
        //TODO: send the message
    }

    /**
     *
     */
    public void createConversation(){
        try {
            System.out.println("Enter conversation name:");
            String convoName = stdin.nextLine();
            System.out.println("Enter UUID of user you want to create conversation with:");
            UUID receiverID = UUID.fromString(stdin.nextLine());
            System.out.println("Enter message:");
            String message = stdin.nextLine();
            HashSet<UUID> others = new HashSet<UUID>();
            others.add(receiverID);
            conversationController.initiateConversation(convoName, signedInUserUUID, others, signedInUserUUID, message);
        }catch (MessageDeniedException e){
            consoleUtilities.confirmBoxClear("Cannot send messages to users not on contact list.");
        }
    }

    /**
     *
     */
    public void selectConversation(){
        Set<UUID> conversationList = conversationController.getConversationlist(signedInUserUUID);

        if (conversationList.size() == 0) {
            consoleUtilities.confirmBoxClear("You currently do not have any active conversations.");
        }
        else{
            UUID selectedConversationUUID = conversationPickerMenu("Choose an active conversation to view.", conversationList);
            if(selectedConversationUUID != null){
                // Mahak's menu
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

//        /**
//         * TODO: Remove this placeholder code
//         */
//
//        consoleUtilities.clearConsole();
//        Set<UUID> conversationList = conversationController.getConversationlist(signedInUserUUID);
//
//        for(UUID conversationUUID : conversationList) {
//            System.out.printf("Conversation: %s\n", conversationController.getConversationName(conversationUUID));
//            System.out.println(conversationController.getMessages(signedInUserUUID, conversationUUID));
//        }
//
//        consoleUtilities.confirmBox("This should be the messaging menu");

        String[] options = new String[]{
                "View all active conversations.",
                "Create a new conversation."
        };

        boolean running = true;
        while(running){
            int choice = consoleUtilities.singleSelectMenu("Messaging Menu Options", options);
            switch (choice){
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
