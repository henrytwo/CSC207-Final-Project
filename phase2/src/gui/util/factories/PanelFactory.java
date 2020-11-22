package gui.util.factories;

import gui.login.LoginView;
import gui.mainMenu.MainMenuView;
import gui.conference.ConferenceMenuView;
import gui.test.TestView;
import gui.util.exception.NullPanelException;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.util.Map;

public class PanelFactory implements IPanelFactory {
    IFrame mainFrame;

    /**
     * @param mainFrame main IFrame for the system
     */
    public PanelFactory(IFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public IPanel createPanel(String name) {
        return createPanel(name, null);
    }

    /**
     * Generates an IPanel given its name
     *
     * @param name
     * @param arguments
     * @return
     */
    @Override
    public IPanel createPanel(String name, Map<String, String> arguments) {
        switch (name) {
            case "login":
                return new LoginView(mainFrame);
            case "mainMenu":
                return new MainMenuView(mainFrame);
            case "test":
                return new TestView(mainFrame, arguments.get("conference"));
            case "conferenceMenu":
                return new ConferenceMenuView(mainFrame);
            default:
                throw new NullPanelException(name);
        }
    }
}
