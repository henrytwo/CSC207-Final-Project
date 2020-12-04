package gui.conference.tabs;

import convention.ConferenceController;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    private Map<String, Object> initializationArguments;

    ConferenceTabsPresenter(IFrame mainFrame, IConferenceTabsView conferenceTabsView, UUID conferenceUUID, Map<String, Object> initializationArguments) {
        this.mainFrame = mainFrame;
        this.conferenceTabsView = conferenceTabsView;
        this.conferenceUUID = conferenceUUID;
        this.initializationArguments = initializationArguments;

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
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.GENERAL, false); // Disable general tab
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.ALL_EVENTS, false); // Disable events tab
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.YOUR_REGISTERED_EVENTS, false); // Disable registered events tab
        }

        if (hasSpeakerPermissions) {

        } else {
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.YOUR_SPEAKER_EVENTS, false); // Disable speaker events tab
        }

        if (hasOrganizerPermissions) {
            IPanel settingsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_SETTINGS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", conferenceUUID);
                }
            });

            conferenceTabsView.setSettingsTabPanel(settingsView);

            IPanel roomsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_ROOMS, new HashMap<String, Object>(initializationArguments) {
                {
                    put("conferenceUUID", conferenceUUID);
                }
            });

            conferenceTabsView.setRoomsTabPanel(roomsView);

        } else {
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.ROOMS, false); // Disable rooms tab
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.SETTINGS, false); // Disable settings tab
        }
    }
}
