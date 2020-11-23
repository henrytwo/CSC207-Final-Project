package gui.conference;

import gui.util.enums.PanelNames;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.util.HashMap;

public class ConferenceMenuPresenter {
    private IPanel conferenceMenuView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;

    ConferenceMenuPresenter(IFrame mainFrame, IPanel conferenceMenuView) {
        this.conferenceMenuView = conferenceMenuView;
        this.mainFrame = mainFrame;

        panelFactory = mainFrame.getPanelFactory();
    }

    void mainMenu() {
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.MAIN_MENU));
    }

    void test() {
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.TEST, new HashMap<>() {
            {
                put("conference", "yes");
            }
        }));
    }
}
