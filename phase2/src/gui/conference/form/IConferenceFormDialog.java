package gui.conference.form;

import java.util.UUID;

public interface IConferenceFormDialog {
    UUID getConferenceUUID();
    void setConferenceUUID(UUID newUUID);
    void close();
    void setName(String name);
    void setStart(String start);
    void setEnd(String end);
    void setDialogTitle(String title);
    String getName();
    String getStart();
    String getEnd();
}
