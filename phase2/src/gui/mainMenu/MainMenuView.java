package gui.mainMenu;

import gui.GUISystem;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel {
    private JPanel panel;
    private JButton logoutButton;
    private JList list1;
    private JPasswordField passwordField1;
    private JTable table1;
    private JButton conferencesButton;

    private MainMenuPresenter mainMenuPresenter;

    /**
     * Constructs the main menu. This is a root UI component, which has no parent.
     *
     * @param guiSystem parent gui system
     */
    public MainMenuView(GUISystem guiSystem) {
        mainMenuPresenter = new MainMenuPresenter(guiSystem, this);

        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(mainMenuPresenter);

        conferencesButton.setActionCommand("conferenceMenu");
        conferencesButton.addActionListener(mainMenuPresenter);
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
