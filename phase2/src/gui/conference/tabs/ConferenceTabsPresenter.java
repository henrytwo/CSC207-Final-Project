package gui.conference.tabs;

import gui.conference.util.AbstractConferencePresenter;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Presenter for ConferenceTabs
 */
class ConferenceTabsPresenter extends AbstractConferencePresenter {

    private boolean hasAttendeePermissions;
    private boolean hasSpeakerPermissions;
    private boolean hasOrganizerPermissions;

    private IConferenceTabsView conferenceTabsView;

    private Map<String, Object> initializationArguments;

    /**
     * @param mainFrame               main GUI frame
     * @param conferenceTabsView      view being managed
     * @param conferenceUUID          UUID of the associated conference
     * @param initializationArguments arguments used to initialize child components
     */
    ConferenceTabsPresenter(IFrame mainFrame, IConferenceTabsView conferenceTabsView, UUID conferenceUUID, Map<String, Object> initializationArguments) {
        super(mainFrame, conferenceUUID);

        this.conferenceTabsView = conferenceTabsView;
        this.initializationArguments = initializationArguments;

        hasOrganizerPermissions = conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);
        hasSpeakerPermissions = conferenceController.isSpeaker(conferenceUUID, signedInUserUUID, signedInUserUUID) || hasOrganizerPermissions;
        hasAttendeePermissions = conferenceController.isAttendee(conferenceUUID, signedInUserUUID, signedInUserUUID) || hasSpeakerPermissions;

        updateTabs();
    }

    /**
     * Updates tab given the user's role
     */
    private void updateTabs() {
        if (hasAttendeePermissions) {
            IPanel generalView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_GENERAL, new HashMap<String, Object>(initializationArguments) {
                {
                    put("conferenceUUID", conferenceUUID);
                }
            });

            IPanel allEventsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENTS, new HashMap<String, Object>(initializationArguments) {
                {
                    put("conferenceUUID", conferenceUUID);
                    put("getEvents", (Supplier) () -> eventController.getEvents(conferenceUUID, signedInUserUUID));
                }
            });

            IPanel registeredEventsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENTS, new HashMap<String, Object>(initializationArguments) {
                {
                    put("conferenceUUID", conferenceUUID);
                    put("getEvents", (Supplier) () -> eventController.getAttendeeEvents(conferenceUUID, signedInUserUUID));
                }
            });

            conferenceTabsView.setAllEventsTabPanel(allEventsView);
            conferenceTabsView.setRegisteredEventsTabPanel(registeredEventsView);
            conferenceTabsView.setGeneralTabPanel(generalView);
        } else {
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.GENERAL, false); // Disable general tab
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.ALL_EVENTS, false); // Disable events tab
            conferenceTabsView.setTabEnabled(ConferenceTabsConstants.tabNames.YOUR_REGISTERED_EVENTS, false); // Disable registered events tab
        }

        if (hasSpeakerPermissions) {
            IPanel speakerEventsView = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENTS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", conferenceUUID);
                    put("getEvents", (Supplier) () -> eventController.getSpeakerEvents(conferenceUUID, signedInUserUUID));
                }
            });

            conferenceTabsView.setSpeakersTabPanel(speakerEventsView);
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
