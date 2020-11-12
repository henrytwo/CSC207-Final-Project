import contact.ContactController;
import contact.ContactManager;
import messaging.ConversationController;
import messaging.ConversationManager;
import messaging.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceSystem {
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void run() {
        // Setup logger
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        // write some test code here, or write unit tests somewhere else

        ContactManager contactManager = new ContactManager();
        ConversationManager conversationManager = new ConversationManager();
        ContactController contactController = new ContactController(contactManager);
        ConversationController conversationController = new ConversationController(contactManager, conversationManager);

        // Testing message sending
        UUID me = UUID.randomUUID();
        UUID otherUser = UUID.randomUUID();

        // Create a chat with a single person
        UUID convo1 = conversationController.initiateConversation("My cool chat", me, new HashSet<>() {

        }, new Message(me, "Hi there"));

        // Send test messages
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "asdasd"), convo1);
        conversationController.sendMessage(new Message(me, "Cool message"), convo1);

        UUID convo2 = conversationController.initiateConversation("My cool chat", me, new HashSet<>() {

        }, new Message(me, "Nice convo bro"));


        // Ok, now the user can take a look of the convos they're a part of
        Set<UUID> convoList = conversationController.getConversationlist(me);

        for(UUID convoUUID : convoList) {

            System.out.println("\n\nConvo UUID:" + convoUUID + "\n" + conversationController.getMessages(me, convoUUID));

        }
    }
}
