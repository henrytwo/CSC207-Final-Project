package gui.util.factories;

import gui.conference.ConferenceMenuView;
import gui.login.LoginView;
import gui.mainMenu.MainMenuView;
import gui.test.TestView;
import gui.util.enums.PanelNames;
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
    public IPanel createPanel(PanelNames.names name) {
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
    public IPanel createPanel(PanelNames.names name, Map<String, String> arguments) {
        switch (name) {
            case LOGIN:
                return new LoginView(mainFrame);
            case MAIN_MENU:
                return new MainMenuView(mainFrame);
            case TEST:
                return new TestView(mainFrame, arguments.get("conference"));
            case CONFERENCES:
                return new ConferenceMenuView(mainFrame);
            default:
                throw new NullPanelException(name);
        }
    }
}
