package gui.mainMenu;

import gui.util.enums.PanelNames;
import gui.util.interfaces.IFrame;
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

        panelFactory = mainFrame.getPanelFactory();
        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        mainMenuView.setSignedInAs(String.format("Signed in as %s", userController.getUserFullName(userController.getCurrentUser())));
    }

    void logout() {
        userController.logout();
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.LOGIN));
    }

    void conferenceMenu() {

        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.CONFERENCES));
    }
}
