package gui.messaging.menu;

public interface IMessagingView {
    void setConversationList(String[] conversationNames);

    void setConversationListSelection(int selectionIndex);

    String getTextboxContent();

    void setMessages(String[] messages);

    void setTextFieldToNull();

    int getMessagesFromJList();

    void setEnableTextField(boolean instruction);

    void setEnableSendButton(boolean instruction);

    void scrollToLastMessage();

    void setUsersList(String[] users);

}
