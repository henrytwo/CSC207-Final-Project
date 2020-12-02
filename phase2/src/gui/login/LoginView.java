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
    private LoginPresenter loginPresenter;

    /**
     * Constructs the loginButton page
     *
     * @param mainFrame parent gui system
     */
    public LoginView(IFrame mainFrame) {
        loginPresenter = new LoginPresenter(mainFrame, this);

        registerButton.addActionListener((e) -> loginPresenter.goToRegister());
        loginButton.addActionListener((e) -> loginPresenter.login());

        // submit on ENTER
        panel.registerKeyboardAction((e) -> loginPresenter.login(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public String getPassword() {
        return String.valueOf(passwordField.getPassword());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
