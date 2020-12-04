package gui.conference.rooms;

import gui.conference.AbstractConferencePresenter;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConferenceRoomsPresenter extends AbstractConferencePresenter {

    private IConferenceRoomsView conferenceRoomView;
    protected UUID conferenceUUID;

    private List<UUID> roomUUIDs;
    private int currentRoomIndex=-1;

    ConferenceRoomsPresenter(IFrame mainFrame, IConferenceRoomsView conferenceRoomView, UUID conferenceUUID) {
        super(mainFrame, conferenceUUID);

        this.conferenceUUID = conferenceUUID;
        this.conferenceRoomView = conferenceRoomView;
//        updateRoomData();
    }

    void createRoom() {
        IDialog roomFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.ROOM_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        UUID newRoomUUID = (UUID) roomFormDialog.run();
        if (newRoomUUID != null) {
            updateAndSelectNewRoom(newRoomUUID);
        }

    }

    /**
     * Updates the local list of rooms and selects a room by UUID
     *
     * @param selectedRoomUUID UUID of room to open
     */
    private void updateAndSelectNewRoom(UUID selectedRoomUUID) {
        // Update the local list with the new room
        updateRoomList();
        updateRoomLocations();

        // Select the latest room
        int index = roomUUIDs.indexOf(selectedRoomUUID);

        conferenceRoomView.setRoomListSelection(index);
        selectRoomPanel(index);
    }

    /**
     * Updates the panel on the right side of the screen with the currently selected room
     *
     * @param index          index of the room to open
     */
    void selectRoomPanel(int index) {
        selectRoomPanel(index, null);
    }

    /**
     * Updates the panel on the right side of the screen with the currently selected room
     *
     * @param index          index of the room to open
     * @param defaultTabName name of the tab to open by default
     */
    void selectRoomPanel(int index, ConferenceTabsConstants.tabNames defaultTabName) {
        // Don't need to perform an update if we're already selected
        if (index != currentRoomIndex) {
            currentRoomIndex = index;
            UUID selectedRoomUUID = roomUUIDs.get(index);

            // Update UI with tabs for this room
            IPanel roomTabsPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_ROOMS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", conferenceUUID);
                    put("roomUUID", selectedRoomUUID);
                    put("defaultTabName", defaultTabName);
                }
            });

            conferenceRoomView.setRoomTabs(roomTabsPanel);
        }
    }

    /**
     * Update the local list of room UUIDs
     */
    private void updateRoomList() {
        currentRoomIndex = -1;
        roomUUIDs = new ArrayList<>(roomController.getRooms(conferenceUUID, signedInUserUUID));
    }

    /**
     * Updates the local list of room locations
     */
    private void updateRoomLocations() {
        String[] roomLocations = new String[roomUUIDs.size()];

        for (int i = 0; i < roomUUIDs.size(); i++) {
            roomLocations[i] = roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUIDs.get(i));
            ;
        }

        conferenceRoomView.setRoomList(roomLocations);
    }


}
