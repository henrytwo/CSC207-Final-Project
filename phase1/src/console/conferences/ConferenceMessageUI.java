package console.conferences;

import console.ConsoleUtilities;
import console.conversation.MessagingUI;
import convention.ConferenceController;
import messaging.ConversationController;
import user.UserController;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConferenceMessageUI {

    ConferenceController conferenceController;
    UserController userController;
    ConsoleUtilities consoleUtilities;
    ConversationController conversationController;

    public ConferenceMessageUI(ConferenceController conferenceController, UserController userController, ConversationController conversationController) {
        this.conferenceController = conferenceController;
        this.userController = userController;
        this.consoleUtilities = new ConsoleUtilities(userController);
        this.conversationController = conversationController;
    }

    /**
     * Create a conversation with any number of users in this conference
     *
     * @param conferenceUUID UUID of the selected conference
     * @param availableUsers Set of UUID of users who should be available to add to the conversation
     */
    void messageUsers(UUID conferenceUUID, Set<UUID> availableUsers) {
        UUID signedInUserUUID = userController.getCurrentUser();

        // Remove the current user from the list, since you can't message yourself
        availableUsers.remove(signedInUserUUID);

        if (availableUsers.size() == 0) {
            consoleUtilities.confirmBoxClear("No users available.");
        } else {
            // Add the current user as a user in the convo
            Set<UUID> conversationUserUUIDs = new HashSet<>();
            conversationUserUUIDs.add(signedInUserUUID);

            Set<UUID> pickedUserUUIDs = consoleUtilities.userPicker("Choose users to create a conversation with", availableUsers);

            // Null means the user want to cancel
            if (pickedUserUUIDs != null) {
                // Choose users to add to the convo
                conversationUserUUIDs.addAll(pickedUserUUIDs);

                // Actually create the conversation
                UUID newConversationUUID = conferenceController.createConversationWithUsers(conferenceUUID, signedInUserUUID, conversationUserUUIDs);
                consoleUtilities.confirmBoxClear(String.format("New conversation created with %d users (including you)", conversationUserUUIDs.size()));

                MessagingUI messagingUI = new MessagingUI(userController, conversationController);
                messagingUI.showMenuOfMessages(newConversationUUID);
            }
        }
    }
}
