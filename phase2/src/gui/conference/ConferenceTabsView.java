package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.awt.*;
import java.util.UUID;
import javax.swing.*;

public class ConferenceTabsView implements IPanel, IConferenceTabsView {
    private JTabbedPane conferenceTabs;
    private JPanel generalPanel;
    private JPanel allEventsPanel;
    private JPanel roomsPanel;
    private JPanel settingsPanel;
    private JPanel conferenceTabsPanel;
    private JPanel registeredEvents;
    private JPanel speakerEvents;

    private ConferenceTabsPresenter conferenceTabsPresenter;

    private IPanel parentPanel;
    private UUID conferenceUUID;

    public ConferenceTabsView(IFrame mainFrame, IPanel parentPanel, UUID conferenceUUID) {
        this.parentPanel = parentPanel;
        this.conferenceUUID = conferenceUUID;

        conferenceTabsPresenter = new ConferenceTabsPresenter(mainFrame, this);

    }

    @Override
    public void setTabEnabled(int tabIndex, boolean state) {
        conferenceTabs.setEnabledAt(tabIndex, state);
    }

    @Override
    public IPanel getParentPanel() {
        return parentPanel;
    }

    @Override
    public UUID getConferenceUUID() {
        return conferenceUUID;
    }

    @Override
    public void setGeneralTabPanel(IPanel panel) {
        generalPanel.add(panel.getPanel());
    }

    @Override
    public JPanel getPanel() {
        return conferenceTabsPanel;
    }
}
