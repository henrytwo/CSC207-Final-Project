package gui.conference.picker;

import convention.ConferenceController;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import util.ControllerBundle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConferencePickerView implements IDialog, IConferencePickerView {

    /**
     * TODO: figure out if this works under MVP
     */

    private IFrame mainFrame;

    private List<UUID> availableConferenceUUIDs;
    private String instructions;

    private ConferenceController conferenceController;

    public ConferencePickerView(IFrame mainFrame, Set<UUID> availableConferenceUUIDs, String instructions) {
        this.mainFrame = mainFrame;
        this.availableConferenceUUIDs = new ArrayList<>(availableConferenceUUIDs);
        this.instructions = instructions;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conferenceController = controllerBundle.getConferenceController();
    }

    public UUID show() {
        String[] conferenceNames = new String[availableConferenceUUIDs.size()];

        for (int i = 0; i < availableConferenceUUIDs.size(); i++) {
            UUID conferenceUUID = availableConferenceUUIDs.get(i);

            conferenceNames[i] = String.format("%4d. %s", i, conferenceController.getConferenceName(conferenceUUID));
        }

        String selectedValue = (String) JOptionPane.showInputDialog(
                mainFrame.getFrame(),
                instructions,
                "Select conference",
                JOptionPane.QUESTION_MESSAGE,
                null,
                conferenceNames,
                "");

        // Find the UUID corresponding to the selected value
        for (int i = 0; i < availableConferenceUUIDs.size(); i++) {
            if (conferenceNames[i].equals(selectedValue)) {
                return availableConferenceUUIDs.get(i);
            }
        }

        return null;

    }
}
