package gui.conference.events.form;

import java.util.UUID;

public interface IEventFormDialog {
    void setEventUUID(UUID newUUID);

    void close();

    void setName(String name);

    void setStart(String start);

    void setEnd(String end);

    void setUpdated(boolean updated);

    void setDialogTitle(String title);

    String getName();

    String getStart();

    String getEnd();
}
