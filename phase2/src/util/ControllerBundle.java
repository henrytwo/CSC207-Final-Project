package util;

import contact.ContactController;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import messaging.ConversationController;
import user.UserController;

/**
 * Stores all the controllers in the system as a single object to make it easier to pass around the UI
 */
public class ControllerBundle {
    // User controller
    UserController userController;

    // Messaging controllers
    ContactController contactController;
    ConversationController conversationController;

    // Convention controllers
    RoomController roomController;
    EventController eventController;
    ConferenceController conferenceController;

    /**
     * Constructs the main UI system.
     *
     * @param userController
     * @param contactController
     * @param conversationController
     * @param roomController
     * @param eventController
     * @param conferenceController
     */
    public ControllerBundle(UserController userController, ContactController contactController, ConversationController conversationController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;
        this.contactController = contactController;
        this.conversationController = conversationController;
        this.roomController = roomController;
        this.eventController = eventController;
        this.conferenceController = conferenceController;
    }

    public UserController getUserController() {
        return userController;
    }

    public ConferenceController getConferenceController() {
        return conferenceController;
    }

    public ContactController getContactController() {
        return contactController;
    }

    public RoomController getRoomController() {
        return roomController;
    }

    public EventController getEventController() {
        return eventController;
    }

    public ConversationController getConversationController() {
        return conversationController;
    }
}
