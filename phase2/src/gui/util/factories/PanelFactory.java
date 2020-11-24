package gui.util.factories;

import gui.conference.ConferenceMenuView;
import gui.login.LoginView;
import gui.mainMenu.MainMenuView;
import gui.util.enums.PanelNames;
import gui.util.exception.NullPanelException;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.util.Map;

/**
 * Creates IPanels for an IFrame given its name and some initializing arguments
 */
public class PanelFactory implements IPanelFactory {
    private IFrame mainFrame;

    /**
     * @param mainFrame main IFrame for the system
     */
    public PanelFactory(IFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Generate a panel given its name with no parameters
     *
     * @param name
     * @return
     */
    @Override
    public IPanel createPanel(PanelNames.names name) {
        return createPanel(name, null);
    }

    /**
     * Generates an IPanel given its name and (optional) initializing arguments
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
            case CONFERENCES:
                return new ConferenceMenuView(mainFrame);
            default:
                throw new NullPanelException(name);
        }
    }
}
