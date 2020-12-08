package gui.messaging.menu;

/**
 * an interface for for the GUI of the messaging service
 */
public interface IMessagingView {
    void setConversationTitle(String title);

    void setConversationList(String[] conversationNames);

    void setConversationListSelection(int selectionIndex);

    String getTextBoxContent();

    void setMessages(String[] messages);

    void clearTextBox();

    int getNumMessages();

    void setEnableTextField(boolean instruction);

    void setEnableSendButton(boolean instruction);

    void scrollToLastMessage();

    void setUsersList(String[] users);

    void setEnableArchiveButton(boolean instruction);

    void setEnableUnreadButton(boolean instruction);
}
