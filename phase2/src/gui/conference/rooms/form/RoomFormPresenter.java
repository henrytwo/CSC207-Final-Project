package gui.conference.rooms.form;

import convention.RoomController;
import convention.exception.InvalidCapacityException;
import convention.exception.InvalidNameException;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.UUID;

class RoomFormPresenter {

    private RoomController roomController;
    private boolean isExistingRoom;
    private UUID conferenceUUID;
    private UUID roomUUID;
    private UUID userUUID;

    private IDialogFactory dialogFactory;

    private IRoomFormDialog roomFormDialog;

    private String roomLocation = "";
    private int roomCapacity;

    RoomFormPresenter(IFrame mainFrame, IRoomFormDialog roomFormDialog, UUID conferenceUUID, UUID roomUUID) {
        this.roomFormDialog = roomFormDialog;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        roomController = controllerBundle.getRoomController();

        dialogFactory = mainFrame.getDialogFactory();

        this.conferenceUUID = conferenceUUID;
        this.userUUID = controllerBundle.getUserController().getCurrentUser();

        isExistingRoom = roomUUID != null;

        if (isExistingRoom) {
            roomFormDialog.setDialogTitle(String.format("Editing Room (%s)", roomUUID));

            roomLocation = roomController.getRoomLocation(conferenceUUID, userUUID, roomUUID);
            roomCapacity = roomController.getRoomCapacity(conferenceUUID, userUUID, roomUUID);

            roomFormDialog.setLocation(roomLocation);
            roomFormDialog.setCapacity(roomCapacity);

        } else {
            roomFormDialog.setDialogTitle("Create new room");
        }
    }

    void submit() {

        try {
            roomLocation = roomFormDialog.getRoomLocation();
            roomCapacity = roomFormDialog.getCapacity();

            if (isExistingRoom) {
                roomController.setRoomLocation(conferenceUUID, userUUID, roomUUID, roomLocation);
                roomController.setRoomCapacity(conferenceUUID, userUUID, roomUUID, roomCapacity);
            } else {
                roomUUID = roomController.createRoom(conferenceUUID, userUUID, roomLocation, roomCapacity);
            }

            roomFormDialog.setRoomUUID(roomUUID);
            roomFormDialog.setUpdated(true);
            roomFormDialog.close();
        } catch (InvalidCapacityException e) {

            IDialog invalidCapacityDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid room capacity. Capacity should be greater than 0.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidCapacityDialog.run();

        } catch (InvalidNameException e) {

            IDialog invalidRoomLocationDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid location. Please follow the given format.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidRoomLocationDialog.run();

        }
    }
}
