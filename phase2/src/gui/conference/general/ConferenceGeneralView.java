package gui.conference.general;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

/**
 * GeneralView for a conference
 */
public class ConferenceGeneralView implements IPanel, IConferenceGeneralView {
    private JPanel generalViewPanel;
    private JTable generalTable;
    private JButton leaveConferenceButton;

    private ConferenceGeneralPresenter conferenceGeneralPresenter;

    /**
     * @param mainFrame      main GUI frame
     * @param conferenceUUID UUID of the associated conference
     */
    public ConferenceGeneralView(IFrame mainFrame, UUID conferenceUUID) {
        conferenceGeneralPresenter = new ConferenceGeneralPresenter(mainFrame, this, conferenceUUID);

        leaveConferenceButton.addActionListener((e) -> conferenceGeneralPresenter.leaveConference());
    }

    /**
     * Sets the general table data
     *
     * @param tableData   2D array of table data
     * @param columnNames array of table data
     */
    @Override
    public void setTableData(String[][] tableData, String[] columnNames) {
        TableModel tableModel = new DefaultTableModel(tableData, columnNames);

        generalTable.setModel(tableModel);
    }

    /**
     * gets the general view panel
     *
     * @return the general view panel
     */
    @Override
    public JPanel getPanel() {
        return generalViewPanel;
    }
}