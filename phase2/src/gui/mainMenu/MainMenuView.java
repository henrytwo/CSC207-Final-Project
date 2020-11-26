package gui.mainMenu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MainMenuView implements IPanel, IMainMenuView {
    private JPanel mainMenuPanel;
    private JButton logoutButton;
    private MainMenuPresenter mainMenuPresenter;
    private JTabbedPane mainMenuTabs;

    private JPanel conferenceMenuPanel;
    private JPanel messagingPanel;
    private JPanel contactsPanel;
    private JPanel topBarPanel;
    private JLabel bottomMessageBar;

    /**
     * Constructs the main menu.
     *
     * @param guiSystem gui system
     */
    public MainMenuView(IFrame guiSystem) {
        this(guiSystem, 0);
    }

    public MainMenuView(IFrame guiSystem, int defaultTabIndex) {
        mainMenuPresenter = new MainMenuPresenter(guiSystem, this);

        setTabIndex(defaultTabIndex);

        logoutButton.addActionListener((e) -> mainMenuPresenter.logout());
    }

    @Override
    public void setTabIndex(int index) {
        mainMenuTabs.setSelectedIndex(index);
    }

    @Override
    public void setConferenceMenuPanel(IPanel panel) {
        conferenceMenuPanel.add(panel.getPanel());
    }

    @Override
    public void setMessagingPanel(IPanel panel) {
        messagingPanel.add(panel.getPanel());
    }

    @Override
    public void setContactsPanel(IPanel panel) {
        contactsPanel.add(panel.getPanel());
    }

    @Override
    public void setLogoutButtonText(String text) {
        logoutButton.setText(text);
    }

    @Override
    public JPanel getPanel() {
        return mainMenuPanel;
    }

    @Override
    public void setTopBarPanelText(String text) {
        bottomMessageBar.setVisible(text.length() > 0);
        bottomMessageBar.setText(text);
    }
}
