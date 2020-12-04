package gui.messaging;

public interface IMessagingView {
    void setConversationList(String[] conversationNames);
    void setConversationListSelection(int selectionIndex);
    String getTextboxContent();
    void setMessages(String[] messages);
    void setTextFieldToNull();
    int getMessagesFromJList();
    void disableTextField(boolean instruction);
    void disableSendButton(boolean instruction);
    void scrollToLastMessage();

}
