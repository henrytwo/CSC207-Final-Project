package gui.conference;

import gui.util.interfaces.IPanel;
import java.util.UUID;


public interface IConferenceMenuView {
    void setConferenceList(String[] conferenceNames);
    void setConferenceListSelection(int selectionIndex);
    void setConferenceTabs(IPanel tabsPanel);
}
