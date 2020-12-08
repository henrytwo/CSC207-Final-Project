package gui.conference.events.details;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Map;
import java.util.UUID;

/**
 * Detailed view for events.
 */
public class EventsDetailsView implements IEventsDetailsView, IPanel {
    private JPanel eventsGeneralPanel;
    private JButton registerButton;
    private JButton deleteEventsButton;
    private JButton editEventsButton;
    private JTable generalEventsTable;
    private JTable attendeeTable;
    private JTable speakerTable;
    private JButton eventConversationButton;
    private JButton messageUserButton;

    private EventsDetailsPresenter eventsGeneralPresenter;

    /**
     * Constructor for this view.
     *
     * @param mainFrame               main gui frame
     * @param eventUUID               UUID of the event for which details are being shown.
     * @param conferenceUUID          UUID of the associated conference
     * @param initializationArguments HshMAp to initialize the state of this view
     */
    public EventsDetailsView(IFrame mainFrame, UUID eventUUID, UUID conferenceUUID, Map<String, Object> initializationArguments) {
        eventsGeneralPresenter = new EventsDetailsPresenter(mainFrame, this, eventUUID, conferenceUUID, initializationArguments);

        registerButton.addActionListener((e) -> eventsGeneralPresenter.toggleRegistration());
        deleteEventsButton.addActionListener((e) -> eventsGeneralPresenter.deleteEvent());
        editEventsButton.addActionListener((e) -> eventsGeneralPresenter.editEvent());
        messageUserButton.addActionListener((e) -> eventsGeneralPresenter.messageUser());
        eventConversationButton.addActionListener((e) -> eventsGeneralPresenter.eventConversation());
    }

    /**
     * Sets the state of the register button to be enabled or not.
     *
     * @param state boolean to decide if state is enabled
     */
    @Override
    public void enableRegisterButton(boolean state) {
        registerButton.setEnabled(state);
    }

    /**
     * Sets the state of the event conversation button to be enabled or not.
     *
     * @param state boolean to decide if state is enabled
     */
    @Override
    public void enableEventConversationButton(boolean state) {
        eventConversationButton.setEnabled(state);
    }

    /**
     * Sets the state of the message user button to be enabled or not.
     *
     * @param state boolean to decide if state is enabled
     */
    @Override
    public void enableMessageUserButton(boolean state) {
        messageUserButton.setEnabled(state);
    }

    /**
     * Sets the state of the edit event button to be enabled or not.
     *
     * @param state boolean to decide if state is enabled
     */
    @Override
    public void enableEditEventButton(boolean state) {
        editEventsButton.setEnabled(state);
    }

    /**
     * Sets the state of the delete event button to be enabled or not.
     *
     * @param state boolean to decide if state is enabled
     */
    @Override
    public void enableDeleteEventButton(boolean state) {
        deleteEventsButton.setEnabled(state);
    }

    /**
     * Sets the text on register button (register/unregister)
     *
     * @param text text to be set
     */
    @Override
    public void setRegisterButtonText(String text) {
        registerButton.setText(text);
    }

    /**
     * Sets the state of the general information table for a event.
     *
     * @param tableData   data to be filled into the table
     * @param columnNames name of table's columns
     */
    @Override
    public void setGeneralTableData(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);
        generalEventsTable.setModel(tableModel);
    }

    /**
     * Sets the state of the attendee information table for a event.
     *
     * @param tableData   data to be filled into the table
     * @param columnNames name of table's columns
     */
    @Override
    public void setAttendeeTableData(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);
        attendeeTable.setModel(tableModel);
    }

    /**
     * Sets the state of the speaker information table for a event.
     *
     * @param tableData   data to be filled into the table
     * @param columnNames name of table's columns
     */
    @Override
    public void setSpeakerTableData(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);
        speakerTable.setModel(tableModel);
    }

    /**
     * Returns the the detailed view object
     *
     * @return this view panel
     */
    @Override
    public JPanel getPanel() {
        return eventsGeneralPanel;
    }
}
