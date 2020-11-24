package gui.conference.general;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceGeneralView implements IPanel, IConferenceGeneralView {
    private JPanel generalViewPanel;
    private JLabel testText;

    private UUID conferenceUUID;
    private IPanel parentPanel;

    private ConferenceGeneralPresenter conferenceGeneralPresenter;

    public ConferenceGeneralView(IFrame mainFrame, IPanel parentPanel, UUID conferenceUUID) {
        this.conferenceUUID = conferenceUUID;
        this.parentPanel = parentPanel;

        conferenceGeneralPresenter = new ConferenceGeneralPresenter(mainFrame, this);

    }

    @Override
    public IPanel getParentPanel() {
        return parentPanel;
    }

    @Override
    public UUID getConferenceUUID() {
        return conferenceUUID;
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
