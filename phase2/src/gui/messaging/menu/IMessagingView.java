package gui.messaging.menu;

public interface IMessagingView {
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

}
