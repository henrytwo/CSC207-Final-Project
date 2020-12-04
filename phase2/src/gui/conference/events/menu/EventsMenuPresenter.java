package gui.conference.events.menu;

import convention.ConferenceController;
import convention.EventController;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.*;
import user.UserController;
import util.ControllerBundle;

import java.util.*;

public class EventsMenuPresenter {
    private IEventsMenuView eventMenuView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private EventController eventController;
    private UserController userController;
    private ConferenceController conferenceController;

    private List<UUID> eventUUIDs;
    private UUID signedInUserUUID;

    private UUID currentConferenceUUID;
    private int currentEventIndex = -1;
    private Map<String, Object> initializationArguments;

    public EventsMenuPresenter(IFrame mainFrame, IEventsMenuView eventMenuView, UUID defaultEventUUID, UUID currentConferenceUUID, Map<String, Object> initializationArguments) {
        this.mainFrame = mainFrame;
        this.eventMenuView = eventMenuView;
        this.panelFactory = mainFrame.getPanelFactory();
        this.dialogFactory = mainFrame.getDialogFactory();

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        this.eventController = controllerBundle.getEventController();
        this.userController = controllerBundle.getUserController();
        this.conferenceController = controllerBundle.getConferenceController();

        signedInUserUUID = userController.getCurrentUser();
        this.currentConferenceUUID = currentConferenceUUID;

        updateEventsList(currentConferenceUUID, signedInUserUUID);

        if (eventUUIDs.size() > 0) {
            updateEventNames();

            int defaultConferenceIndex = 0;

            // Choose the specified default conference UUID
            if (defaultEventUUID != null && eventUUIDs.contains(defaultEventUUID)) {
                defaultConferenceIndex = eventUUIDs.indexOf(defaultEventUUID);
            }

            // Set initial conference selection
            eventMenuView.setEventListSelection(defaultConferenceIndex); // makes it look like we select it
            //selectConferencePanel(defaultConferenceIndex, (ConferenceTabsConstants.tabNames) initializationArguments.get("defaultTabName")); // this one actually sets the right hand panel
        }
    }

    private void updateEventNames() {
        String[] eventNames = new String[eventUUIDs.size()];

        for (int i = 0; i < eventUUIDs.size(); i++) {
            eventNames[i] = conferenceController.getConferenceName(eventUUIDs.get(i));
        }

        eventMenuView.setEventList(eventNames);
    }

    private void updateEventsList(UUID currentConferenceUUID, UUID signedInUserUUID) {
        currentEventIndex = -1;
        eventUUIDs = new ArrayList<>(eventController.getEvents(currentConferenceUUID, signedInUserUUID));
    }

    void selectEventPanel(int index) {
        selectEventPanel(index, null);
    }

    void selectEventPanel(int index, ConferenceTabsConstants.tabNames defaultTabName) {
        // Don't need to perform an update if we're already selected
        if (index != currentEventIndex) {
            currentEventIndex = index;
            UUID selectedEventUUID = eventUUIDs.get(index);

            // Update UI with tabs for this conference
            IPanel eventTabsPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENTS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", currentConferenceUUID);
                    put("eventUUID", selectedEventUUID);
                    put("defaultTabName", defaultTabName);
                }
            });

            eventMenuView.setEventTabs(eventTabsPanel);
        }
    }

    /**
     * Updates the local list of events and selects an event by UUID
     *
     * @param selectedEventUUID UUID of event to open
     */
    private void updateAndSelectNewEvent(UUID selectedEventUUID) {
        // Update the local list with the new room
        updateEventsList(currentConferenceUUID, signedInUserUUID);
        updateEventNames();

        // Select the latest room
        int index = eventUUIDs.indexOf(selectedEventUUID);

        eventMenuView.setEventListSelection(index);
        selectEventPanel(index);
    }

    void createEvent() {
        IDialog eventFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", currentConferenceUUID);
            }
        });

        UUID newEventUUID = (UUID) eventFormDialog.run();
        if (newEventUUID != null) {
            updateAndSelectNewEvent(newEventUUID);
        }

    }

}