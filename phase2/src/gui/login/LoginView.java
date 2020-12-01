package gui.login;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame implements IPanel, ILoginView, ActionListener {
    private JPanel panel;
    private JLabel user_label, password_label, message;
    private JButton loginButton;
    private JButton godLoginButton;
    private JTextField userName_text;
    private JPasswordField password_text;
    private JButton submit, cancel;
    private LoginPresenter loginPresenter;

//    /**
//     * Constructs the loginButton page
//     *
//     * @param mainFrame parent gui system
//     */
//    public LoginView(IFrame mainFrame) {
//        loginPresenter = new LoginPresenter(mainFrame, this);
//
//        loginButton.addActionListener((e) -> loginPresenter.login());
//        godLoginButton.addActionListener((e) -> loginPresenter.loginAsGod());
//    }
    LoginView() {
        // Username Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        userName_text = new JTextField();
        // Password Label
        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();
        // Submit
        submit = new JButton("SUBMIT");
        panel = new JPanel(new GridLayout(3, 1));
        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);
        message = new JLabel();
        panel.add(message);
        panel.add(submit);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(450,350);
        setVisible(true);
    }
    public static void main(String[] args) {
        new LoginView();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
