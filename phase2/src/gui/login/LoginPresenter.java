package gui.login;

import gui.util.enums.PanelNames;
import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

public class LoginPresenter {

    private IFrame mainFrame;
    private ILoginView loginView;

    ControllerBundle controllerBundle;
    UserController userController;

    IPanelFactory panelFactory;

    LoginPresenter(IFrame mainFrame, ILoginView loginView) {
        this.mainFrame = mainFrame;
        this.loginView = loginView;

        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        panelFactory = new PanelFactory(mainFrame);
    }

    void login() {
        userController.registerUser("wtf", "wtf", "wtf", "wtf");
        userController.login("wtf", "wtf");
        mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.MAIN_MENU));
    }
}
