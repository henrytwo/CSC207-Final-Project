package gui.messaging.form;

import java.util.UUID;

public interface IConversationFormDialog {
    void setConversationUUID(UUID newUUID);
    void close();
    void setChatName(String name);
    void setMessage(String end);
    void setUpdated(boolean updated);
    void setDialogTitle(String title);
    String getChatName();
    String getMessage();
}

