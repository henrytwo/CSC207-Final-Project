package gui.conference.rooms;

import com.sun.xml.internal.bind.v2.runtime.ClassBeanInfoImpl;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;
import javax.swing.event.*;

public class ConferenceRoomsView implements IPanel, IConferenceRoomsView {
    private JPanel roomsViewPanel;
    private JSplitPane roomsSplitPane;
    private JButton createRoomButton;
    private JList roomsList;
    private JPanel RoomDetailView;

    private ConferenceRoomsPresenter conferenceRoomsPresenter;

    public ConferenceRoomsView(IFrame mainFrame, UUID conferenceUUID){
        ConferenceRoomsPresenter conferenceRoomsPresenter = new ConferenceRoomsPresenter(mainFrame, this, conferenceUUID);
        createRoomButton.addActionListener((e) -> conferenceRoomsPresenter.createRoom() );

    }


    @Override
    public JPanel getPanel() {
        return roomsViewPanel;
    }
}
