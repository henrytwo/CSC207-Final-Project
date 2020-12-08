package gui.conference.form;

import java.util.UUID;

/**
 * interface for the conference dialog form of the GUI
 */
public interface IConferenceFormDialog {
    void setConferenceUUID(UUID newUUID);

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
