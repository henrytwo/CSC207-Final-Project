package gui.conference.rooms.details;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

public class RoomDetailsView implements IPanel, IRoomDetailsView {
    private JPanel roomViewPanel;
    private JTable roomTable;
    private JButton editRoomButton;
    private JButton deleteRoomButton;

    private RoomDetailsPresenter roomDetailsPresenter;

    /**
     * Constructor for room details view
     *
     * @param mainFrame      main GUI frame
     * @param conferenceUUID UUID of the associated conference
     * @param roomUUID       UUID of the room whose details are being presented
     */
    public RoomDetailsView(IFrame mainFrame, UUID conferenceUUID, UUID roomUUID) {
        roomDetailsPresenter = new RoomDetailsPresenter(mainFrame, this, conferenceUUID, roomUUID);

        editRoomButton.addActionListener((e) -> roomDetailsPresenter.editRoom());
        deleteRoomButton.addActionListener((e) -> roomDetailsPresenter.deleteRoom());
    }

    /**
     * Sets the room table data
     *
     * @param tableData   2D array of table data
     * @param columnNames array of table data
     */
    @Override
    public void setRoomTableData(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);

        roomTable.setModel(tableModel);

    }

    /**
     * Gets the panel to display
     *
     * @return the room detail view panel
     */
    @Override
    public JPanel getPanel() {
        return roomViewPanel;
    }
}
