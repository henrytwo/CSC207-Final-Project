package gui.register;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class RegisterView extends JFrame implements IPanel, IRegisterView {
    private JPanel panel;
    private JTextField FirstNameTextField;
    private JTextField lastNameTextField;
    private JTextField userNameTextField;
    private JPasswordField passwordPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private RegisterPresenter registerPresenter;

    public RegisterView(IFrame mainFrame) {
        registerPresenter = new RegisterPresenter(mainFrame, this);

        loginButton.addActionListener((e) -> registerPresenter.goToLogin());
        registerButton.addActionListener((e) -> registerPresenter.register());

        // submit on ENTER
        panel.registerKeyboardAction((e) -> registerPresenter.register(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public String getFirstName() {
        return FirstNameTextField.getText();
    }

    @Override
    public String getLastName() {
        return lastNameTextField.getText();
    }

    @Override
    public String getUsername() {
        return userNameTextField.getText();
    }

    @Override
    public String getPassword() {
        return String.valueOf(passwordPasswordField.getPassword());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
