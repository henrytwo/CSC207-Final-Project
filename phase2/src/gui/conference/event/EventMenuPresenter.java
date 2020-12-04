package gui.conference.event;

import convention.ConferenceController;
import convention.EventController;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventMenuPresenter {
    private IEventMenuView eventMenuView;
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

    public EventMenuPresenter(IFrame mainFrame, IEventMenuView eventMenuView, UUID defaultEventUUID, UUID currentConferenceUUID, Map<String, Object> initializationArguments) {
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

//    void joinEvent() {
//        int numberOfEvents = eventUUIDs.size();
//        ArrayList<UUID> availableEvents = new ArrayList<>();
//        for (int i = 0; i < numberOfEvents; i++) {
//            availableEvents.add(eventUUIDs.get(i));
//        }
//        ArrayList<UUID> attendeeEvents = new ArrayList<>(eventController.getAttendeeEvents(currentConferenceUUID, signedInUserUUID));
//        int attendeeEventsSize = attendeeEvents.size();
//        for (int j = 0; j < attendeeEventsSize; j++) {
//            availableEvents.remove(attendeeEvents.get(j));
//        }
//
//        if (availableEvents.size() == 0) {
//            IDialog noEventsDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
//                {
//                    put("title", "Error");
//                    put("message", "There are no events available for you to join.");
//                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
//                }
//            });
//
//            noEventsDialog.run();
//        } else {
//            IDialog eventPicker = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_PICKER, new HashMap<String, Object>() {
//                {
//                    put("instructions", "Choose a conference to join");
//                    put("availableConferenceUUIDs", availableEvents);
//                }
//            });
//
//            // IDialog returns Object type by default, so we have to cast
//            UUID selectedEventUUID = (UUID) eventPicker.run();
//
//            if (selectedEventUUID != null) {
//                eventController.registerForEvent(currentConferenceUUID, signedInUserUUID, signedInUserUUID, selectedEventUUID);
//
//                //updateAndSelectNewConference(selectedConferenceUUID);
//            }
//
//        }
//
//    }
}