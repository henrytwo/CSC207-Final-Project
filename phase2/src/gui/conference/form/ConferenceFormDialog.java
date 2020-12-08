package gui.conference.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

/**
 * Form to edit to create a conference
 */
public class ConferenceFormDialog extends JDialog implements IDialog, IConferenceFormDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField start;
    private JTextField end;
    private JTextField name;

    private UUID conferenceUUID;

    private boolean updated = false;

    /**
     * Creates conference form dialog. If conferenceUUID is null, then form submission will result in a new
     * conference being created. Otherwise, details about the existing conference will be retrieved.
     *
     * @param mainFrame      main GUI frame
     * @param conferenceUUID UUID of conference to edit, if applicable
     */
    public ConferenceFormDialog(IFrame mainFrame, UUID conferenceUUID) {

        /* Setup formatting */

        // Set the parent frame so that this dialog is centered
        super(mainFrame.getFrame());
        this.setLocationRelativeTo(mainFrame.getFrame());

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        /* Initiate presenter */

        this.conferenceUUID = conferenceUUID;
        ConferenceFormPresenter conferenceFormPresenter = new ConferenceFormPresenter(mainFrame, this, conferenceUUID);

        /* Initiate listeners */

        buttonOK.addActionListener((e) -> conferenceFormPresenter.submit());
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
     * shows whether the form has been updated
     *
     * @param newUpdated the new value of update
     */
    @Override
    public void setUpdated(boolean newUpdated) {
        updated = newUpdated;
    }

    /**
     * sets the name
     *
     * @param newName the name
     */
    @Override
    public void setName(String newName) {
        name.setText(newName);
    }

    /**
     * starts the text
     *
     * @param newStart the start
     */
    @Override
    public void setStart(String newStart) {
        start.setText(newStart);
    }

    /**
     * ends the text
     *
     * @param newEnd the end
     */
    @Override
    public void setEnd(String newEnd) {
        end.setText(newEnd);
    }

    /**
     * sets the title of a dialogue
     *
     * @param newTitle the title
     */
    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    /**
     * gets the name
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name.getText();
    }

    /**
     * gets the start
     *
     * @return the start
     */
    @Override
    public String getStart() {
        return start.getText();
    }

    /**
     * get end
     *
     * @return the end
     */
    @Override
    public String getEnd() {
        return end.getText();
    }

    /**
     * sets the UUID of the conference
     *
     * @param newUUID the new UUID
     */
    @Override
    public void setConferenceUUID(UUID newUUID) {
        conferenceUUID = newUUID;
    }

    /**
     * closes and disposes of the dialogue
     */
    @Override
    public void close() {
        dispose();
    }

    /**
     * updates the dialogue
     *
     * @return the updated UUID of the conference
     */
    @Override
    public UUID run() {
        this.pack();
        this.setVisible(true);

        // Only return the UUID if we actually performed an update
        return updated ? conferenceUUID : null;
    }
}
