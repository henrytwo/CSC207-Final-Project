package gui.conference.rooms.details;

import convention.ConferenceController;
import convention.RoomController;
import gui.conference.AbstractConferencePresenter;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.factories.DialogFactory;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.UUID;

public class RoomDetailsPresenter extends AbstractConferencePresenter {


    private UUID roomUUID;

    private IRoomDetailsView roomDetailsView;

    private boolean hasAttendeePermissions;
    private boolean hasSpeakerPermissions;
    private boolean hasOrganizerPermissions;


    RoomDetailsPresenter(IFrame mainFrame, IRoomDetailsView roomDetailsView, UUID conferenceUUID, UUID roomUUID) {
        super(mainFrame, conferenceUUID);
        this.roomDetailsView = roomDetailsView;
        this.roomUUID = roomUUID;
        hasOrganizerPermissions = conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);
        hasSpeakerPermissions = conferenceController.isSpeaker(conferenceUUID, signedInUserUUID, signedInUserUUID) || hasOrganizerPermissions;
        hasAttendeePermissions = conferenceController.isAttendee(conferenceUUID, signedInUserUUID, signedInUserUUID) || hasSpeakerPermissions;

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

    void editRoom(){
        //Only god user and Organizers are allowed to edit rooms.;
        IDialog roomFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.ROOM_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        if (roomFormDialog.run() != null) {
            // Reload to update changes
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_ROOMS, new HashMap<String, Object>() {
                {
                    put("defaultConferenceUUID", conferenceUUID);
                }
            }));
        }
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
