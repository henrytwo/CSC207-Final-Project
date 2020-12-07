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
    private UserController userController;

    // Messaging controllers
    private ContactController contactController;
    private ConversationController conversationController;

    // Convention controllers
    private RoomController roomController;
    private EventController eventController;
    private ConferenceController conferenceController;

    /**
     * Constructs controller bundle
     */
    public ControllerBundle(UserController userController, ContactController contactController, ConversationController conversationController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;
        this.contactController = contactController;
        this.conversationController = conversationController;
        this.roomController = roomController;
        this.eventController = eventController;
        this.conferenceController = conferenceController;
    }

    /**
     * Gets UserController
     *
     * @return
     */
    public UserController getUserController() {
        return userController;
    }

    /**
     * Gets ConferenceController
     *
     * @return
     */
    public ConferenceController getConferenceController() {
        return conferenceController;
    }

    /**
     * Gets ContactController
     *
     * @return
     */
    public ContactController getContactController() {
        return contactController;
    }

    /**
     * Gets RoomController
     *
     * @return
     */
    public RoomController getRoomController() {
        return roomController;
    }

    /**
     * Gets EventController
     *
     * @return
     */
    public EventController getEventController() {
        return eventController;
    }

    /**
     * Gets ConversationController
     *
     * @return
     */
    public ConversationController getConversationController() {
        return conversationController;
    }
}
