package gui.conference.rooms.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class RoomFormDialog extends JDialog implements IDialog, IRoomFormDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField location;
    private JSpinner capacity;
    private int fallbackValue;

    private UUID conferenceUUID;
    private UUID roomUUID;

    private boolean updated = false;

    /**
     * Creates room form dialog. If roomUUID is null, then form submission will result in a new
     * room being created. Otherwise, details about the existing room will be retrieved.
     *
     * @param mainFrame      main GUI frame
     * @param conferenceUUID UUID of the associated conference
     * @param roomUUID       UUID of the room to edit, if applicable
     */
    public RoomFormDialog(IFrame mainFrame, UUID conferenceUUID, UUID roomUUID) {

        super(mainFrame.getFrame());
        this.setLocationRelativeTo(mainFrame.getFrame());

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.conferenceUUID = conferenceUUID;
        RoomFormPresenter roomFormPresenter = new RoomFormPresenter(mainFrame, this, conferenceUUID, roomUUID);

        buttonOK.addActionListener((e) -> roomFormPresenter.submit());
        buttonCancel.addActionListener((e) -> close());


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });


        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction((e) -> close(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Runs the room form dialog
     *
     * @return
     */
    @Override
    public Object run() {
        this.pack();
        this.setVisible(true);
        return updated ? roomUUID : null;

    }

    /**
     * Sets the title of the dialog
     *
     * @param newTitle title to be set for dialog
     */
    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    /**
     * Sets the location of the room
     *
     * @param roomLocation the location of the room
     */
    @Override
    public void setLocation(String roomLocation) {
        location.setText(roomLocation);
    }

    /**
     * Sets the capacity of the room
     *
     * @param roomCapacity capacity of the room
     */
    @Override
    public void setCapacity(int roomCapacity) {
        capacity.setValue(roomCapacity);
    }

    /**
     * Close the dialog.
     */
    @Override
    public void close() {
        dispose();
    }

    /**
     * Gets the location of the room.
     *
     * @return location of room
     */
    @Override
    public String getRoomLocation() {
        return location.getText();
    }

    /**
     * Sets the status of room as updated, iff room has been updated.
     *
     * @param newUpdated true if updated, otherwise false
     */
    @Override
    public void setUpdated(boolean newUpdated) {
        updated = newUpdated;
    }

    /**
     * Sets the room UUID for the dialog
     *
     * @param newUUID UUID of the room
     */
    @Override
    public void setRoomUUID(UUID newUUID) {
        roomUUID = newUUID;
    }

    /**
     * Gets the capacity of the room
     *
     * @return integer value of room capacity
     */
    @Override
    public int getCapacity() {
        try {
            capacity.commitEdit();
        } catch (java.text.ParseException e) {
            // Edited value is invalid, spinner.getValue() will return
            // the last valid value, you could revert the spinner to show that:
            JComponent editor = capacity.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                ((JSpinner.DefaultEditor) editor).getTextField().setValue(capacity.getValue());
            }
            // reset the value to some known value:
            capacity.setValue(fallbackValue);
            // or treat the last valid value as the current, in which
            // case you don't need to do anything.
        }
        return (Integer) capacity.getValue();
    }
}
