package gui.conference.rooms.menu;

import gui.conference.tabs.ConferenceTabsConstants;
import gui.conference.util.AbstractConferencePresenter;
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
    private int currentRoomIndex = -1;

    /**
     * @param mainFrame          main UI frame
     * @param conferenceRoomView view that this presenter is managing
     * @param conferenceUUID     UUID of the associated conference
     * @param defaultRoomUUID    UUID of the default room to select. If none selected, or invalid, the first one will be selected.
     */
    ConferenceRoomsPresenter(IFrame mainFrame, IConferenceRoomsView conferenceRoomView, UUID conferenceUUID, UUID defaultRoomUUID) {
        super(mainFrame, conferenceUUID);

        this.conferenceUUID = conferenceUUID;
        this.conferenceRoomView = conferenceRoomView;

        // Fetch local list of rooms
        updateRoomList();

        // Select default room
        if (roomUUIDs.size() > 0) {
            updateRoomLocations();

            int defaultRoomIndex = 0;

            if (roomUUIDs.contains(defaultRoomUUID)) {
                defaultRoomIndex = roomUUIDs.indexOf(defaultRoomUUID);
            }

            conferenceRoomView.setRoomListSelection(defaultRoomIndex);
            selectRoomPanel(defaultRoomIndex);
        }
    }

    /**
     * Initiates dialog for a user to create a room
     */
    void createRoom() {
        IDialog roomFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.ROOM_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        UUID newRoomUUID = (UUID) roomFormDialog.run();
        if (newRoomUUID != null) {
            reloadManageRoomsPage(newRoomUUID);
        }

    }

    /**
     * Updates the panel on the right side of the screen with the currently selected room
     *
     * @param index index of the room to open
     */
    void selectRoomPanel(int index) {
        selectRoomPanel(index, null);
    }

    /**
     * Updates the panel on the right side of the screen with the currently selected room
     *
     * @param index          index of the room to open
     * @param defaultTabName name of the room to open by default
     */
    void selectRoomPanel(int index, ConferenceTabsConstants.tabNames defaultTabName) {
        // Don't need to perform an update if we're already selected
        if (index != currentRoomIndex) {
            currentRoomIndex = index;
            UUID selectedRoomUUID = roomUUIDs.get(index);

            // Update UI with tabs for this room
            IPanel roomTabsPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_ROOM_DETAIL, new HashMap<String, Object>() {
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
     * Reloads page and specifies a default room to open upon next load
     *
     * @param selectedRoomUUID the UUID of the room to load
     */
    private void reloadManageRoomsPage(UUID selectedRoomUUID) {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>() {
            {
                put("defaultConferenceUUID", conferenceUUID);
                put("defaultRoomUUID", selectedRoomUUID);
                put("defaultTabName", ConferenceTabsConstants.tabNames.ROOMS);
            }
        }));
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
        }

        conferenceRoomView.setRoomList(roomLocations);
    }


}
