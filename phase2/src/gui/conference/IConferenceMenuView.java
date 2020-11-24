package gui.conference;

import gui.util.interfaces.IPanel;

public interface IConferenceMenuView {
    void setConferenceList(String[] conferenceNames);
    void setConferenceListSelection(int selectionIndex);
    void setConferenceMenuTabs(IPanel tabsPanel);
}
