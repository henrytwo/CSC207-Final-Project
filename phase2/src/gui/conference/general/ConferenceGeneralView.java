package gui.conference.general;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceGeneralView implements IPanel, IConferenceGeneralView {
    private JPanel generalViewPanel;
    private JLabel testText;

    private UUID conferenceUUID;

    private ConferenceGeneralPresenter conferenceGeneralPresenter;

    public ConferenceGeneralView(IFrame mainFrame, UUID conferenceUUID) {
        this.conferenceUUID = conferenceUUID;

        conferenceGeneralPresenter = new ConferenceGeneralPresenter(mainFrame, this, conferenceUUID);
    }

    @Override
    public void setTestText(String text) {
        testText.setText(text);
    }

    @Override
    public JPanel getPanel() {
        return generalViewPanel;
    }
}
