package gui.login;

import gui.util.enums.PanelNames;
import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPresenter implements ActionListener {

    private IFrame mainFrame;
    private ILoginView loginView;

    ControllerBundle controllerBundle;
    UserController userController;

    public LoginPresenter(IFrame mainFrame, ILoginView loginView) {
        this.mainFrame = mainFrame;
        this.loginView = loginView;

        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IPanelFactory panelFactory = new PanelFactory(mainFrame);

        switch (e.getActionCommand()) {
            case "login":
                userController.registerUser("wtf", "wtf", "wtf", "wtf");
                userController.login("wtf", "wtf");
                mainFrame.setPanel(panelFactory.createPanel(PanelNames.names.MAIN_MENU));
                break;
        }

    }
}
