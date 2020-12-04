package gui.conference.event;

import gui.util.interfaces.IPanel;

public interface IEventMenuView {
    void setEventList(String[] eventNames);
    void setEventListSelection(int selectionIndex);
    void setEventPane(IPanel tabsPanel);
}
