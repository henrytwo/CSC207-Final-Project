package gui;

import user.UserController;
import util.ControllerBundle;

import javax.swing.*;
import java.util.function.Consumer;

public class MainMenuUI implements Panelable {
    private JPanel panel;
    private JButton logout;
    private JList list1;
    private JPasswordField passwordField1;
    private JTable table1;

    private Consumer<Panelable> setPanel;

    private ControllerBundle controllerBundle;
    private UserController userController;
    private Panelable parent;

    /**
     * Constructs the main menu
     *
     * @param controllerBundle bundle of controllers used by the system
     * @param setPanel         set the current panel in the frame
     * @param parent           parent panelable object
     */
    MainMenuUI(ControllerBundle controllerBundle, Consumer<Panelable> setPanel, Panelable parent) {
        this.parent = parent;
        this.setPanel = setPanel;
        this.controllerBundle = controllerBundle;
        this.userController = controllerBundle.getUserController();

        System.out.println("Current user: " + userController.getCurrentUser());

        logout.addActionListener((e) -> logout());
    }

    private void logout() {
        userController.logout();
        setPanel.accept(parent);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
