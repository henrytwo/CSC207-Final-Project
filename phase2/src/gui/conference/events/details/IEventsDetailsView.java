package gui.conference.events.details;

public interface IEventsDetailsView {
    void setTableData(String[][] tableData, String[] columnNames);
    void setRegisterButtonText(String text);
    void enableEditEventButton(boolean state);
    void enableDeleteEventButton(boolean state);
}
