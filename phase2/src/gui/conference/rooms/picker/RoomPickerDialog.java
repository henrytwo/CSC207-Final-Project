package gui.conference.rooms.picker;

import convention.RoomController;
import gui.util.dialogs.UUIDPickerDialog;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import java.util.Set;
import java.util.UUID;

public class RoomPickerDialog extends UUIDPickerDialog implements IDialog {
    public RoomPickerDialog(IFrame mainFrame, UUID conferenceUUID, Set<UUID> availableRoomUUIDs, String instructions) {
        super(mainFrame, availableRoomUUIDs, instructions, "Select room", (roomUUID) -> {
            ControllerBundle controllerBundle = mainFrame.getControllerBundle();
            RoomController roomController = controllerBundle.getRoomController();
            UserController userController = controllerBundle.getUserController();

            UUID signedInUserUUID = userController.getCurrentUser();

            return String.format("%s [Capacity: %s]", roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID), roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID));
        });
    }
}
