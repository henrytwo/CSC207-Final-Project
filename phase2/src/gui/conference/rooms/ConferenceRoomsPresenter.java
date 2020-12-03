package gui.conference.rooms;

import gui.conference.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.UUID;

public class ConferenceRoomsPresenter extends AbstractConferencePresenter {

    private IConferenceRoomsView conferenceRoomView;
    protected UUID conferenceUUID;

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

    }


//    private void updateRoomData() {
//
//        String[][] tableData = {
//                //{"Room UUID", roomController.createRoom(conferenceUUID, signedInUserUUID, location, capacity)},
//                {"Room UUID", roomUUID.toString()},
//                {"Room Location", roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID)},
//                {"Room Capacity", String.valueOf(roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID))},
//        };
//
//        String[] columnNames = {
//                "Key",
//                "Value"
//        };
//
//        conferenceRoomView.setTableData(tableData, columnNames);
//    }


}
