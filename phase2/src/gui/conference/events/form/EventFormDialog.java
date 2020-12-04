package gui.conference.events.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

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

    @Override
    public void setUpdated(boolean newUpdated) {
        updated = newUpdated;
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

//    @Override
//    public void setRoomArea(String roomInfo) {
//        roomArea.append(roomInfo);
//    }
//
//    @Override
//    public void setSpeakerArea(String speakerInfo) {
//        speakerArea.append(speakerInfo);
//    }

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
    public Object run() {
        this.pack();
        this.setVisible(true);

        // Only return the UUID if we actually performed an update
        return updated ? eventUUID : null;
    }

    @Override
    public void setEventUUID(UUID newUUID) {
        eventUUID = newUUID;
    }

    @Override
    public void close() {
        dispose();
    }
}