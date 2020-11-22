package gui.login;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class LoginView implements IPanel {
    private JPanel panel;
    private JButton loginButton;

    private LoginPresenter loginPresenter;

    /**
     * Constructs the loginButton page
     *
     * @param mainFrame parent gui system
     */
    public LoginView(IFrame mainFrame) {
        loginPresenter = new LoginPresenter(mainFrame, this);

        loginButton.setActionCommand("login");
        loginButton.addActionListener(loginPresenter);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
