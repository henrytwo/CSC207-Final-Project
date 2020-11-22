package gui.mainMenu;

import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPresenter implements ActionListener {

    IPanel mainMenuView;
    IFrame mainFrame;

    public MainMenuPresenter(IFrame mainFrame, IPanel mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IPanelFactory frameFactory = new PanelFactory(mainFrame);

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        UserController userController = controllerBundle.getUserController();

        System.out.println(e);

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
