package gui.conference.events.details;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.exception.FullEventException;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EventsDetailsPresenter {

    private IEventsDetailsView eventsGeneralView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private EventController eventController;
    private UserController userController;
    private ConferenceController conferenceController;
    private RoomController roomController;

    private UUID eventUUID;
    private UUID signedInUserUUID;

    private UUID currentConferenceUUID;

    private EventsMenuPresenter eventsMenuPresenter;

    private Map<String, Object> initializationArguments;

    EventsDetailsPresenter(IFrame mainFrame, IEventsDetailsView eventGeneralView, UUID defaultEventUUID, UUID currentConferenceUUID, Map<String, Object> initializationArguments) {
        this.mainFrame = mainFrame;
        this.eventsGeneralView = eventGeneralView;
        this.initializationArguments = initializationArguments;

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

        updateUserData();
        updateGeneralData();
        updateButtons();
    }

    private void updateUserData() {
        if (conferenceController.isOrganizer(currentConferenceUUID, signedInUserUUID, signedInUserUUID)) {

            Set<UUID> attendeeUUIDs = eventController.getEventAttendees(currentConferenceUUID, signedInUserUUID, eventUUID);

            String[][] tableData = new String[1][attendeeUUIDs.size()];

            int index = 0;

            for (UUID userUUID : attendeeUUIDs) {
                tableData[0][index] = userController.getUserFullName(userUUID);

                index++;
            }

            String[] columnNames = {
                    "Event Attendees",
            };

            eventsGeneralView.setUserTableData(tableData, columnNames);

        }
    }

    private void updateButtons() {

        if (eventController.isRegistered(currentConferenceUUID, signedInUserUUID, eventUUID)) {
            eventsGeneralView.setRegisterButtonText("Unregister");
        }

        if (!conferenceController.isOrganizer(currentConferenceUUID, signedInUserUUID, signedInUserUUID)) {
            eventsGeneralView.enableEditEventButton(false);
            eventsGeneralView.enableDeleteEventButton(false);
        }
    }

    void toggleRegistration() {
        if (eventController.isRegistered(currentConferenceUUID, signedInUserUUID, eventUUID)) {
            IDialog confirmLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to unregister from this event? (%s)", eventController.getEventTitle(currentConferenceUUID, signedInUserUUID, eventUUID)));
                    put("title", "Confirm unregister Event");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmLeaveDialog.run()) {
                eventController.unregisterForEvent(currentConferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
                reloadPage(eventUUID);
            }
        } else {
            try {
                eventController.registerForEvent(currentConferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);

                IDialog registeredDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "You have successfully registered for this event.");
                        put("title", "Registered");
                        put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                    }
                });

                registeredDialog.run();

                reloadPage(eventUUID);
            } catch (FullEventException e) {
                IDialog fullCapacityDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "Sorry, that event is already at full capacity.");
                        put("title", "Error");
                        put("messageType", DialogFactoryOptions.dialogType.ERROR);
                    }
                });

                fullCapacityDialog.run();
            }
        }
    }

    void deleteEvent() {
        IDialog confirmDeleteDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you want to delete this event? (%s)", eventController.getEventTitle(currentConferenceUUID, signedInUserUUID, eventUUID)));
                put("title", "Confirm delete Event");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });
        if ((boolean) confirmDeleteDialog.run()) {
            eventController.deleteEvent(currentConferenceUUID, signedInUserUUID, eventUUID);
            reloadPage(null);
        }
    }

    void editEvent() {
        IDialog eventFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", currentConferenceUUID);
                put("eventUUID", eventUUID);
            }
        });

        if (eventFormDialog.run() != null) {
            // Reload the main menu to update changes
            reloadPage(eventUUID);
        }
    }

    private void reloadPage(UUID defaultEventUUID) {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>(initializationArguments) {
            {
                put("defaultConferenceUUID", currentConferenceUUID);
                put("defaultTabName", ConferenceTabsConstants.tabNames.ALL_EVENTS);
                put("defaultEventUUID", defaultEventUUID);
            }
        }));
    }

    private void updateGeneralData() {
        boolean isSpeaker = conferenceController.isSpeaker(currentConferenceUUID, signedInUserUUID, signedInUserUUID);

        String[][] tableData = {
                {"Event Name", eventController.getEventTitle(currentConferenceUUID, signedInUserUUID, eventUUID)},
                {"Start", eventController.getEventTimeRange(currentConferenceUUID, signedInUserUUID, eventUUID).getStart().toString()},
                {"End", eventController.getEventTimeRange(currentConferenceUUID, signedInUserUUID, eventUUID).getEnd().toString()},
                {"UUID", eventUUID.toString()},
                {},
                {"Room Location", "" + roomController.getRoomLocation(currentConferenceUUID, signedInUserUUID, eventController.getEventRoom(currentConferenceUUID, signedInUserUUID, eventUUID))},
                {},
                {"# Attendees", "" + eventController.getNumRegistered(currentConferenceUUID, signedInUserUUID, eventUUID) + "/" + roomController.getRoomCapacity(currentConferenceUUID, signedInUserUUID, eventController.getEventRoom(currentConferenceUUID, signedInUserUUID, eventUUID))},
                {"# Speakers", "" + eventController.getEventSpeakers(currentConferenceUUID, signedInUserUUID, eventUUID).size()},
        };

        String[] columnNames = {
                "Key",
                "Value"
        };

        eventsGeneralView.setGeneralTableData(tableData, columnNames);
    }
}