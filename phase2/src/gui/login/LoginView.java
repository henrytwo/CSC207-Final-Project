package gui.login;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class LoginView implements IPanel, ILoginView {
    private JPanel panel;
    private JButton loginButton;
    private JButton godLoginButton;

    private LoginPresenter loginPresenter;

    /**
     * Constructs the loginButton page
     *
     * @param mainFrame parent gui system
     */
    public LoginView(IFrame mainFrame) {
        loginPresenter = new LoginPresenter(mainFrame, this);

        loginButton.addActionListener((e) -> loginPresenter.login());
        godLoginButton.addActionListener((e) -> loginPresenter.loginAsGod());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
