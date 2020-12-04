package gui.conference.rooms.details;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

public class RoomDetailsView implements IPanel, IRoomDetailsView{
    private JPanel roomViewPanel;
    private JTable roomTable;
    private JButton editRoomButton;

    private RoomDetailsPresenter roomDetailsPresenter;

    public RoomDetailsView(IFrame mainFrame, UUID conferenceUUID, UUID roomUUID) {
        roomDetailsPresenter = new RoomDetailsPresenter(mainFrame,this, conferenceUUID, roomUUID);

        editRoomButton.addActionListener((e) -> roomDetailsPresenter.editRoom());
    }

    @Override
    public void setRoomTableData(String[][] tableData, String [] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);

        roomTable.setModel(tableModel);

    }

    @Override
    public JPanel getPanel() {
        return roomViewPanel;
    }
}
