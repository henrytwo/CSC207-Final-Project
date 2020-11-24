package gui.conference;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;

public class ConferenceMenuPresenter {
    private IConferenceMenuView conferenceMenuView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;

    ConferenceMenuPresenter(IFrame mainFrame, IConferenceMenuView conferenceMenuView) {
        this.conferenceMenuView = conferenceMenuView;
        this.mainFrame = mainFrame;

        panelFactory = mainFrame.getPanelFactory();
    }

    void addStuffToList() {
        conferenceMenuView.setList(new String[]{"A", "B", "C"});
    }
}
