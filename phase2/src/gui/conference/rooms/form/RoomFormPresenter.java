package gui.conference.rooms.form;

import convention.exception.InvalidCapacityException;
import convention.exception.InvalidNameException;
import gui.conference.util.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.UUID;

class RoomFormPresenter extends AbstractConferencePresenter {
    private boolean isExistingRoom;
    private UUID roomUUID;

    private IRoomFormDialog roomFormDialog;

    private String roomLocation = "";
    private int roomCapacity;

    /**
     *
     * @param mainFrame main GUI frame
     * @param roomFormDialog dialog to manage
     * @param conferenceUUID UUID of associated conference
     * @param roomUUID UUID of associated room
     */
    RoomFormPresenter(IFrame mainFrame, IRoomFormDialog roomFormDialog, UUID conferenceUUID, UUID roomUUID) {
        super(mainFrame, conferenceUUID);

        this.roomFormDialog = roomFormDialog;
        this.roomUUID = roomUUID;

        isExistingRoom = roomUUID != null;

        if (isExistingRoom) {
            roomFormDialog.setDialogTitle(String.format("Editing Room (%s)", roomUUID));

            roomLocation = roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID);
            roomCapacity = roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID);

            roomFormDialog.setLocation(roomLocation);
            roomFormDialog.setCapacity(roomCapacity);
        } else {
            roomFormDialog.setDialogTitle("Create new room");
        }
    }

    /**
     * Submit form and:
     * 1) Apply edit changes if room is existing, or
     * 2) Create a new room otherwise
     */
    void submit() {

        try {
            roomLocation = roomFormDialog.getRoomLocation();
            roomCapacity = roomFormDialog.getCapacity();

            if (isExistingRoom) {
                roomController.setRoomLocation(conferenceUUID, signedInUserUUID, roomUUID, roomLocation);
                roomController.setRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID, roomCapacity);
            } else {
                roomUUID = roomController.createRoom(conferenceUUID, signedInUserUUID, roomLocation, roomCapacity);
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
