package gui.login;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class LoginView extends JFrame implements IPanel, ILoginView {
    private JPanel panel;
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton resetPasswordButton;
    private LoginPresenter loginPresenter;

    /**
     * Constructs the loginButton page
     *
     * @param mainFrame parent main frame
     */
    public LoginView(IFrame mainFrame) {
        loginPresenter = new LoginPresenter(mainFrame, this);

        registerButton.addActionListener((e) -> loginPresenter.goToRegister());
        loginButton.addActionListener((e) -> loginPresenter.login());
        resetPasswordButton.addActionListener((e) -> loginPresenter.resetPassword());

        // submit on ENTER
        panel.registerKeyboardAction((e) -> loginPresenter.login(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Getter for username
     * @return username of the user
     */
    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Getter for password
     * @return password of the user
     */
    @Override
    public String getPassword() {
        return String.valueOf(passwordField.getPassword());
    }

    /**
     * Getter for panel
     *
     * @return panel
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }
}
