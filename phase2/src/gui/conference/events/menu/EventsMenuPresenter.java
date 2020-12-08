package gui.conference.events.menu;

import gui.conference.tabs.ConferenceTabsConstants;
import gui.conference.util.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.util.*;
import java.util.function.Supplier;

/**
 * Manages EventMenuView
 */
class EventsMenuPresenter extends AbstractConferencePresenter {
    private IEventsMenuView eventMenuView;

    private List<UUID> eventUUIDs;

    private int currentEventIndex = -1;
    private Map<String, Object> initializationArguments;

    /**
     *
     * @param mainFrame main gui frame
     * @param eventMenuView view to manage
     * @param conferenceUUID UUID of the associated
     * @param getEvents lambda function that returns set of all events
     * @param defaultEventUUID UUID of the default event to select, if none provided the first one wll be selected.
     * @param initializationArguments HashMap of values to initialise initial state
     */
    EventsMenuPresenter(IFrame mainFrame, IEventsMenuView eventMenuView, UUID conferenceUUID, Supplier<Set<UUID>> getEvents, UUID defaultEventUUID, Map<String, Object> initializationArguments) {
        super(mainFrame, conferenceUUID);

        this.eventMenuView = eventMenuView;
        this.initializationArguments = initializationArguments;

        updateEventsList(getEvents);

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

    private void updateEventsList(Supplier<Set<UUID>> getEvents) {
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

    void createEvent() {
        IDialog eventFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_FORM, new HashMap<String, Object>(initializationArguments) {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        UUID newEventUUID = (UUID) eventFormDialog.run();
        if (newEventUUID != null) {
            reloadEventsPage(newEventUUID);
        }
    }

    private void reloadEventsPage(UUID defaultEventUUID) {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>(initializationArguments) {
            {
                put("defaultConferenceUUID", conferenceUUID);
                put("defaultTabName", ConferenceTabsConstants.tabNames.ALL_EVENTS);
                put("defaultEventUUID", defaultEventUUID);
            }
        }));
    }
}