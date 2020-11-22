package gui.mainMenu.conference;

import gui.util.Frameable;
import gui.util.Panelable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConferenceMenuPresenter implements ActionListener {
    Panelable conferenceMenuView;
    Frameable parentFrame;

    public ConferenceMenuPresenter(Frameable parentFrame, Panelable conferenceMenuView) {
        this.conferenceMenuView = conferenceMenuView;
        this.parentFrame = parentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "mainMenu":
                parentFrame.setPanel(conferenceMenuView.getParent());
                break;
            case "testThing":
                // ok this is kinda a bad practice, but need to figure out how they actually want this to be done...
                parentFrame.setPanel(new Test());
                break;
        }
    }
}
