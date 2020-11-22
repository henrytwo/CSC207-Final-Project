package gui;

import contact.ContactController;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import messaging.ConversationController;
import user.UserController;
import util.ControllerBundle;

import javax.swing.*;

public class MainMenu {
    private JPanel panel;
    private JButton button1;
    private JList list1;
    private JPasswordField passwordField1;
    private JTable table1;

    ControllerBundle controllerBundle;
    UserController userController;

    /**
     * Constructs the main menu
     *
     * @param controllerBundle
     */
    public MainMenu(ControllerBundle controllerBundle) {
        this.controllerBundle = controllerBundle;
        this.userController = controllerBundle.getUserController();

        System.out.println("Current user: " + userController.getCurrentUser());
    }

    public JPanel getPanel() {
        return panel;
    }
}
