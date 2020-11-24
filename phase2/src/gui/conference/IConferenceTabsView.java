package gui.conference;

import gui.util.interfaces.IPanel;

import java.util.UUID;

public interface IConferenceTabsView {
    void setGeneralTabPanel(IPanel panel);
    UUID getConferenceUUID();
    IPanel getParentPanel();
    void setTabEnabled(int tabIndex, boolean state);
}
