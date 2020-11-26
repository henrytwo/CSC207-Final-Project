package gui.conference.general;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

public class ConferenceGeneralView implements IPanel, IConferenceGeneralView {
    private JPanel generalViewPanel;
    private JTable generalTable;
    private JButton leaveConferenceButton;

    private UUID conferenceUUID;

    private ConferenceGeneralPresenter conferenceGeneralPresenter;

    public ConferenceGeneralView(IFrame mainFrame, UUID conferenceUUID) {
        this.conferenceUUID = conferenceUUID;

        conferenceGeneralPresenter = new ConferenceGeneralPresenter(mainFrame, this, conferenceUUID);

        leaveConferenceButton.addActionListener((e) -> conferenceGeneralPresenter.leaveConference());
    }

    @Override
    public void setTableData(Object[][] tableData, Object[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);

        generalTable.setModel(tableModel);
    }

    @Override
    public JPanel getPanel() {
        return generalViewPanel;
    }
}
