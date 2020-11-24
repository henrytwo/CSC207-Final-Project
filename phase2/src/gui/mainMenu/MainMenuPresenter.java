package gui.mainMenu;

import gui.util.enums.PanelNames;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

class MainMenuPresenter {
    private IMainMenuView mainMenuView;
    private IFrame mainFrame;
    private IPanelFactory panelFactory;

    private UserController userController;

    MainMenuPresenter(IFrame mainFrame, IMainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.mainFrame = mainFrame;

        panelFactory = mainFrame.getPanelFactory();

        // Initiate controllers
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        // Initiate main menu tabs
        IPanel conferenceMenuView = panelFactory.createPanel(PanelNames.names.CONFERENCES);
        mainMenuView.setConferenceMenuPanel(conferenceMenuView);

        IPanel messagingView = panelFactory.createPanel(PanelNames.names.MESSAGING);
        mainMenuView.setMessagingPanel(messagingView);

        IPanel contactsView = panelFactory.createPanel(PanelNames.names.CONTACTS);
        mainMenuView.setContactsPanel(contactsView);

        // Logout button text
        mainMenuView.setLogoutButtonText(String.format("Logout (Signed in as %s)", userController.getUserFullName(userController.getCurrentUser())));
    }

    void logout() {
        userController.logout();
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.LOGIN));
    }
}
