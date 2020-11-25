package gui.mainMenu;

import gui.util.enums.Names;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.UUID;

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
        IPanel conferenceMenuView = panelFactory.createPanel(Names.panelNames.CONFERENCE_MENU);
        mainMenuView.setConferenceMenuPanel(conferenceMenuView);

        IPanel messagingView = panelFactory.createPanel(Names.panelNames.MESSAGING);
        mainMenuView.setMessagingPanel(messagingView);

        IPanel contactsView = panelFactory.createPanel(Names.panelNames.CONTACTS);
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
        mainFrame.setPanel(panelFactory.createPanel(Names.panelNames.LOGIN));
    }
}
