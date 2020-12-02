package gui.messaging;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

public class MessagingView implements IPanel, IMessagingView {
    private JPanel messagingPanel;
    private JButton createNewChat;
    private JList chatGroups;
    private JTextField messagetext;
    private JList messages;
    private JLabel Label1;
    private JButton sendButton;
    private JButton Send;
    private MessagingPresenter messagingPresenter;

    public MessagingView(IFrame mainFrame) {

//        Send.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Hello");
//
//            }
//        });
        messagingPanel.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                JOptionPane.showMessageDialog(null, "Message Sent Successfully");
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
        chatGroups.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
//                chatGroups.addListSelectionListener((e) -> MessagingPresenter.selectConferencePanel(MessageList.getSelectedIndex()));
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageContent = messagetext.getText();
                messagingPresenter.sendmessage(messageContent);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return messagingPanel;
    }
}
