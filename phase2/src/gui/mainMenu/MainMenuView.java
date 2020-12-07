package gui.mainMenu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;

/**
 * Main menu GUI
 */
public class MainMenuView implements IPanel, IMainMenuView {
    private JPanel mainMenuPanel;
    private JButton logoutButton;
    private MainMenuPresenter mainMenuPresenter;
    private JTabbedPane mainMenuTabs;

    private JPanel conferenceMenuPanel;
    private JPanel messagingPanel;
    private JPanel contactsPanel;
    private JPanel scheduleDownloadPanel;
    private JPanel topBarPanel;
    private JLabel bottomMessageBar;
    private JButton surpriseButton;
    private JButton aboutButton;
    private JPanel bottomMessagePanel;

    /**
     * Constructs the main menu.
     *
     * @param mainFrame               main GUI frame
     * @param defaultTabIndex         tab that is opened by default on page load
     * @param initializationArguments HashMap of values that can be used to set the initial state of a panel
     */
    public MainMenuView(IFrame mainFrame, int defaultTabIndex, Map<String, Object> initializationArguments) {
        mainMenuPresenter = new MainMenuPresenter(mainFrame, this, initializationArguments);

        setTabIndex(defaultTabIndex);

        aboutButton.addActionListener((e) -> mainMenuPresenter.about());
        logoutButton.addActionListener((e) -> mainMenuPresenter.logout());
        surpriseButton.addActionListener((e) -> mainMenuPresenter.surprise());
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
    public void setScheduleDownloadPanel(IPanel panel) {
        scheduleDownloadPanel.add(panel.getPanel());
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
        bottomMessagePanel.setVisible(text.length() > 0);
        bottomMessageBar.setText(text);
    }
}
