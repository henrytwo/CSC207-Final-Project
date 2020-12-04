package gui.conference.tabs;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceTabsView implements IPanel, IConferenceTabsView {
    private JTabbedPane conferenceTabs;
    private JPanel generalPanel;
    private JPanel allEventsPanel;
    private JPanel roomsPanel;
    private JPanel settingsPanel;
    private JPanel conferenceTabsPanel;
    private JPanel registeredEvents;
    private JPanel speakerEvents;

    private ConferenceTabsConstants conferenceTabsConstants = new ConferenceTabsConstants();

    private ConferenceTabsPresenter conferenceTabsPresenter;

    public ConferenceTabsView(IFrame mainFrame, UUID conferenceUUID, ConferenceTabsConstants.tabNames defaultTabName) {
        conferenceTabsPresenter = new ConferenceTabsPresenter(mainFrame, this, conferenceUUID);

        if (defaultTabName != null) {
            setSelectedTab(defaultTabName);
        }
    }

    @Override
    public void setSelectedTab(ConferenceTabsConstants.tabNames tabName) {
        conferenceTabs.setSelectedIndex(conferenceTabsConstants.getTabIndex(tabName));
    }

    @Override
    public void setTabEnabled(ConferenceTabsConstants.tabNames tabName, boolean state) {
        conferenceTabs.setEnabledAt(conferenceTabsConstants.getTabIndex(tabName), state);
    }

    @Override
    public void setGeneralTabPanel(IPanel panel) {
        generalPanel.add(panel.getPanel());
    }

    @Override
    public void setSettingsTabPanel(IPanel panel) {
        settingsPanel.add(panel.getPanel());
    }

    @Override
    public void setRoomsTabPanel(IPanel panel) {
        roomsPanel.add(panel.getPanel());
    }

    @Override
    public void setSpeakersTabPanel(IPanel panel){
        speakerEvents.add(panel.getPanel());
    }

    @Override
    public void setAllEventsTabPanel(IPanel panel){
        allEventsPanel.add(panel.getPanel());
    }

    @Override
    public void setRegisteredEventsTabPanel(IPanel panel){
        registeredEvents.add(panel.getPanel());
    }

    @Override
    public JPanel getPanel() {
        return conferenceTabsPanel;
    }


}
