package gui.messaging.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

public class MessagingView implements IPanel, IMessagingView {
    private JPanel messagingPanel;
    private JButton newConversationButton;
    private JList conversationList;
    private JTextField messageText;
    private JList messages;
    private JButton sendButton;
    private JList userList;
    private JButton archiveButton;
    private JButton button2;
    private MessagingPresenter messagingPresenter;

    /**
     * Creates GUI for the Messaging Functionality
     *
     * @param mainFrame               the main GUI frame
     * @param defaultConversationUUID UUID of the default conversation to select. If none selected, or invalid, the first one will be selected.
     */
    public MessagingView(IFrame mainFrame, UUID defaultConversationUUID) {
        messagingPresenter = new MessagingPresenter(mainFrame, this, defaultConversationUUID);
        conversationList.addListSelectionListener((e) -> messagingPresenter.updateSelection(conversationList.getSelectedIndex()));
        newConversationButton.addActionListener((e) -> messagingPresenter.createConversation());

        messagingPanel.registerKeyboardAction((e) -> messagingPresenter.sendMessage(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        sendButton.addActionListener((e) -> messagingPresenter.sendMessage());

        archiveButton.addActionListener((e) -> messagingPresenter.archive());
    }

    @Override
    public JPanel getPanel() {
        return messagingPanel;
    }

    @Override
    public void setConversationList(String[] conversationNames) {
        conversationList.setListData(conversationNames);
    }

    @Override
    public void setTextFieldToNull() {
        messageText.setText("");
    }

    @Override
    public void setConversationListSelection(int selectionIndex) {
        conversationList.setSelectedIndex(selectionIndex);
    }

    @Override
    public String getTextboxContent() {
        return messageText.getText();
    }

    @Override
    public void setMessages(String[] updatedMessages) {
        messages.setListData(updatedMessages);
    }

    @Override
    public int getMessagesFromJList() {
        ListModel list = messages.getModel();
        return list.getSize();
    }

    @Override
    public void setEnableTextField(boolean instruction) {
        messageText.setEnabled(instruction);
    }

    @Override
    public void setEnableSendButton(boolean instruction) {
        sendButton.setEnabled(instruction);
    }

    @Override
    public void scrollToLastMessage() {
        int lastMessageIndex = messages.getModel().getSize() - 1;
        if (lastMessageIndex >= 0) {
            messages.ensureIndexIsVisible(lastMessageIndex);
        }
    }

    @Override
    public void setUsersList(String[] users) {
        userList.setListData(users);
    }

}
