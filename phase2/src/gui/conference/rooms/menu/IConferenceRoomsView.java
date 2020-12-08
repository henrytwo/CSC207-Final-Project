package gui.conference.rooms.menu;

import gui.util.interfaces.IPanel;

/**
 * Interface for ConferenceRoomsView
 */
public interface IConferenceRoomsView {
    void setRoomTabs(IPanel tabsPanel);

    void setRoomList(String[] conferenceNames);

    void setRoomListSelection(int selectionIndex);

}
