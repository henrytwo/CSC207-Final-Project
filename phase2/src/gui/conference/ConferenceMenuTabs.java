package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.util.UUID;
import javax.swing.*;

public class ConferenceMenuTabs implements IPanel, IConfeerenceMenuTabs {
    private JTabbedPane conferenceTabs;
    private JPanel generalPanel;
    private JPanel eventsPanel;
    private JPanel roomsPanel;
    private JPanel settingsPanel;
    private JPanel conferenceMenuTabsPanel;
    private JLabel test;

    public ConferenceMenuTabs(IFrame mainFrame, UUID conferenceUUID) {

        // temp
        test.setText(conferenceUUID.toString());

    }

    @Override
    public JPanel getPanel() {
        return conferenceMenuTabsPanel;
    }
}
