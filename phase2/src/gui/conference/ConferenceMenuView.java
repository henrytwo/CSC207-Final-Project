package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ConferenceMenuView implements IPanel, IConferenceMenuView {
    private JPanel panel;
    private JTabbedPane conferenceTabs;
    private JPanel roomsPanel;
    private JPanel settingsPanel;
    private JPanel eventsPanel;
    private JPanel generalPanel;
    private JButton createConferenceButton;
    private JList conferenceList;
    private JSplitPane conferenceSplitPane;

    private ConferenceMenuPresenter conferenceMenuPresenter;

    /**
     * @param guiSystem gui system
     */
    public ConferenceMenuView(IFrame guiSystem) {
        conferenceMenuPresenter = new ConferenceMenuPresenter(guiSystem, this);

        conferenceList.addListSelectionListener((e) -> conferenceMenuPresenter.selectConference(conferenceList.getSelectedIndex()));
    }

    @Override
    public void setConferenceListSelection(int selectionIndex) {
        conferenceList.setSelectedIndex(selectionIndex);
    }

    @Override
    public void setConferenceList(String[] conferenceNames) {
        conferenceList.setListData(conferenceNames);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
