package gui.util.factories;

import gui.conference.tabs.ConferenceTabsView;
import gui.conference.menu.ConferenceMenuView;
import gui.conference.general.ConferenceGeneralView;
import gui.contacts.ContactsView;
import gui.login.LoginView;
import gui.mainMenu.MainMenuView;
import gui.messaging.MessagingView;
import gui.util.enums.PanelFactoryOptions;
import gui.util.exception.NullPanelException;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.util.UUID;
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
    public IPanel createPanel(PanelFactoryOptions.panelNames name) {
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
    public IPanel createPanel(PanelFactoryOptions.panelNames name, Map<String, Object> arguments) {
        switch (name) {
            case LOGIN:
                return new LoginView(mainFrame);
            case MAIN_MENU:
                return new MainMenuView(mainFrame);
            case CONFERENCE_MENU:
                return new ConferenceMenuView(mainFrame);
            case CONFERENCE_TABS:
                return new ConferenceTabsView(mainFrame, (UUID) arguments.get("conferenceUUID"));
            case CONFERENCE_GENERAL:
                return new ConferenceGeneralView(mainFrame, (UUID) arguments.get("conferenceUUID"));
            case CONTACTS:
                return new ContactsView(mainFrame);
            case MESSAGING:
                return new MessagingView(mainFrame);
            default:
                throw new NullPanelException(name);
        }
    }
}
