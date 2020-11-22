package gui.login;

import gui.GUISystem;
import gui.util.interfaces.IPanel;
import user.UserController;
import util.ControllerBundle;

import javax.swing.*;

public class LoginView implements IPanel {
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
    public LoginView(GUISystem guiSystem) {
        this.guiSystem = guiSystem;

        controllerBundle = guiSystem.getControllerBundle();
        userController = controllerBundle.getUserController();

        loginButton.addActionListener((e) -> login());
    }

    private void login() {
        userController.registerUser("wtf", "wtf", "wtf", "wtf");
        userController.login("wtf", "wtf");

        System.out.println(userController.getCurrentUser());

        guiSystem.refreshLogin();
    }

    @Override
    public IPanel getParent() {
        return null;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
