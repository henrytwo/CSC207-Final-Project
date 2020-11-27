package gui.conference.settings;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

public class ConferenceSettingsView implements IPanel, IConferenceSettingsView {

    private ConferenceSettingsPresenter conferenceSettingsPresenter;
    private JPanel settingsPanel;
    private JButton editConferenceButton;
    private JButton removeUserButton;
    private JButton removeOrganizerButton;
    private JButton addOrganizerButton;
    private JButton inviteUserButton;
    private JButton deleteConferenceButton;
    private JButton createConversationWithUsersButton;
    private JList conferenceMembersList;

    public ConferenceSettingsView(IFrame mainFrame, UUID conferenceUUID) {
        conferenceSettingsPresenter = new ConferenceSettingsPresenter(mainFrame, this, conferenceUUID);

        editConferenceButton.addActionListener((e) -> conferenceSettingsPresenter.editConference());
        deleteConferenceButton.addActionListener((e) -> conferenceSettingsPresenter.deleteConference());
    }

    @Override
    public JPanel getPanel() {
        return settingsPanel;
    }
}
