package gui.register;

import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.HashMap;

class RegisterPresenter {
    private IFrame mainFrame;
    private IRegisterView registerView;
    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private UserController userController;

    RegisterPresenter(IFrame mainFrame, IRegisterView registerView) {
        this.mainFrame = mainFrame;
        this.registerView = registerView;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
        panelFactory = mainFrame.getPanelFactory();
        dialogFactory = mainFrame.getDialogFactory();
    }

    void goToLogin() {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.LOGIN));
    }

    void register() {
        if (registerView.getFirstname().length() == 0 || registerView.getLastname().length() == 0 || registerView.getUsername().length() == 0 || registerView.getPassword().length() == 0) {
            IDialog invalidRegistrationDialog
                    = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Unable to register: Fields must be non-empty!");
                    put("title", "Authentication Error");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidRegistrationDialog.run();
        } else {
            if (userController.registerUser(registerView.getFirstname(), registerView.getLastname(), registerView.getUsername(), registerView.getPassword()) != null) {
                mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
            } else {
                IDialog invalidRegistrationDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "Unable to register: Username is already taken");
                        put("title", "Authentication Error");
                        put("messageType", DialogFactoryOptions.dialogType.ERROR);
                    }
                });

                invalidRegistrationDialog.run();
            }
        }
    }
}
