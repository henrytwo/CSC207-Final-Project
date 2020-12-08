package gui.conference.tabs;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

/**
 * view for conference tabs
 */
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

    /**
     * the conference tabs view
     * @param mainFrame
     * @param conferenceUUID
     * @param defaultTabName
     * @param initializationArguments
     */
    public ConferenceTabsView(IFrame mainFrame, UUID conferenceUUID, ConferenceTabsConstants.tabNames defaultTabName, Map<String, Object> initializationArguments) {
        ConferenceTabsPresenter conferenceTabsPresenter = new ConferenceTabsPresenter(mainFrame, this, conferenceUUID, initializationArguments);

        if (defaultTabName != null) {
            setSelectedTab(defaultTabName);
        }
    }

    /**
     * sets the selected tab
     *
     * @param tabName the tab being selected
     */
    @Override
    public void setSelectedTab(ConferenceTabsConstants.tabNames tabName) {
        conferenceTabs.setSelectedIndex(conferenceTabsConstants.getTabIndex(tabName));
    }

    /**
     * sets whether a tab is enabled or not
     *
     * @param tabName the tab name
     * @param state whether the tab is enabled or disabled
     */
    @Override
    public void setTabEnabled(ConferenceTabsConstants.tabNames tabName, boolean state) {
        conferenceTabs.setEnabledAt(conferenceTabsConstants.getTabIndex(tabName), state);
    }

    /**
     * sets the general tab panel
     *
     * @param panel the panel
     */
    @Override
    public void setGeneralTabPanel(IPanel panel) {
        generalPanel.add(panel.getPanel());
    }

    /**
     * sets the settings tab panel
     *
     * @param panel the settings tab panel
     */
    @Override
    public void setSettingsTabPanel(IPanel panel) {
        settingsPanel.add(panel.getPanel());
    }

    /**
     * sets the rooms tab panel
     *
     * @param panel the panel
     */
    @Override
    public void setRoomsTabPanel(IPanel panel) {
        roomsPanel.add(panel.getPanel());
    }

    /**
     * sets the speakers tab panel
     */
    @Override
    public void setSpeakersTabPanel(IPanel panel) {
        speakerEvents.add(panel.getPanel());
    }

    /**
     * sets the all events tab panel
     *
     * @param panel the panel
     */
    @Override
    public void setAllEventsTabPanel(IPanel panel) {
        allEventsPanel.add(panel.getPanel());
    }

    /**
     * sets the registered events tab
     *
     * @param panel the panel
     */
    @Override
    public void setRegisteredEventsTabPanel(IPanel panel) {
        registeredEvents.add(panel.getPanel());
    }

    /**
     * gets a panel
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return conferenceTabsPanel;
    }


}
