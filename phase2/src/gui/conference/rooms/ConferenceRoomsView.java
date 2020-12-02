package gui.conference.rooms;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

public class ConferenceRoomsView implements IPanel, IConferenceRoomsView {
    private JPanel roomsViewPanel;
    private JSplitPane roomsSplitPane;
    private JButton createRoomButton;
    private JList roomsList;

    private ConferenceRoomsPresenter conferenceRoomsPresenter;

    public ConferenceRoomsView(IFrame mainFrame, UUID conferenceUUID){
        ConferenceRoomsPresenter conferenceRoomsPresenter = new ConferenceRoomsPresenter(mainFrame, this, conferenceUUID);
    }

    @Override
    public JPanel getPanel() {
        return roomsViewPanel;
    }
}
