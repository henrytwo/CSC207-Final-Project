package gui.login;

import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;

class LoginPresenter extends AbstractPresenter {

    private ILoginView loginView;

    /**
     * Constructor for Login Presenter
     *
     * @param mainFrame mainframe of the GUI
     * @param loginView view that this presenter is managing
     */
    LoginPresenter(IFrame mainFrame, ILoginView loginView) {
        super(mainFrame);
        this.loginView = loginView;
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
