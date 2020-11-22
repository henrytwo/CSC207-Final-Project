package gui.mainMenu;

import gui.GUISystem;
import gui.util.Panelable;
import gui.mainMenu.conference.ConferenceMenuView;
import user.UserController;
import util.ControllerBundle;

import javax.swing.*;

public class MainMenuUI implements Panelable {
    private JPanel panel;
    private JButton logoutButton;
    private JList list1;
    private JPasswordField passwordField1;
    private JTable table1;
    private JButton conferencesButton;

    private ControllerBundle controllerBundle;
    private UserController userController;
    private GUISystem guiSystem;

    /**
     * Constructs the main menu. This is a root UI component, which has no parent.
     *
     * @param guiSystem parent gui system
     */
    public MainMenuUI(GUISystem guiSystem) {
        this.guiSystem = guiSystem;

        controllerBundle = guiSystem.getControllerBundle();
        userController = controllerBundle.getUserController();

        System.out.println("Current user: " + userController.getCurrentUser());

        logoutButton.addActionListener((e) -> logout());
        conferencesButton.addActionListener((e) -> openConferences());
    }

    private void logout() {
        userController.logout();
        guiSystem.refreshLogin();
    }

    private void openConferences() {
        guiSystem.setPanel(new ConferenceMenuView(guiSystem, this));
    }

    @Override
    public Panelable getParent() {
        return null;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
