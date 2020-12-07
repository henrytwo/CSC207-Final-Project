package gui.mainMenu;

import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the MainMenuView
 */
class MainMenuPresenter extends AbstractPresenter {

    /**
     * @param mainFrame
     * @param mainMenuView
     * @param initializationArguments HashMap of values that can be used to set the initial state of a panel
     */
    MainMenuPresenter(IFrame mainFrame, IMainMenuView mainMenuView, Map<String, Object> initializationArguments) {
        super(mainFrame);

        // Initiate main menu tabs
        IPanel conferenceMenuView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_MENU, initializationArguments);
        mainMenuView.setConferenceMenuPanel(conferenceMenuView);

        IPanel messagingView = panelFactory.createPanel(PanelFactoryOptions.panelNames.MESSAGING, initializationArguments);
        mainMenuView.setMessagingPanel(messagingView);

        IPanel contactsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONTACTS, initializationArguments);
        mainMenuView.setContactsPanel(contactsView);

        // Logout button text
        mainMenuView.setLogoutButtonText(String.format("Logout (Signed in as %s)", userController.getUserFullName(signedInUserUUID)));

        // God mode users get something special
        if (userController.getUserIsGod(signedInUserUUID)) {
            mainMenuView.setTopBarPanelText("God mode enabled");
        }
    }

    /**
     * Launches about dialog
     */
    void about() {
        @SuppressWarnings("SpellCheckingInspection") String aboutMessage = "CSC207 Fall 2020\n" +
                "University of Toronto, St. George Campus\n\n" +
                "Developed by:\n" +
                "Henry Tu, Mahak Khurmi, Pranjal Bajaria, Antara Singh, Yilin Zhang, Emre Alca, Shubhra Bedi";

        IDialog aboutDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
            {
                put("title", "About");
                put("message", aboutMessage);
                put("messageType", DialogFactoryOptions.dialogType.PLAIN);
            }
        });
        aboutDialog.run();
    }

    /**
     * Launches a wonderful surprise
     */
    void surprise() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
            } else {
                noSurprise();
            }
        } catch (IOException | URISyntaxException e) {
            noSurprise();
        }
    }

    /**
     * I guess the surprise didn't work
     */
    private void noSurprise() {
        IDialog noSurpriseExceptionDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
            {
                put("title", "Error");
                put("message", "Unable to load surprise :(");
                put("messageType", DialogFactoryOptions.dialogType.ERROR);
            }
        });
        noSurpriseExceptionDialog.run();
    }

    void logout() {
        userController.logout();
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.LOGIN));
    }
}
