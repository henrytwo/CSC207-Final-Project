package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ConferenceMenuView implements IPanel {
    private JPanel panel;
    private JList list1;
    private JTabbedPane tabbedPane1;
    private JButton openTestThingButton;
    private JButton button2;
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

        backToMainMenuButton.setActionCommand("mainMenu");
        backToMainMenuButton.addActionListener(conferenceMenuPresenter);

        openTestThingButton.setActionCommand("testThing");
        openTestThingButton.addActionListener(conferenceMenuPresenter);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
