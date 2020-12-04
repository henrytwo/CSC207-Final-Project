package gui.conference.events.menu;

import gui.util.interfaces.IPanel;

public interface IEventsMenuView {
    void setEventList(String[] eventNames);

    void setEventListSelection(int selectionIndex);

    void setEventTabs(IPanel tabsPanel);

    void setCreateEventButtonEnabled(boolean state);
}
