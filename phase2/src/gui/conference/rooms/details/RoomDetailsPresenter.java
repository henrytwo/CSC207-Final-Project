package gui.conference.rooms.details;

import convention.exception.RoomInUseException;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.conference.util.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.UUID;

class RoomDetailsPresenter extends AbstractConferencePresenter {

    private UUID roomUUID;

    private IRoomDetailsView roomDetailsView;


    /**
     * Constructor for Room details presenter
     *
     * @param mainFrame       main GUI frame
     * @param roomDetailsView view object for room details UI
     * @param conferenceUUID  UUID of associated conference
     * @param roomUUID        UUID of associated room
     */
    RoomDetailsPresenter(IFrame mainFrame, IRoomDetailsView roomDetailsView, UUID conferenceUUID, UUID roomUUID) {
        super(mainFrame, conferenceUUID);
        this.roomDetailsView = roomDetailsView;
        this.roomUUID = roomUUID;

        updateRoomData();
    }

    /**
     * Deletes the selected room after confirming the deletion through a dialog.
     */
    void deleteRoom() {
        IDialog confirmDeleteDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you want to DELETE this room? You CANNOT undo this. (%s)", roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID)));
                put("title", "Confirm delete room");
                put("messageType", DialogFactoryOptions.dialogType.WARNING);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if ((Boolean) confirmDeleteDialog.run()) {
            try {
                roomController.deleteRoom(conferenceUUID, signedInUserUUID, roomUUID);
            } catch (RoomInUseException e) {
                IDialog roomInUsedExceptionDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("title", "Error");
                        put("message", String.format("Unable to delete room: %s", e.getMessage()));
                        put("messageType", DialogFactoryOptions.dialogType.ERROR);
                    }
                });

                roomInUsedExceptionDialog.run();
            }

            reloadManageRoomsPage(null);
        }
    }

    /**
     * Allows God users to edit the room location and capacity by prompting the room form dialog.
     */
    void editRoom() {
        //Only god user and Organizers are allowed to edit rooms.;
        IDialog roomFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.ROOM_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
                put("roomUUID", roomUUID);
            }
        });

        if (roomFormDialog.run() != null) {
            // Reload to update changes
            reloadManageRoomsPage(roomUUID);
        }
    }

    /**
     * Reloads page and specifies a default room to open upon next load
     *
     * @param selectedRoomUUID
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
     * Updates the room table data.
     */
    private void updateRoomData() {
        String[][] tableData = {
                {"Room UUID", roomUUID.toString()},
                {"Room Location", roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID)},
                {"Room Capacity", String.valueOf(roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID))},
        };

        String[] columnNames = {
                "Key",
                "Value"
        };

        roomDetailsView.setRoomTableData(tableData, columnNames);
    }

}
