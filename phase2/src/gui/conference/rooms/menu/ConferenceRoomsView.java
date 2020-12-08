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

    /**
     *
     * @param mainFrame main GUI frame
     * @param conferenceUUID UUID of the associated conference
     * @param defaultRoomUUID UUID of the default room to select. If none selected, or invalid, the first one will be selected
     */
    public ConferenceRoomsView(IFrame mainFrame, UUID conferenceUUID, UUID defaultRoomUUID) {
        ConferenceRoomsPresenter conferenceRoomsPresenter = new ConferenceRoomsPresenter(mainFrame, this, conferenceUUID, defaultRoomUUID);
        createRoomButton.addActionListener((e) -> conferenceRoomsPresenter.createRoom());

        roomsList.addListSelectionListener((e) -> conferenceRoomsPresenter.selectRoomPanel(roomsList.getSelectedIndex()));
    }

    /**
     *
     * @param tabsPanel
     */
    @Override
    public void setRoomTabs(IPanel tabsPanel) {
        roomsSplitPane.setRightComponent(tabsPanel.getPanel());
    }

    /**
     * Sets the room list to be displayed
     *
     * @param roomLocations array of locations of the rooms
     */
    @Override
    public void setRoomList(String[] roomLocations) {
        roomsList.setListData(roomLocations);
    }

    /**
     * Sets the selected index of the room form the rooms list
     *
     * @param selectionIndex index of the selected room
     * */
    @Override
    public void setRoomListSelection(int selectionIndex) {
        roomsList.setSelectedIndex(selectionIndex);
    }

    /**
     * Gets the panel to display
     *
     * @return room view panel
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }
}
