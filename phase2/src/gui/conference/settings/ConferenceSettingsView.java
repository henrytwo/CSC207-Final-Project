package gui.conference.settings;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceSettingsView implements IPanel, IConferenceSettingsView {

    private ConferenceSettingsPresenter conferenceSettingsPresenter;
    private JPanel settingsPanel;
    private JButton editConferenceButton;
    private JButton viewRemoveAttendeesButton;
    private JButton viewRemoveOrganizersButton;
    private JButton addOrganizerButton;
    private JButton addAttendeeButton;
    private JButton deleteConferenceButton;

    public ConferenceSettingsView(IFrame mainFrame, UUID conferenceUUID) {
        conferenceSettingsPresenter = new ConferenceSettingsPresenter(mainFrame, this, conferenceUUID);

        editConferenceButton.addActionListener((e) -> conferenceSettingsPresenter.editConference());
    }

    @Override
    public JPanel getPanel() {
        return settingsPanel;
    }
}
