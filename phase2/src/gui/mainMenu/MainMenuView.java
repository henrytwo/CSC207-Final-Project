package gui.mainMenu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel, IMainMenuView {
    private JPanel mainMenuPanel;
    private JButton logoutButton;
    private JTabbedPane mainMenuTabs;
    private MainMenuPresenter mainMenuPresenter;

    private JPanel conferenceMenuPanel;
    private JPanel messagingPanel;
    private JPanel contactsPanel;
    private JPanel topbar;
    private JLabel topbarLabel;

    /**
     * Constructs the main menu.
     *
     * @param guiSystem gui system
     */
    public MainMenuView(IFrame guiSystem) {
        mainMenuPresenter = new MainMenuPresenter(guiSystem, this);

        logoutButton.addActionListener((e) -> mainMenuPresenter.logout());
    }

    @Override
    public void setConferenceMenuPanel(IPanel panel) {
        conferenceMenuPanel.add(panel.getMainMenuPanel());
    }

    @Override
    public void setMessagingPanel(IPanel panel) {
        messagingPanel.add(panel.getMainMenuPanel());
    }

    @Override
    public void setContactsPanel(IPanel panel) {
        contactsPanel.add(panel.getMainMenuPanel());
    }

    @Override
    public void setLogoutButtonText(String text) {
        logoutButton.setText(text);
    }

    @Override
    public JPanel getMainMenuPanel() {
        return mainMenuPanel;
    }
}
