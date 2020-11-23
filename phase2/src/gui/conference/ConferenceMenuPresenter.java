package gui.conference;

import gui.util.enums.PanelNames;
import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.util.HashMap;

public class ConferenceMenuPresenter {
    private IPanel conferenceMenuView;
    private IFrame mainFrame;

    private IPanelFactory frameFactory;

    ConferenceMenuPresenter(IFrame mainFrame, IPanel conferenceMenuView) {
        this.conferenceMenuView = conferenceMenuView;
        this.mainFrame = mainFrame;

        frameFactory = new PanelFactory(mainFrame);
    }

    void mainMenu() {
        mainFrame.setPanel(frameFactory.createPanel(PanelNames.names.MAIN_MENU));
    }

    void test() {
        mainFrame.setPanel(frameFactory.createPanel(PanelNames.names.TEST, new HashMap<>() {
            {
                put("conference", "yes");
            }
        }));
    }
}
