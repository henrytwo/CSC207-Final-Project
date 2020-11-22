package gui;

import user.UserController;
import util.ControllerBundle;

import javax.swing.*;
import java.util.function.Consumer;

public class LoginUI implements Panelable {
    private JPanel panel;
    private JButton loginButton;

    private ControllerBundle controllerBundle;
    private UserController userController;

    private Consumer<Panelable> setPanel;

    /**
     * Constructs the loginButton page
     *
     * @param controllerBundle bundle of controllers used by the system
     * @param setPanel         set the current panel in the frame
     */
    LoginUI(ControllerBundle controllerBundle, Consumer<Panelable> setPanel) {
        this.setPanel = setPanel;
        this.controllerBundle = controllerBundle;
        this.userController = controllerBundle.getUserController();

        loginButton.addActionListener((e) -> login());

        // If the user is already logged in, redirect them straight to the main menu
        if (userController.getCurrentUser() != null) {

            setPanel.accept(new MainMenuUI(controllerBundle, setPanel, this));
        }
    }

    private void login() {

        userController.login("wtf", "wtf");

        System.out.println(userController.getCurrentUser());

        openMainMenu();
    }

    private void openMainMenu() {
        setPanel.accept(new MainMenuUI(controllerBundle, setPanel, this));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
