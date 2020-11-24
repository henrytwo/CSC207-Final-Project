package gui.messaging;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class MessagingView implements IPanel, IMessagingView {
    private JPanel messagingPanel;

    public MessagingView(IFrame mainFrame) {

    }

    @Override
    public JPanel getPanel() {
        return messagingPanel;
    }
}
