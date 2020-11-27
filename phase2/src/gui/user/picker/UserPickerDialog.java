package gui.user.picker;

import convention.ConferenceController;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserPickerDialog implements IDialog {

    private IFrame mainFrame;

    private List<UUID> availableUserUUIDs;
    private String instructions;

    private UserController userController;

    public UserPickerDialog(IFrame mainFrame, Set<UUID> availableUserUUIDs, String instructions) {
        this.mainFrame = mainFrame;
        this.availableUserUUIDs = new ArrayList<>(availableUserUUIDs);
        this.instructions = instructions;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
    }

    @Override
    public UUID run() {
        String[] userFullNames = new String[availableUserUUIDs.size()];

        for (int i = 0; i < availableUserUUIDs.size(); i++) {
            UUID userUUID = availableUserUUIDs.get(i);

            userFullNames[i] = String.format("%4d. %s", i, userController.getUserFullName(userUUID));
        }

        String selectedValue = (String) JOptionPane.showInputDialog(
                mainFrame.getFrame(),
                instructions,
                "Select user",
                JOptionPane.QUESTION_MESSAGE,
                null,
                userFullNames,
                "");

        // Find the UUID corresponding to the selected value
        for (int i = 0; i < availableUserUUIDs.size(); i++) {
            if (userFullNames[i].equals(selectedValue)) {
                return availableUserUUIDs.get(i);
            }
        }

        return null;

    }
}
