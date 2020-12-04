package gui.conference.events.rightPane;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import gui.conference.events.menu.EventsMenuPresenter;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
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

    private EventsMenuPresenter eventsMenuPresenter;


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
        if(eventController.getNumRegistered(currentConferenceUUID, signedInUserUUID, eventUUID) ==
                roomController.getRoomCapacity(currentConferenceUUID, signedInUserUUID, eventController.getEventRoom(currentConferenceUUID, signedInUserUUID, eventUUID))){
            IDialog fullCapacityDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Sorry, that event is already at full capacity.");
                    put("title", "Error");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });
            fullCapacityDialog.run();
        }
        else{
            eventController.registerForEvent(currentConferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
        }
    }

    void deleteEvent(){

        if(role != "Organiser"){
            IDialog accessDeniedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Only Organisers of an event can delete an event.");
                    put("title", "Error");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            accessDeniedDialog.run();
        }
        else{
            IDialog confirmDeleteDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to delete this event? (%s)", eventController.getEventTitle(currentConferenceUUID, signedInUserUUID, eventUUID)));
                    put("title", "Confirm delete Event");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });
            if((boolean) confirmDeleteDialog.run()){
                eventController.deleteEvent(currentConferenceUUID, signedInUserUUID, eventUUID);
                mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENTS));
            }
        }

    }

    void editEvent(){
        IDialog eventFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", currentConferenceUUID);
                put("eventUUID", eventUUID);
            }
        });

        if (eventFormDialog.run() != null) {
            // Reload the main menu to update changes
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_EVENTS, new HashMap<String, Object>() {
                {
                    put("defaultConferenceUUID", currentConferenceUUID);
                    put("defaultTabName", ConferenceTabsConstants.tabNames.ALL_EVENTS);
                }
            }));
        }
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
            // MAKE SURE TO KICK THIS USER FROM THE CHAT
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