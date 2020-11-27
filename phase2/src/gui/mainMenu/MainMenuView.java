package gui.mainMenu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Map;

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
     * @param defaultTabIndex tab that is opened by default on page load
     * @param initializationArguments hashmap of values that can be used to set the initial state of a panel
     */
    public MainMenuView(IFrame guiSystem, int defaultTabIndex, Map<String, Object> initializationArguments) {
        mainMenuPresenter = new MainMenuPresenter(guiSystem, this, initializationArguments);

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
