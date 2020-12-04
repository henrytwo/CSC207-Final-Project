package gui.conference.events.rightPane;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.UUID;

public class EventsGeneralPresenter {

    private IEventsGeneralView eventsGeneralView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private EventController eventController;
    private UserController userController;
    private ConferenceController conferenceController;
    private RoomController roomController;

    private UUID eventUUID;
    private UUID signedInUserUUID;

    protected String role;

    private UUID currentConferenceUUID;


    public EventsGeneralPresenter(IFrame mainFrame, IEventsGeneralView eventGeneralView, UUID defaultEventUUID, UUID currentConferenceUUID) {
        this.mainFrame = mainFrame;
        this.eventsGeneralView = eventGeneralView;

        this.panelFactory = mainFrame.getPanelFactory();
        this.dialogFactory = mainFrame.getDialogFactory();

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();

        this.eventController = controllerBundle.getEventController();
        this.userController = controllerBundle.getUserController();
        this.conferenceController = controllerBundle.getConferenceController();
        this.roomController = controllerBundle.getRoomController();
        this.eventUUID = defaultEventUUID;

        signedInUserUUID = userController.getCurrentUser();
        this.currentConferenceUUID = currentConferenceUUID;

        updateGeneralData();

    }

    void registerForEvent(){

    }

    void deleteEvent(){

    }

    void editEvent(){

    }

    void unregisterFromEvent(){
        IDialog confirmLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you want to unregister from this event? (%s)", eventController.getEventTitle(currentConferenceUUID, signedInUserUUID, eventUUID)));
                put("title", "Confirm unregister Event");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if((boolean) confirmLeaveDialog.run()){
            eventController.unregisterForEvent(currentConferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
        }
    }

    private void updateRole() {
        if (conferenceController.isOrganizer(currentConferenceUUID, signedInUserUUID, signedInUserUUID)) {
            role = "Organizer";
        } else if (conferenceController.isSpeaker(currentConferenceUUID, signedInUserUUID, signedInUserUUID)) {
            role = "Speaker";
        } else {
            role = "Attendee";
        }
    }

    private void updateGeneralData() {
        boolean isSpeaker = conferenceController.isSpeaker(currentConferenceUUID, signedInUserUUID, signedInUserUUID);

        String[][] tableData = {
                {"Event Name", eventController.getEventTitle(currentConferenceUUID, signedInUserUUID, eventUUID)},
                {"Start", eventController.getEventTimeRange(currentConferenceUUID, signedInUserUUID, eventUUID).getStart().toString()},
                {"End", eventController.getEventTimeRange(currentConferenceUUID, signedInUserUUID, eventUUID).getEnd().toString()},
                {"UUID", eventUUID.toString()},
                {},
                {"Room Location", "" + roomController.getRoomLocation(currentConferenceUUID, signedInUserUUID,eventController.getEventRoom(currentConferenceUUID, signedInUserUUID, eventUUID))},
                {},
                {"# Attendees", "" + eventController.getNumRegistered(currentConferenceUUID, signedInUserUUID, eventUUID) + "/" + roomController.getRoomCapacity(currentConferenceUUID, signedInUserUUID, eventController.getEventRoom(currentConferenceUUID, signedInUserUUID, eventUUID))},
                {"# Speakers", "" + eventController.getEventSpeakers(currentConferenceUUID, signedInUserUUID, eventUUID).size()},
        };

        String[] columnNames = {
                "Key",
                "Value"
        };

        eventsGeneralView.setTableData(tableData, columnNames);
    }
}
