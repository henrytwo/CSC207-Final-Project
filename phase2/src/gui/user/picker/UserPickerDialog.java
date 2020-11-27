package gui.user.picker;

import gui.util.dialogs.UUIDPickerDialog;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import java.util.Set;
import java.util.UUID;

/**
 * Dialog to pick a user UUID
 */
public class UserPickerDialog extends UUIDPickerDialog implements IDialog {
    public UserPickerDialog(IFrame mainFrame, Set<UUID> availableUserUUIDs, String instructions) {
        super(mainFrame, availableUserUUIDs, instructions, "Select user", (uuid) -> {
            ControllerBundle controllerBundle = mainFrame.getControllerBundle();
            UserController userController = controllerBundle.getUserController();

            return userController.getUserFullName(uuid);
        });
    }
}
