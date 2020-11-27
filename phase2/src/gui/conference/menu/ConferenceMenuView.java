package gui.conference.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ConferenceMenuView implements IPanel, IConferenceMenuView {
    private JPanel panel;
    private JButton createConferenceButton;
    private JList conferenceList;
    private JSplitPane conferenceSplitPane;
    private JButton joinConferenceButton;

    private ConferenceMenuPresenter conferenceMenuPresenter;

    /**
     * @param guiSystem gui system
     * @param defaultConferenceUUID UUID of the default conference to select. If none selected, or invalid, the first one will be selected.
     */
    public ConferenceMenuView(IFrame guiSystem, UUID defaultConferenceUUID) {
        conferenceMenuPresenter = new ConferenceMenuPresenter(guiSystem, this, defaultConferenceUUID);

        conferenceList.addListSelectionListener((e) -> conferenceMenuPresenter.selectConferencePanel(conferenceList.getSelectedIndex()));
        createConferenceButton.addActionListener((e) -> conferenceMenuPresenter.createConference());
        joinConferenceButton.addActionListener((e) -> conferenceMenuPresenter.joinConference());
    }

    @Override
    public void setConferenceTabs(IPanel tabsPanel) {
        conferenceSplitPane.setRightComponent(tabsPanel.getPanel());
    }

    @Override
    public void setConferenceListSelection(int selectionIndex) {
        conferenceList.setSelectedIndex(selectionIndex);
    }

    @Override
    public void setConferenceList(String[] conferenceNames) {
        conferenceList.setListData(conferenceNames);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
