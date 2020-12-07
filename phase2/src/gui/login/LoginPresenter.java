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

    /**
     * Constructor for Login Presenter
     *
     * @param mainFrame mainframe of the GUI
     * @param loginView view that this presenter is managing
     */
    LoginPresenter(IFrame mainFrame, ILoginView loginView) {
        this.mainFrame = mainFrame;
        this.loginView = loginView;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
        panelFactory = mainFrame.getPanelFactory();
        dialogFactory = mainFrame.getDialogFactory();
    }

    /**
     * This method helps to reset the password for a user who forgot their current password
     */
    void resetPassword() {
        IDialog resetPasswordDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
            {
                put("title", "Reset Password");
                put("message", "Oh you forgot your password? That's too bad \uD83D\uDE1B");
                put("messageType", DialogFactoryOptions.dialogType.PLAIN);
            }
        });
        resetPasswordDialog.run();
    }

    /**
     * Goes to the register page if a first time user
     */
    void goToRegister() {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.REGISTER));
    }

    /**
     * Login as an already registered user
     */
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
