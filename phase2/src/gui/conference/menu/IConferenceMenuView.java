package gui.conference.menu;

import gui.util.interfaces.IPanel;

/**
 * Interface for ConferenceMenuView
 */
public interface IConferenceMenuView {
    void setConferenceList(String[] conferenceNames);

    void setConferenceListSelection(int selectionIndex);

    void setConferenceTabs(IPanel tabsPanel);
}
