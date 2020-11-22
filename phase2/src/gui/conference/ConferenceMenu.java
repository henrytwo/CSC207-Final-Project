package gui.conference;

import gui.GUISystem;
import gui.Panelable;

import javax.swing.*;

public class ConferenceMenu implements Panelable {
    private JPanel panel;
    private JList list1;
    private JTabbedPane tabbedPane1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton backToMainMenuButton;

    private GUISystem guiSystem;
    private Panelable parent;

    /**
     * @param guiSystem parent gui system
     * @param parent parent component so that we can go back a page if needed
     */
    public ConferenceMenu(GUISystem guiSystem, Panelable parent) {
        this.guiSystem = guiSystem;
        this.parent = parent;

        backToMainMenuButton.addActionListener((e) -> guiSystem.setPanel(parent));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
