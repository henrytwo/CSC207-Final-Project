package gui.conference.tabs;

import convention.ConferenceController;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;
import java.util.UUID;

import java.util.HashMap;

public class ConferenceTabsPresenter {
    private IPanelFactory panelFactory;

    private ConferenceController conferenceController;
    private UserController userController;

    private UUID conferenceUUID;
    private UUID signedInUserUUID;

    private boolean hasAttendeePermissions;
    private boolean hasSpeakerPermissions;
    private boolean hasOrganizerPermissions;

    private IFrame mainFrame;
    private IConferenceTabsView conferenceTabsView;

    ConferenceTabsPresenter(IFrame mainFrame, IConferenceTabsView conferenceTabsView, UUID conferenceUUID) {
        this.mainFrame = mainFrame;
        this.conferenceTabsView = conferenceTabsView;
        this.conferenceUUID = conferenceUUID;

        panelFactory = mainFrame.getPanelFactory();
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();

        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();

        signedInUserUUID = userController.getCurrentUser();

        hasOrganizerPermissions = conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);
        hasSpeakerPermissions = conferenceController.isSpeaker(conferenceUUID, signedInUserUUID, signedInUserUUID) || hasOrganizerPermissions;
        hasAttendeePermissions = conferenceController.isAttendee(conferenceUUID, signedInUserUUID, signedInUserUUID) || hasSpeakerPermissions;

        updateTabs();
    }

    private void updateTabs() {
        if (hasAttendeePermissions) {
            IPanel generalView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_GENERAL, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", conferenceUUID);
                }
            });

            conferenceTabsView.setGeneralTabPanel(generalView);
        } else {
            conferenceTabsView.setTabEnabled(0, false); // Disable general tab
            conferenceTabsView.setTabEnabled(1, false); // Disable events tab
            conferenceTabsView.setTabEnabled(2, false); // Disable registered events tab
        }

        if (hasSpeakerPermissions) {

        } else {
            conferenceTabsView.setTabEnabled(3, false); // Disable speaker events tab
        }

        if (hasOrganizerPermissions) {
            IPanel settingsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_SETTINGS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", conferenceUUID);
                }
            });

            conferenceTabsView.setSettingsTabPanel(settingsView);
        } else {
            conferenceTabsView.setTabEnabled(4, false); // Disable rooms tab
            conferenceTabsView.setTabEnabled(5, false); // Disable settings tab
        }


    }
}
