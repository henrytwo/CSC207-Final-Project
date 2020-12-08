package gui.conference.events.details;

/**
 * interface for the details view of events UI
 */
public interface IEventsDetailsView {
    void setGeneralTableData(String[][] tableData, String[] columnNames);

    void setAttendeeTableData(String[][] tableData, String[] columnNames);

    void setSpeakerTableData(String[][] tableData, String[] columnNames);

    void setRegisterButtonText(String text);

    void enableRegisterButton(boolean state);

    void enableEventConversationButton(boolean state);

    void enableEditEventButton(boolean state);

    void enableDeleteEventButton(boolean state);

    void enableMessageUserButton(boolean state);
}
