package gui.conference.events.menu;

import convention.ConferenceController;
import convention.EventController;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.*;
import user.UserController;
import util.ControllerBundle;

import java.util.*;
import java.util.function.Supplier;

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

    private UUID conferenceUUID;
    private int currentEventIndex = -1;
    private Map<String, Object> initializationArguments;

    private Supplier<Set<UUID>> getEvents;

    EventsMenuPresenter(IFrame mainFrame, IEventsMenuView eventMenuView, UUID conferenceUUID, Supplier<Set<UUID>> getEvents, UUID defaultEventUUID, Map<String, Object> initializationArguments) {
        this.mainFrame = mainFrame;
        this.eventMenuView = eventMenuView;
        this.getEvents = getEvents;
        this.initializationArguments = initializationArguments;
        this.panelFactory = mainFrame.getPanelFactory();
        this.dialogFactory = mainFrame.getDialogFactory();

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        this.eventController = controllerBundle.getEventController();
        this.userController = controllerBundle.getUserController();
        this.conferenceController = controllerBundle.getConferenceController();

        signedInUserUUID = userController.getCurrentUser();
        this.conferenceUUID = conferenceUUID;

        updateEventsList(getEvents, conferenceUUID, signedInUserUUID);

        if (eventUUIDs.size() > 0) {
            updateEventNames();

            int defaultEventIndex = 0;

            // Choose the specified default conference UUID
            if (defaultEventUUID != null && eventUUIDs.contains(defaultEventUUID)) {
                defaultEventIndex = eventUUIDs.indexOf(defaultEventUUID);
            }

            // Set initial conference selection
            eventMenuView.setEventListSelection(defaultEventIndex); // makes it look like we select it
            //selectConferencePanel(defaultEventIndex, (ConferenceTabsConstants.tabNames) initializationArguments.get("defaultTabName")); // this one actually sets the right hand panel
            selectEventPanel(defaultEventIndex);
        }

        updateButtons();
    }

    private void updateButtons() {
        if (!conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID)) {
            eventMenuView.setCreateEventButtonEnabled(false);
        }
    }

    private void updateEventNames() {
        String[] eventNames = new String[eventUUIDs.size()];

        for (int i = 0; i < eventUUIDs.size(); i++) {
            eventNames[i] = eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUIDs.get(i));
        }

        eventMenuView.setEventList(eventNames);
    }

    private void updateEventsList(Supplier<Set<UUID>> getEvents, UUID currentConferenceUUID, UUID signedInUserUUID) {
        currentEventIndex = -1;
        eventUUIDs = new ArrayList<>(getEvents.get());
    }

    void selectEventPanel(int index) {

        // Don't need to perform an update if we're already selected
        if (index != currentEventIndex) {
            currentEventIndex = index;
            UUID selectedEventUUID = eventUUIDs.get(index);

            // Update UI with tabs for this conference
            IPanel eventTabsPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENT_DETAILS, new HashMap<String, Object>(initializationArguments) {
                {
                    put("conferenceUUID", conferenceUUID);
                    put("eventUUID", selectedEventUUID);
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
        updateEventsList(getEvents, conferenceUUID, signedInUserUUID);
        updateEventNames();

        // Select the latest room
        int index = eventUUIDs.indexOf(selectedEventUUID);

        eventMenuView.setEventListSelection(index);
        selectEventPanel(index);
    }

    void createEvent() {
        IDialog eventFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_FORM, new HashMap<String, Object>(initializationArguments) {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        UUID newEventUUID = (UUID) eventFormDialog.run();
        if (newEventUUID != null) {
            updateAndSelectNewEvent(newEventUUID);
        }

    }

}