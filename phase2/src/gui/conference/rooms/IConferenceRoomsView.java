package gui.conference.rooms;

import gui.util.interfaces.IPanel;

public interface IConferenceRoomsView {
    void setRoomTabs(IPanel tabsPanel);
    void setRoomList(String[] conferenceNames);
    void setRoomListSelection(int selectionIndex);
    //void setTableData(String[][] tableData, String[] columnNames);

}
