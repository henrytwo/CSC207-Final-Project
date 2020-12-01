package gui.login;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class LoginView extends JFrame implements IPanel, ILoginView {
    private JPanel panel;
    private JButton loginButton;
    private JTextField username;
    private JPasswordField password;
    private LoginPresenter loginPresenter;

    /**
     * Constructs the loginButton page
     *
     * @param mainFrame parent gui system
     */
    public LoginView(IFrame mainFrame) {
        loginPresenter = new LoginPresenter(mainFrame, this);

        loginButton.addActionListener((e) -> loginPresenter.login());
    }

    public String getUsername() {
        return username.getText();
    }
    public String getPassword() {
        return String.valueOf(password.getPassword());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
