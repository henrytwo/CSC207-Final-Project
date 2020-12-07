package gui.conference.picker;

import convention.ConferenceController;
import gui.util.dialogs.UUIDPickerDialog;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import util.ControllerBundle;

import java.util.Set;
import java.util.UUID;

/**
 * Dialog to pick a conference. Returns the selected conference UUID.
 */
public class ConferencePickerDialog extends UUIDPickerDialog implements IDialog {
    public ConferencePickerDialog(IFrame mainFrame, Set<UUID> availableConferenceUUIDs, String instructions) {
        super(mainFrame, availableConferenceUUIDs, instructions, "Select conference", (uuid) -> {
            ControllerBundle controllerBundle = mainFrame.getControllerBundle();
            ConferenceController conferenceController = controllerBundle.getConferenceController();

            return String.format("%s %s", conferenceController.getConferenceName(uuid), conferenceController.getConferenceTimeRange(uuid));
        });
    }
}
