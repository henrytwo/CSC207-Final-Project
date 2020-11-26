package gui.conference.tabs;

import gui.util.interfaces.IPanel;

import java.util.UUID;

public interface IConferenceTabsView {
    void setGeneralTabPanel(IPanel panel);
    void setTabEnabled(int tabIndex, boolean state);
}
