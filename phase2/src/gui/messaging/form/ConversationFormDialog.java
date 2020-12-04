package gui.messaging.form;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class ConversationFormDialog extends JDialog implements IDialog, IConversationFormDialog {
    private JTextField chatName;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField messageContent;
    private JPanel contentPane;
    private JButton chooseUsersButton;

    private UUID conversationUUID;
    private boolean updated = false;


    /**
     *
     * Creates conversation form dialog. The form submission will result in a new conversation being created.
     * @param mainFrame the mainframe of GUI
     */
    public ConversationFormDialog(IFrame mainFrame) {

        /* Setup formatting */

        // Set the parent frame so that this dialog is centered
        super(mainFrame.getFrame());
        this.setLocationRelativeTo(mainFrame.getFrame());

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveButton);

        /* Initiate presenter */

        ConversationFormPresenter conversationFormPresenter = new ConversationFormPresenter(mainFrame, this);

        /* Initiate listeners */

        chooseUsersButton.addActionListener((e) -> conversationFormPresenter.selectUsers());
        saveButton.addActionListener((e) -> conversationFormPresenter.submit());
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
    public void setChatName(String newName) {
        chatName.setText(newName);
    }

    @Override
    public void setMessage(String newMessage) {
        messageContent.setText(newMessage);
    }

    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    @Override
    public String getChatName() {
        return chatName.getText();
    }

    @Override
    public String getMessage() {
        return messageContent.getText();
    }

    @Override
    public void setConversationUUID(UUID newUUID) {
        conversationUUID = newUUID;
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public UUID run() {
        this.pack();
        this.setVisible(true);

        // Only return the UUID if we actually performed an update
        return updated ? conversationUUID : null;
    }
}
