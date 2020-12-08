package gui.util.dialogs;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * Dialog that allows a user to select an item identified by UUID. The UUID can be parsed into a human-friendly
 * display name by a lambda function in the constructor.
 */
public class UUIDPickerDialog implements IDialog {

    private IFrame mainFrame;

    private List<UUID> availableUUIDs;
    private String instructions;
    private String title;

    private Function<UUID, String> displayNameParser;

    /**
     * @param mainFrame         main IFrame object
     * @param availableUUIDs    set of available UUIDs to put in the list
     * @param instructions      instructions for dialog
     * @param title             title of dialog
     * @param displayNameParser lambda function to convert a UUID into a human readable display name
     */
    public UUIDPickerDialog(IFrame mainFrame, Set<UUID> availableUUIDs, String instructions, String title, Function<UUID, String> displayNameParser) {
        this.mainFrame = mainFrame;
        this.availableUUIDs = new ArrayList<>(availableUUIDs);
        this.instructions = instructions;
        this.title = title;
        this.displayNameParser = displayNameParser;
    }

    /**
     * Displays the dialog
     *
     * @return UUID of the item selected
     */
    @Override
    public UUID run() {
        String[] displayNames = new String[availableUUIDs.size()];

        for (int i = 0; i < availableUUIDs.size(); i++) {
            displayNames[i] = String.format("%4d. %s", i, displayNameParser.apply(availableUUIDs.get(i)));
        }

        String selectedValue = (String) JOptionPane.showInputDialog(
                mainFrame.getFrame(),
                instructions,
                title,
                JOptionPane.QUESTION_MESSAGE,
                null,
                displayNames,
                "");

        // Find the UUID corresponding to the selected value
        for (int i = 0; i < availableUUIDs.size(); i++) {
            if (displayNames[i].equals(selectedValue)) {
                return availableUUIDs.get(i);
            }
        }

        return null;

    }
}
