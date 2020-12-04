package gui.messaging;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

public class MessagingView implements IPanel, IMessagingView {
    private JPanel messagingPanel;
    private JButton newConversationButton;
    private JList chatGroups;
    private JTextField messagetext;
    private JList messages;
    private JButton sendButton;
    private MessagingPresenter messagingPresenter;

    public MessagingView(IFrame guiSystem, UUID defaultConversationUUID) {
        messagingPresenter = new MessagingPresenter(guiSystem, this, defaultConversationUUID);
        chatGroups.addListSelectionListener((e) -> messagingPresenter.updateSelection(chatGroups.getSelectedIndex()));
        newConversationButton.addActionListener((e) -> messagingPresenter.createConversation());

        messagingPanel.registerKeyboardAction((e) -> messagingPresenter.sendMessage(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        sendButton.addActionListener((e) -> messagingPresenter.sendMessage());
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
    public void setTextFieldToNull(){
        messagetext.setText("");
    }

    @Override
    public void setConversationListSelection(int selectionIndex) {
        chatGroups.setSelectedIndex(selectionIndex);
    }

    @Override
    public String getTextboxContent() {
        return messagetext.getText();
    }

    @Override
    public void setMessages(String[] updatedMessages) {
        messages.setListData(updatedMessages);
    }

    @Override
    public int getMessagesFromJList(){
        ListModel list = messages.getModel();
        return list.getSize();
    }

    @Override
    public void disableTextField(boolean instruction){
        messagetext.setEnabled(instruction);
    }

    @Override
    public void disableSendButton(boolean instruction){
        sendButton.setEnabled(instruction);
    }

    @Override
    public void scrollToLastMessage(){
    int lastMessageIndex = messages.getModel().getSize() - 1;
        if(lastMessageIndex >= 0){
            messages.ensureIndexIsVisible(lastMessageIndex);
        }
    }

}
