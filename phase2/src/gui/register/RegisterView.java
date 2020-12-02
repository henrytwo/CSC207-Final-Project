package gui.register;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class RegisterView extends JFrame implements IPanel, IRegisterView {
    private JPanel panel;
    private JTextField FIrstNameTextField;
    private JTextField lastNameTextField;
    private JTextField userNameTextField;
    private JPasswordField passwordPasswordField;
    private JButton registerButton;
    private RegisterPresenter RegisterPresenter;

    public RegisterView(IFrame mainFrame) {
        RegisterPresenter = new RegisterPresenter(mainFrame, this);

        registerButton.addActionListener((e) -> RegisterPresenter.register());
    }

    public String getFirstname(){
        return FIrstNameTextField.getText();
    }

    public String getLastname(){
        return lastNameTextField.getText();
    }

    public String getUsername() {
        return userNameTextField.getText();
    }


    public String getPassword() {
        return String.valueOf(passwordPasswordField.getPassword());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
