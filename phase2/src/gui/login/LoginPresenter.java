package gui.login;

import gui.util.enums.PanelNames;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

public class LoginPresenter {

    private IFrame mainFrame;
    private ILoginView loginView;
    private IPanelFactory panelFactory;

    ControllerBundle controllerBundle;
    UserController userController;

    LoginPresenter(IFrame mainFrame, ILoginView loginView) {
        this.mainFrame = mainFrame;
        this.loginView = loginView;

        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
        panelFactory = mainFrame.getPanelFactory();
    }

    void login() {
        userController.registerUser("Test", "Testerson", "test", "test");
        userController.login("test", "test");
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.MAIN_MENU));
    }
}
