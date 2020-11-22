package gui.login;

import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPresenter implements ActionListener {

    private IFrame mainFrame;
    private IPanel loginView;

    public LoginPresenter(IFrame mainFrame, IPanel loginView) {
        this.mainFrame = mainFrame;
        this.loginView = loginView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IPanelFactory panelFactory = new PanelFactory(mainFrame);

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        UserController userController = controllerBundle.getUserController();

        switch (e.getActionCommand()) {
            case "login":
                userController.registerUser("wtf", "wtf", "wtf", "wtf");
                userController.login("wtf", "wtf");
                mainFrame.setPanel(panelFactory.createPanel("mainMenu"));
                break;
        }

    }
}
