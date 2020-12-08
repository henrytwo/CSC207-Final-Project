package gui.conference.events.menu;

import gui.util.interfaces.IPanel;

/**
 * an interface for the Events view UI
 */
public interface IEventsMenuView {
    void setEventList(String[] eventNames);

    void setEventListSelection(int selectionIndex);

    void setEventTabs(IPanel tabsPanel);

    void setCreateEventButtonEnabled(boolean state);
}
