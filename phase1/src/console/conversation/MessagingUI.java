package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;
import user.UserController;

import java.util.Set;
import java.util.UUID;

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

    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.signedInUserUUID = userController.getCurrentUser();

        /**
         * TODO: Remove this placeholder code
         */
        consoleUtilities.clearConsole();
        Set<UUID> conversationList = conversationController.getConversationlist(signedInUserUUID);

        for(UUID conversationUUID : conversationList) {
            System.out.printf("Conversation: %s\n", conversationController.getConversationName(conversationUUID));
            System.out.println(conversationController.getMessages(signedInUserUUID, conversationUUID));
        }

        consoleUtilities.confirmBox("This should be the messaging menu");
    }
}
