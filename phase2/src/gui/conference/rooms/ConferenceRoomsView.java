package gui.conference.rooms;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceRoomsView implements IPanel, IConferenceRoomsView {
    private JPanel roomsViewPanel;
    private JSplitPane roomsSplitPane;
    private JButton createRoomButton;
    private JList roomsList;
    private JPanel RoomDetailView;

    private ConferenceRoomsPresenter conferenceRoomsPresenter;

    public ConferenceRoomsView(IFrame mainFrame, UUID conferenceUUID) {
        ConferenceRoomsPresenter conferenceRoomsPresenter = new ConferenceRoomsPresenter(mainFrame, this, conferenceUUID);
        createRoomButton.addActionListener((e) -> conferenceRoomsPresenter.createRoom());

    }


    @Override
    public JPanel getPanel() {
        return roomsViewPanel;
    }
}
