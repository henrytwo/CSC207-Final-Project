package gui.mainMenu.conference;

import gui.GUISystem;
import gui.util.Panelable;

import javax.swing.*;

public class ConferenceMenuView implements Panelable {
    private JPanel panel;
    private JList list1;
    private JTabbedPane tabbedPane1;
    private JButton openTestThingButton;
    private JButton button2;
    private JButton button3;
    private JButton backToMainMenuButton;
    private JCheckBox checkBox1;
    private JTextPane textPane1;

    private GUISystem guiSystem;
    private Panelable parent;

    private ConferenceMenuPresenter conferenceMenuPresenter;

    /**
     * @param guiSystem parent gui system
     * @param parent parent component so that we can go back a page if needed
     */
    public ConferenceMenuView(GUISystem guiSystem, Panelable parent) {
        this.guiSystem = guiSystem;
        this.parent = parent;

        this.conferenceMenuPresenter = new ConferenceMenuPresenter(guiSystem, this);

        backToMainMenuButton.setActionCommand("mainMenu");
        backToMainMenuButton.addActionListener(conferenceMenuPresenter);

        openTestThingButton.setActionCommand("testThing");
        openTestThingButton.addActionListener(conferenceMenuPresenter);
    }

    @Override
    public Panelable getParent() {
        return parent;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
