package gui.messaging;

public interface IMessagingView {
    void setConversationList(String[] conversationNames);
    void setConversationListSelection(int selectionIndex);
    String getMessagefromtextbox();
    void setMessages(String[] messages);

}
