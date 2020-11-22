package gui.mainMenu.conference;

import gui.util.Panelable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConferenceMenuPresenter implements ActionListener {
    Panelable parent;

    public ConferenceMenuPresenter(Panelable parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action: " + e);


    }
}
