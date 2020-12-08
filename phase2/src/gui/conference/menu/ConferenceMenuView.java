package gui.conference.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

/**
 * Main conference selection menu
 */
public class ConferenceMenuView implements IPanel, IConferenceMenuView {
    private JPanel panel;
    private JButton createConferenceButton;
    private JList conferenceList;
    private JSplitPane conferenceSplitPane;
    private JButton joinConferenceButton;

    private ConferenceMenuPresenter conferenceMenuPresenter;

    /**
     * @param mainFrame               main GUI frame
     * @param defaultConferenceUUID   UUID of the default conference to select. If none selected, or invalid, the first one will be selected.
     * @param initializationArguments HashMap of values that can be used to set the initial state of a panel
     */
    public ConferenceMenuView(IFrame mainFrame, UUID defaultConferenceUUID, Map<String, Object> initializationArguments) {
        conferenceMenuPresenter = new ConferenceMenuPresenter(mainFrame, this, defaultConferenceUUID, initializationArguments);

        conferenceList.addListSelectionListener((e) -> conferenceMenuPresenter.selectConferencePanel(conferenceList.getSelectedIndex()));
        createConferenceButton.addActionListener((e) -> conferenceMenuPresenter.createConference());
        joinConferenceButton.addActionListener((e) -> conferenceMenuPresenter.joinConference());
    }

    /**
     * sets the tabs of the conference
     *
     * @param tabsPanel the tabs
     */
    @Override
    public void setConferenceTabs(IPanel tabsPanel) {
        conferenceSplitPane.setRightComponent(tabsPanel.getPanel());
    }

    /**
     * sets the selection from the conference list
     *
     * @param selectionIndex the index of the selection
     */
    @Override
    public void setConferenceListSelection(int selectionIndex) {
        conferenceList.setSelectedIndex(selectionIndex);
    }

    /**
     * sets the list of conferences
     *
     * @param conferenceNames the list of names
     */
    @Override
    public void setConferenceList(String[] conferenceNames) {
        conferenceList.setListData(conferenceNames);
    }

    /**
     * gets a panel
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

}
