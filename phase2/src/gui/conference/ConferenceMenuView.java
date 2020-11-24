package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ConferenceMenuView implements IPanel, IConferenceMenuView {
    private JPanel panel;
    private JList conferenceList;
    private JTabbedPane conferenceTabs;
    private JPanel roomsPanel;
    private JPanel settingsPanel;
    private JPanel eventsPanel;
    private JPanel generalPanel;

    private ConferenceMenuPresenter conferenceMenuPresenter;

    /**
     * @param guiSystem gui system
     */
    public ConferenceMenuView(IFrame guiSystem) {
        conferenceMenuPresenter = new ConferenceMenuPresenter(guiSystem, this);
    }

    @Override
    public JPanel getMainMenuPanel() {
        return panel;
    }
}
