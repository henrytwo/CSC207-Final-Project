package gui.mainMenu;

import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.Map;
import java.util.UUID;

class MainMenuPresenter {
    private IMainMenuView mainMenuView;
    private IFrame mainFrame;
    private IPanelFactory panelFactory;

    private UserController userController;

    private Map<String, Object> initializationArguments;

    /**
     * @param mainFrame
     * @param mainMenuView
     * @param initializationArguments hashmap of values that can be used to set the initial state of a panel
     */
    MainMenuPresenter(IFrame mainFrame, IMainMenuView mainMenuView, Map<String, Object> initializationArguments) {
        this.initializationArguments = initializationArguments;
        this.mainMenuView = mainMenuView;
        this.mainFrame = mainFrame;

        panelFactory = mainFrame.getPanelFactory();

        // Initiate controllers
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        // Initiate main menu tabs
        IPanel conferenceMenuView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_MENU, initializationArguments);
        mainMenuView.setConferenceMenuPanel(conferenceMenuView);

        IPanel messagingView = panelFactory.createPanel(PanelFactoryOptions.panelNames.MESSAGING, initializationArguments);
        mainMenuView.setMessagingPanel(messagingView);

        IPanel contactsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONTACTS, initializationArguments);
        mainMenuView.setContactsPanel(contactsView);

        UUID userUUID = userController.getCurrentUser();

        // Logout button text
        mainMenuView.setLogoutButtonText(String.format("Logout (Signed in as %s)", userController.getUserFullName(userUUID)));

        // God mode users get something special
        if (userController.getUserIsGod(userUUID)) {
            mainMenuView.setTopBarPanelText("God mode enabled");
        }
    }

    void logout() {
        userController.logout();
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.LOGIN));
    }
}
