package console.conversation;

import console.ConsoleUtilities;
import messaging.ConversationController;

public class MessagingUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    ConversationController conversationController;

    public MessagingUI(ConversationController conversationController) {
        this.conversationController = conversationController;
    }

    public void run() {

        consoleUtilities.confirmBoxClear("This should be the messaging menu");
    }
}
