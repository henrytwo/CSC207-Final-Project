package gui.conference.rooms.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
     * @param mainFrame
     * @param conferenceUUID
     * @param roomUUID
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

    @Override
    public Object run() {
        this.pack();
        this.setVisible(true);
        return updated ? roomUUID : null;

    }

    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    @Override
    public void setLocation(String roomLocation) {
        location.setText(roomLocation);
    }

    @Override
    public void setCapacity(int roomCapacity) {
        capacity.setValue(roomCapacity);
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public String getRoomLocation() {
        return location.getText();
    }

    @Override
    public void setUpdated(boolean newUpdated) {
        updated = newUpdated;
    }

    @Override
    public void setRoomUUID(UUID newUUID) {
        roomUUID = newUUID;
    }

    @Override
    public int getCapacity(){
        try{
            capacity.commitEdit();
        } catch (java.text.ParseException e) {
            // Edited value is invalid, spinner.getValue() will return
            // the last valid value, you could revert the spinner to show that:
            JComponent editor = capacity.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                ((JSpinner.DefaultEditor)editor).getTextField().setValue(capacity.getValue());
            }
            // reset the value to some known value:
            capacity.setValue(fallbackValue);
            // or treat the last valid value as the current, in which
            // case you don't need to do anything.
        }
        return (Integer) capacity.getValue();
    }
}
