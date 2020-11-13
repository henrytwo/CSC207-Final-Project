package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;
import user.UserController;

import java.util.UUID;

public class MessagingUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    ConversationController conversationController;
    UserController userController;
    UUID userUUID;

    public MessagingUI(UserController userController, ConversationController conversationController) {
        this.conversationController = conversationController;
        this.userController = userController;
    }

    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.userUUID = userController.getCurrentUser();

        consoleUtilities.confirmBoxClear("This should be the messaging menu");
    }
}
