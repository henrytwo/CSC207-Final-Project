package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;
import user.UserController;

public class MessagingUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    ConversationController conversationController;
    UserController userController;

    public MessagingUI(UserController userController, ConversationController conversationController) {
        this.conversationController = conversationController;
        this.userController = userController;
    }

    public void run() {

        consoleUtilities.confirmBoxClear("This should be the messaging menu");
    }
}
