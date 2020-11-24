package gui.mainMenu;

import gui.util.enums.PanelNames;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

public class MainMenuPresenter {
    IMainMenuView mainMenuView;
    IFrame mainFrame;
    IPanelFactory panelFactory;

    ControllerBundle controllerBundle;
    UserController userController;

    MainMenuPresenter(IFrame mainFrame, IMainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.mainFrame = mainFrame;

        // Initiate controllers
        panelFactory = mainFrame.getPanelFactory();
        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        // Initiate main menu tabs
        /**
         * TODO: Instantiate the proper panel for each tab
         */
        IPanel conferenceMenuView = panelFactory.createPanel(PanelNames.names.CONFERENCES);
        mainMenuView.setConferenceMenuPanel(conferenceMenuView);

        IPanel messagingView = panelFactory.createPanel(PanelNames.names.CONFERENCES);
        mainMenuView.setMessagingPanel(messagingView);

        IPanel contactsView = panelFactory.createPanel(PanelNames.names.CONFERENCES);
        mainMenuView.setContactsPanel(contactsView);

        // Logout button text
        mainMenuView.setLogoutButtonText(String.format("Logout (Signed in as %s)", userController.getUserFullName(userController.getCurrentUser())));
    }

    void logout() {
        userController.logout();
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.LOGIN));
    }

    void conferenceMenu() {

        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.CONFERENCES));
    }
}
