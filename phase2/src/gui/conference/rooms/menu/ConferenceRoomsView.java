package gui.conference.rooms.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceRoomsView implements IPanel, IConferenceRoomsView {
    private JPanel panel;
    private JButton createRoomButton;
    private JList roomsList;
    private JSplitPane roomsSplitPane;

    public ConferenceRoomsView(IFrame mainFrame, UUID conferenceUUID, UUID defaultRoomUUID) {
        ConferenceRoomsPresenter conferenceRoomsPresenter = new ConferenceRoomsPresenter(mainFrame, this, conferenceUUID, defaultRoomUUID);
        createRoomButton.addActionListener((e) -> conferenceRoomsPresenter.createRoom());

        roomsList.addListSelectionListener((e) -> conferenceRoomsPresenter.selectRoomPanel(roomsList.getSelectedIndex()));
    }

    @Override
    public void setRoomTabs(IPanel tabsPanel) {
        roomsSplitPane.setRightComponent(tabsPanel.getPanel());
    }

    @Override
    public void setRoomList(String[] roomLocations) {
        roomsList.setListData(roomLocations);
    }

    @Override
    public void setRoomListSelection(int selectionIndex) {
        roomsList.setSelectedIndex(selectionIndex);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
