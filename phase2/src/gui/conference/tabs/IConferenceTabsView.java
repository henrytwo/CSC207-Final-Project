package gui.conference.tabs;

import gui.util.interfaces.IPanel;

public interface IConferenceTabsView {
    void setGeneralTabPanel(IPanel panel);
    void setSettingsTabPanel(IPanel panel);
    void setRoomsTabPanel(IPanel panel);
    void setSpeakersTabPanel(IPanel panel);
    void setAllEventsTabPanel(IPanel panel);
    void setRegisteredEventsTabPanel(IPanel panel);
    void setSelectedTab(ConferenceTabsConstants.tabNames tabName);
    void setTabEnabled(ConferenceTabsConstants.tabNames tabName, boolean state);

}
