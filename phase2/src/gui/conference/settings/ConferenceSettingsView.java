package gui.conference.settings;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

public class ConferenceSettingsView implements IPanel, IConferenceSettingsView {

    private ConferenceSettingsPresenter conferenceSettingsPresenter;
    private JPanel settingsPanel;
    private JButton editConferenceButton;
    private JButton removeUserButton;
    private JButton removeOrganizerButton;
    private JButton addOrganizerButton;
    private JButton addAttendee;
    private JButton deleteConferenceButton;
    private JButton createConversationWithUsersButton;
    private JTable userTable;

    public ConferenceSettingsView(IFrame mainFrame, UUID conferenceUUID) {
        conferenceSettingsPresenter = new ConferenceSettingsPresenter(mainFrame, this, conferenceUUID);

        addOrganizerButton.addActionListener((e) -> conferenceSettingsPresenter.addOrganizer());
        addAttendee.addActionListener((e) -> conferenceSettingsPresenter.addAttendee());
        removeUserButton.addActionListener((e) -> conferenceSettingsPresenter.removeUser());
        removeOrganizerButton.addActionListener((e) -> conferenceSettingsPresenter.removeOrganizer());

        createConversationWithUsersButton.addActionListener((e) -> conferenceSettingsPresenter.createConversation());

        editConferenceButton.addActionListener((e) -> conferenceSettingsPresenter.editConference());
        deleteConferenceButton.addActionListener((e) -> conferenceSettingsPresenter.deleteConference());
    }

    @Override
    public void setUserList(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);

        userTable.setModel(tableModel);
    }

    @Override
    public JPanel getPanel() {
        return settingsPanel;
    }
}
