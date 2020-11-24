package gui.conference.general;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceGeneralView implements IPanel, IConferenceGeneralView {
    private JPanel generalViewPanel;

    public ConferenceGeneralView(IFrame mainFrame, IPanel parentPanel, UUID conferenceUUID) {

    }

    @Override
    public JPanel getPanel() {
        return generalViewPanel;
    }
}
