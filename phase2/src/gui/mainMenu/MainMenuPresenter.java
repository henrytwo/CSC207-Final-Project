package gui.mainMenu;

import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPresenter implements ActionListener {
    IMainMenuView mainMenuView;
    IFrame mainFrame;

    ControllerBundle controllerBundle;
    UserController userController;

    public MainMenuPresenter(IFrame mainFrame, IMainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.mainFrame = mainFrame;

        controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();

        mainMenuView.setSignedInAs(String.format("Signed in as %s", userController.getUserFullName(userController.getCurrentUser())));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IPanelFactory frameFactory = new PanelFactory(mainFrame);

        switch (e.getActionCommand()) {
            case "stuff":
                // test interacting with the view
                break;
            case "logout":
                userController.logout();
                mainFrame.setPanel(frameFactory.createPanel("login"));
                break;
            case "conferenceMenu":
                mainFrame.setPanel(frameFactory.createPanel("conferenceMenu"));
                break;
        }

    }
}
