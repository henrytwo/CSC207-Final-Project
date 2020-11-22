package gui.mainMenu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel {
    private JPanel panel;
    private JButton logoutButton;
    private JList list1;
    private JPasswordField passwordField1;
    private JTable table1;
    private JButton conferencesButton;
    private JButton stuffButton;

    private MainMenuPresenter mainMenuPresenter;

    /**
     * Constructs the main menu.
     *
     * @param guiSystem gui system
     */
    public MainMenuView(IFrame guiSystem) {
        mainMenuPresenter = new MainMenuPresenter(guiSystem, this);

        stuffButton.setActionCommand("stuff");
        stuffButton.addActionListener(mainMenuPresenter);

        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(mainMenuPresenter);

        conferencesButton.setActionCommand("conferenceMenu");
        conferencesButton.addActionListener(mainMenuPresenter);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
