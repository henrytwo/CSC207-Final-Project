package gui.conference.rooms.details;

import gui.conference.AbstractConferencePresenter;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.UUID;

public class RoomDetailsPresenter extends AbstractConferencePresenter {


    private UUID roomUUID;

    private IRoomDetailsView roomDetailsView;

    RoomDetailsPresenter(IFrame mainFrame, IRoomDetailsView roomDetailsView, UUID conferenceUUID, UUID roomUUID) {
        super(mainFrame, conferenceUUID);
        this.roomDetailsView = roomDetailsView;
        this.roomUUID = roomUUID;

        updateRoomData();
    }

//    private void updateRoomsView() {
//        if (hasOrganizerPermissions) {
//            IPanel roomView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_ROOMS, new HashMap<String, Object>() {
//                {
//                    put("conferenceUUID", conferenceUUID);
//                }
//            });
//        }
//
//    }

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
