package gui.conference.room;

import convention.RoomController;
import gui.conference.AbstractConferencePresenter;
import gui.util.interfaces.IFrame;
import user.UserController;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import util.ControllerBundle;

import java.util.*;

public class ConferenceRoomPresenter extends AbstractConferencePresenter{

    private IConferenceRoomView conferenceRoomView;
    private UserController userController;
    private RoomController roomController;
    protected UUID roomUUID;

    ConferenceRoomPresenter(IFrame mainFrame, IConferenceRoomView conferenceRoomView, UUID roomUUID){
        super(mainFrame, roomUUID);

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        roomController = controllerBundle.getRoomController();

        this.conferenceRoomView = conferenceRoomView;
        updateRoomData();
    }


    private void updateRoomData() {

        String[][] tableData = {
                //{"Room UUID", roomController.createRoom(conferenceUUID, signedInUserUUID, location, capacity)},
                {"Room UUID", roomUUID.toString()},
                {"Room Location", roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID)},
                {"Room Capacity", String.valueOf(roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID))},
        };

        String[] columnNames = {
                "Key",
                "Value"
        };

        conferenceRoomView.setTableData(tableData, columnNames);
    }


}
