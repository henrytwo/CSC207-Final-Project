package gui.conference.events.details;

public interface IEventsDetailsView {
    void setGeneralTableData(String[][] tableData, String[] columnNames);

    void setAttendeeTableData(String[][] tableData, String[] columnNames);

    void setSpeakerTableData(String[][] tableData, String[] columnNames);

    void setRegisterButtonText(String text);

    void enableEditEventButton(boolean state);

    void enableDeleteEventButton(boolean state);

    void enableMessageUserButton(boolean state);
}
