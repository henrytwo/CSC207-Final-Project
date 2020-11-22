package gui.mainMenu;

import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
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
        IPanelFactory frameFactory = new PanelFactory(mainFrame, mainMenuView);

        System.out.println(e);

        switch (e.getActionCommand()) {
            case "logout":
                mainFrame.setPanel(mainMenuView.getParent());
                break;
            case "conferenceMenu":
                mainFrame.setPanel(frameFactory.createPanel("Conference"));
                break;
        }

    }
}
