package gui.login;

import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.HashMap;

class LoginPresenter {

    private IFrame mainFrame;
    private ILoginView loginView;

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    private UserController userController;

    LoginPresenter(IFrame mainFrame, ILoginView loginView) {
        this.mainFrame = mainFrame;
        this.loginView = loginView;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
        panelFactory = mainFrame.getPanelFactory();
        dialogFactory = mainFrame.getDialogFactory();
    }

    void goToRegister() {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.REGISTER));
    }

    void login() {
        if (userController.login(loginView.getUsername(), loginView.getPassword()) != null) {
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
        } else {
            IDialog invalidLoginDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Invalid Credentials");
                    put("title", "Authentication Error");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidLoginDialog.run();
        }
    }
}
