package gui;

import user.UserController;
import util.ControllerBundle;

import javax.swing.*;

public class LoginUI implements Panelable {
    private JPanel panel;
    private JButton loginButton;

    private ControllerBundle controllerBundle;
    private UserController userController;

    private GUISystem guiSystem;

    /**
     * Constructs the loginButton page
     *
     * @param guiSystem parent gui system
     */
    LoginUI(GUISystem guiSystem) {
        this.guiSystem = guiSystem;

        controllerBundle = guiSystem.getControllerBundle();
        userController = controllerBundle.getUserController();

        loginButton.addActionListener((e) -> login());
    }

    private void login() {
        userController.login("wtf", "wtf");

        System.out.println(userController.getCurrentUser());

        guiSystem.refreshLogin();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
