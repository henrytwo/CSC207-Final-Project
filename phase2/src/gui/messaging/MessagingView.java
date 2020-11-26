package gui.messaging;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

public class MessagingView implements IPanel, IMessagingView {
    private JPanel messagingPanel;
    private JButton Send;
    private JTextField Message_content;

    public MessagingView(IFrame mainFrame) {

        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        messagingPanel.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                JOptionPane.showMessageDialog(null, "Message Sent Successfully");
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
    }

    @Override
    public JPanel getPanel() {
        return messagingPanel;
    }
}
