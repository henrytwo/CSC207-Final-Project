package gui.mainMenu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel, IMainMenuView {
    private JPanel panel;
    private JButton logoutButton;
    private JButton conferencesButton;
    private JButton stuffButton;
    private JLabel signedInAs;

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
    public void setSignedInAs(String text) {
        signedInAs.setText(text);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
