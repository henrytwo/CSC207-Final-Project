package gui.conference.events.rightPane;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

public class EventsGeneralView implements IEventsGeneralView, IPanel {
    private JPanel eventsGeneralPanel;
    private JButton registerButton;
    private JButton deleteEventsButton;
    private JButton editEventsButton;
    private JTable generalEventsTable;

    private EventsGeneralPresenter eventsGeneralPresenter;

    public EventsGeneralView(IFrame mainFrame, UUID eventUUID, UUID conferenceUUID){
        eventsGeneralPresenter = new EventsGeneralPresenter(mainFrame, this, eventUUID, conferenceUUID);

        registerButton.addActionListener((e)-> eventsGeneralPresenter.registerForEvent());
        deleteEventsButton.addActionListener((e)-> eventsGeneralPresenter.deleteEvent());
        editEventsButton.addActionListener((e)-> eventsGeneralPresenter.editEvent());
    }

    @Override
    public void setTableData(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);
        generalEventsTable.setModel(tableModel);
    }

    @Override
    public JPanel getPanel() {
        return eventsGeneralPanel;
    }
}
