package gui;

import contact.ContactController;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import messaging.ConversationController;
import user.UserController;

import javax.swing.*;

public class GUISystem {
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
    public GUISystem(UserController userController, ContactController contactController, ConversationController conversationController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;
        this.contactController = contactController;
        this.conversationController = conversationController;
        this.roomController = roomController;
        this.eventController = eventController;
        this.conferenceController = conferenceController;
    }

    /**
     * Runs the main UI loop
     * <p>
     * If the user is not logged in, present login/register prompts. Otherwise, send them to the main menu.
     */
    public void run() {

        // this is some serious testing stuff... so I'm not sure if this is how it's supposed to work
        // also, this stuff seems to run on a different thread, so we gotta fix how saving to disk is done
        JFrame frame = new JFrame("Main Menu");
        frame.setContentPane(new MainMenu().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}
