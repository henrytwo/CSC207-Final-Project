package gui.conference.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class ConferenceFormDialog extends JDialog implements IDialog, IConferenceFormDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField start;
    private JTextField end;
    private JTextField name;

    private UUID conferenceUUID;

    /**
     * Creates conference form dialog. If conferenceUUID is null, then form submission will result in a new
     * conference being created. Otherwise, details about the existing conference will be retrieved.
     *
     * @param mainFrame
     * @param conferenceUUID
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
        ConferenceFormPresenter conferenceFormPresenter = new ConferenceFormPresenter(mainFrame, this);

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

    @Override
    public void setName(String newName) {
        name.setText(newName);
    }

    @Override
    public void setStart(String newStart) {
        start.setText(newStart);
    }

    @Override
    public void setEnd(String newEnd) {
        end.setText(newEnd);
    }

    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    @Override
    public String getName() {
        return name.getText();
    }

    @Override
    public String getStart() {
        return start.getText();
    }

    @Override
    public String getEnd() {
        return end.getText();
    }

    @Override
    public UUID getConferenceUUID() {
        return conferenceUUID;
    }

    @Override
    public void setConferenceUUID(UUID newUUID) {
        conferenceUUID = newUUID;
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public UUID run() {
        this.pack();
        this.setVisible(true);

        return conferenceUUID;
    }
}
