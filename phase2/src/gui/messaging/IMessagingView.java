package gui.messaging;

public interface IMessagingView {
    void setConversationList(String[] conversationNames);
    void setConversationListSelection(int selectionIndex);
    String getTextboxContent();
    void setMessages(String[] messages);
    void setTextFieldToNull();

}
