package gui.mainMenu;

import gui.util.enums.PanelNames;
import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

public class MainMenuPresenter {
    IMainMenuView mainMenuView;
    IFrame mainFrame;

    ControllerBundle controllerBundle;
    UserController userController;

    IPanelFactory frameFactory;

    MainMenuPresenter(IFrame mainFrame, IMainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.mainFrame = mainFrame;

        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        frameFactory = new PanelFactory(mainFrame);

        mainMenuView.setSignedInAs(String.format("Signed in as %s", userController.getUserFullName(userController.getCurrentUser())));
    }

    void logout() {
        userController.logout();
        mainFrame.setPanel(frameFactory.createPanel(PanelNames.names.LOGIN));
    }

    void conferenceMenu() {

        mainFrame.setPanel(frameFactory.createPanel(PanelNames.names.CONFERENCES));
    }
}
