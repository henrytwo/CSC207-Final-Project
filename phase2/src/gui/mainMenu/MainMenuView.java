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

        logoutButton.addActionListener((e) -> mainMenuPresenter.logout());
        conferencesButton.addActionListener((e) -> mainMenuPresenter.conferenceMenu());
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
