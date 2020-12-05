package gui.mainMenu;

import gui.util.interfaces.IPanel;

public interface IMainMenuView {
    void setLogoutButtonText(String text);

    void setConferenceMenuPanel(IPanel panel);

    void setMessagingPanel(IPanel panel);

    void setContactsPanel(IPanel panel);

    void setTabIndex(int index);

    void setTopBarPanelText(String text);
}
