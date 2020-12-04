package gui.conference.rooms.menu;

import gui.util.interfaces.IPanel;

public interface IConferenceRoomsView {
    void setRoomTabs(IPanel tabsPanel);

    void setRoomList(String[] conferenceNames);

    void setRoomListSelection(int selectionIndex);
    //void setGeneralTableData(String[][] tableData, String[] columnNames);

}
