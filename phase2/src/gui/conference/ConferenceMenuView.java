package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ConferenceMenuView implements IPanel, IConferenceMenuView {
    private JPanel panel;
    private JList list1;
    private JTabbedPane tabbedPane1;
    private JButton openTestThingButton;
    private JButton addStuffToList;
    private JButton button3;
    private JButton backToMainMenuButton;
    private JCheckBox checkBox1;
    private JTextPane textPane1;

    private IFrame guiSystem;

    private ConferenceMenuPresenter conferenceMenuPresenter;

    /**
     * @param guiSystem gui system
     */
    public ConferenceMenuView(IFrame guiSystem) {
        this.guiSystem = guiSystem;

        this.conferenceMenuPresenter = new ConferenceMenuPresenter(guiSystem, this);

        backToMainMenuButton.addActionListener((e) -> conferenceMenuPresenter.mainMenu());
        openTestThingButton.addActionListener((e) -> conferenceMenuPresenter.test());
        addStuffToList.addActionListener((e) -> conferenceMenuPresenter.addStuffToList());
    }

    @Override
    public void setList(String[] data) {
        list1.setListData(data);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
