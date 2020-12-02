package gui.register;

import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

public class RegisterPresenter {
    private IFrame mainFrame;
    private IRegisterView registerView;
    private IPanelFactory panelFactory;

    ControllerBundle controllerBundle;
    UserController userController;

    RegisterPresenter(IFrame mainFrame, IRegisterView registerView) {
        this.mainFrame = mainFrame;
        this.registerView = registerView;

        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
        panelFactory = mainFrame.getPanelFactory();
    }

    void register() {
        userController.registerUser("Test", "Testerson", "test", "test");
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
    }
}
