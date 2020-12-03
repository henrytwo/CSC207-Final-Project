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
import java.util.Map;
import java.util.UUID;

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

    public MessagingView(IFrame guiSystem, UUID defaultConversationUUID, Map<String, Object> initializationArguments) {

        messagingPresenter = new MessagingPresenter(guiSystem, this, defaultConversationUUID, initializationArguments);
        chatGroups.addListSelectionListener((e) -> messagingPresenter.updateSelection(chatGroups.getSelectedIndex()));
        createNewChat.addActionListener((e) -> messagingPresenter.createConversation());

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messagingPresenter.sendMessage();
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return messagingPanel;
    }

    @Override
    public void setConversationList(String[] conversationNames) {
        chatGroups.setListData(conversationNames);
    }

    @Override
    public void setConversationListSelection(int selectionIndex) {
        chatGroups.setSelectedIndex(selectionIndex);
    }


    @Override
    public String getMessagefromtextbox(){
        return messagetext.getText();
    }

    @Override
    public void setMessages(String[] updatedMessages){
        messages.setListData(updatedMessages);
    }
}
