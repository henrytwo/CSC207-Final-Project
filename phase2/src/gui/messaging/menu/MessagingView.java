package gui.messaging.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

/**
 * the messaging view
 */
public class MessagingView implements IPanel, IMessagingView {
    private JPanel messagingPanel;
    private JButton newConversationButton;
    private JList<String> conversationList;
    private JTextField messageText;
    private JList<String> messages;
    private JButton sendButton;
    private JList<String> userList;
    private JButton archiveButton;
    private JButton unreadButton;
    private JLabel conversationTitle;
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

        archiveButton.addActionListener((e) -> messagingPresenter.archiveConversation());
        unreadButton.addActionListener((e) -> messagingPresenter.unreadConversation());

        messages.addMouseListener(new MouseAdapter() {
            /**
             * checks if the mouse has clicked this place
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                messagingPresenter.deleteMessage(messages.getSelectedIndex());
            }
        });

    }

    /**
     * sets the title of a conversation
     *
     * @param title the title of the conversation
     */
    @Override
    public void setConversationTitle(String title) {
        conversationTitle.setText(title);
    }

    /**
     * gets a panel
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return messagingPanel;
    }

    /**
     * sets a list of conversations
     *
     * @param conversationNames list of the names of conversations
     */
    @Override
    public void setConversationList(String[] conversationNames) {
        conversationList.setListData(conversationNames);
    }

    /**
     * clears a text box
     */
    @Override
    public void clearTextBox() {
        messageText.setText("");
    }

    /**
     * selects a specific conversation
     *
     * @param selectionIndex the index of the conversation in question
     */
    @Override
    public void setConversationListSelection(int selectionIndex) {
        conversationList.setSelectedIndex(selectionIndex);
    }

    /**
     * gets the content of a text box
     *
     * @return the content of the text box
     */
    @Override
    public String getTextBoxContent() {
        return messageText.getText();
    }

    /**
     * sets a list of messages
     *
     * @param updatedMessages the messages
     */
    @Override
    public void setMessages(String[] updatedMessages) {
        messages.setListData(updatedMessages);
    }

    /**
     * gets the number of messages in a list
     *
     * @return the number of messages
     */
    @Override
    public int getNumMessages() {
        ListModel<String> list = messages.getModel();
        return list.getSize();
    }

    /**
     * enables/disables the archive button
     *
     * @param instruction the condition of whether the button is active or not
     */
    @Override
    public void setEnableArchiveButton(boolean instruction) {
        archiveButton.setEnabled(instruction);
    }

    /**
     * enables/disables the unread button
     *
     * @param instruction the condition of whether button is active or not
     */
    @Override
    public void setEnableUnreadButton(boolean instruction) {
        unreadButton.setEnabled(instruction);
    }

    /**
     * enables/disables a text field
     *
     * @param instruction the condition of whether a text field is active or not
     */
    @Override
    public void setEnableTextField(boolean instruction) {
        messageText.setEnabled(instruction);
    }

    /**
     * enables/disables the send button
     *
     * @param instruction the condition of whether the button is active or not
     */
    @Override
    public void setEnableSendButton(boolean instruction) {
        sendButton.setEnabled(instruction);
    }

    /**
     * facilitates the scrolling to the last message
     */
    @Override
    public void scrollToLastMessage() {
        int lastMessageIndex = messages.getModel().getSize() - 1;
        if (lastMessageIndex >= 0) {
            messages.ensureIndexIsVisible(lastMessageIndex);
        }
    }

    /**
     * sets the list of users
     *
     * @param users the list of users
     */
    @Override
    public void setUsersList(String[] users) {
        userList.setListData(users);
    }

}
