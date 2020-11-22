package gui.conference;

import gui.util.enums.PanelNames;
import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ConferenceMenuPresenter implements ActionListener {
    private IPanel conferenceMenuView;
    private IFrame mainFrame;

    public ConferenceMenuPresenter(IFrame mainFrame, IPanel conferenceMenuView) {
        this.conferenceMenuView = conferenceMenuView;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IPanelFactory frameFactory = new PanelFactory(mainFrame);

        switch (e.getActionCommand()) {
            case "mainMenu":
                mainFrame.setPanel(frameFactory.createPanel(PanelNames.names.MAIN_MENU));
                break;
            case "testThing":
                mainFrame.setPanel(frameFactory.createPanel(PanelNames.names.TEST, new HashMap<>() {
                    {
                        put("conference", "yes");
                    }
                }));
                break;
        }
    }
}
