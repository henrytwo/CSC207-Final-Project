package gui.conference.events.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

/**
 * Form to edit and create events.
 */
public class EventFormDialog extends JDialog implements IDialog, IEventFormDialog {
    private JPanel contentPane;
    private JTextField name;
    private JTextField start;
    private JTextField end;
    private JButton cancelButton;
    private JButton buttonOk;
    private JButton speakerButton;
    private JButton roomButton;

    private UUID eventUUID;
    private UUID conferenceUUID;

    private boolean updated = false;

    /**
     * Constructor for EventForm object, a form that allows users to enter details of an event.
     *
     * @param mainFrame      current frame
     * @param conferenceUUID UUID of the current conference.
     * @param eventUUID      UUID of the current event.
     */
    public EventFormDialog(IFrame mainFrame, UUID conferenceUUID, UUID eventUUID) {
        super(mainFrame.getFrame());
        this.setLocationRelativeTo(mainFrame.getFrame());

        this.eventUUID = eventUUID;
        this.conferenceUUID = conferenceUUID;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOk);

        EventFormPresenter eventFormPresenter = new EventFormPresenter(mainFrame, this, conferenceUUID, eventUUID);

        // Adding listeners
        roomButton.addActionListener((e) -> eventFormPresenter.selectRoom());
        speakerButton.addActionListener((e) -> eventFormPresenter.selectSpeakers());
        buttonOk.addActionListener((e) -> eventFormPresenter.submit());
        cancelButton.addActionListener((e) -> close());

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
     * Keeps track of if the form was updated.
     *
     * @param newUpdated new value of updated
     */
    @Override
    public void setUpdated(boolean newUpdated) {
        updated = newUpdated;
    }


    /**
     * Fills the name in the name text field.
     *
     * @param newName the name of the event
     */
    @Override
    public void setName(String newName) {
        name.setText(newName);
    }

    /**
     * Fills the start text field with start time of event.
     *
     * @param newStart the start time of the event
     */
    @Override
    public void setStart(String newStart) {
        start.setText(newStart);
    }

    /**
     * Fills the start text field with start time of event.
     *
     * @param newEnd the end time of the event
     */
    @Override
    public void setEnd(String newEnd) {
        end.setText(newEnd);
    }

    /**
     * Sets the tile of the form.
     *
     * @param newTitle the title to be put in
     */
    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    /**
     * Returns the name of teh event typed in the text field.
     *
     * @return name of the event
     */
    @Override
    public String getName() {
        return name.getText();
    }

    /**
     * Returns the start time from the start text field
     *
     * @return the start time of the event
     */
    @Override
    public String getStart() {
        return start.getText();
    }

    /**
     * Returns the end time from the start text field
     *
     * @return the end time of the event
     */
    @Override
    public String getEnd() {
        return end.getText();
    }

    /**
     * Runs this form dialog.
     *
     * @return UUID of the event that was created or updated
     */
    @Override
    public Object run() {
        this.pack();
        this.setVisible(true);

        // Only return the UUID if we actually performed an update
        return updated ? eventUUID : null;
    }

    /**
     * Sets the UUID of the event to the new UUID
     *
     * @param newUUID new UUID of event
     */
    @Override
    public void setEventUUID(UUID newUUID) {
        eventUUID = newUUID;
    }

    /**
     * Closes the form dialog.
     */
    @Override
    public void close() {
        dispose();
    }
}