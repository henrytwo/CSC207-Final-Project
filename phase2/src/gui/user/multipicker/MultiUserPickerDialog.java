package gui.user.multipicker;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import java.util.UUID;

/**
 * Dialog to choose many user UUIDs
 */
public class MultiUserPickerDialog extends JDialog implements IDialog, IMultiUserPickerDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList userList;
    private JButton removeButton;
    private JButton addButton;
    private JLabel instructions;

    private Set<UUID> selectedUserUUIDs;

    public MultiUserPickerDialog(IFrame mainFrame, Set<UUID> availableUserUUIDs, String instructions) {
        /* Setup formatting */

        // Set the parent frame so that this dialog is centered
        super(mainFrame.getFrame());
        this.setLocationRelativeTo(mainFrame.getFrame());

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setInstructions(instructions);

        /* Initiate presenter */

        MultiUserPickerPresenter multiUserPickerPresenter = new MultiUserPickerPresenter(mainFrame, this, availableUserUUIDs);

        /* Initiate listeners */
        addButton.addActionListener((e) -> multiUserPickerPresenter.addUser());
        removeButton.addActionListener((e) -> multiUserPickerPresenter.removeUser());

        buttonOK.addActionListener((e) -> multiUserPickerPresenter.submit());
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
    public void setAddButtonDisabled(boolean state) {
        addButton.setEnabled(!state);
    }

    @Override
    public void setRemoveButtonDisabled(boolean state) {
        removeButton.setEnabled(!state);
    }

    @Override
    public void setInstructions(String newInstructions) {
        instructions.setText(newInstructions);
    }

    @Override
    public void setUserList(String[] newUserList) {
        userList.setListData(newUserList);
    }

    @Override
    public void setDialogTitle(String newTitle) {
        this.setTitle(newTitle);
    }

    @Override
    public void setSelectedUserUUIDs(Set<UUID> selectedUserUUIDs) {
        this.selectedUserUUIDs = selectedUserUUIDs;
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public Set<UUID> run() {
        this.pack();
        this.setVisible(true);

        return selectedUserUUIDs;
    }
}
