package gui.conference.events.form;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.calendar.TimeRange;
import convention.exception.InvalidEventTimeException;
import convention.exception.InvalidNameException;
import convention.exception.InvalidTimeRangeException;
import convention.exception.NullRoomException;
import gui.util.DateParser;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventFormPresenter {

    ConferenceController conferenceController;
    EventController eventController;
    RoomController roomController;

    private boolean isExistingEvent;
    private UUID eventUUID;
    private UUID conferenceUUID;
    private UUID userUUID;

    private IDialogFactory dialogFactory;

    private IEventFormDialog eventFormDialog;

    private String eventName = "";
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeRange timeRange;
    private Set<UUID> speakerUUIDs = new HashSet<>();

    private Set<UUID> selectedSpeakersUUIDS;
    private Set<UUID> availableUserUUIDS;
    private UUID selectedRoomUUID;

    private DateParser dateParser = new DateParser();

    EventFormPresenter(IFrame mainFrame, IEventFormDialog eventFormDialog, UUID conferenceUUID, UUID eventUUID) {

        this.eventFormDialog = eventFormDialog;

        this.dialogFactory = mainFrame.getDialogFactory();

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        UserController userController = controllerBundle.getUserController();
        this.eventController = controllerBundle.getEventController();
        this.conferenceController = controllerBundle.getConferenceController();
        this.roomController = controllerBundle.getRoomController();
        this.conferenceUUID = conferenceUUID;
        this.eventUUID = eventUUID;
        this.userUUID = controllerBundle.getUserController().getCurrentUser();

        this.availableUserUUIDS = userController.getUsers();

        // Existing conferences will have a non-null UUID
        isExistingEvent = eventUUID != null;

        if (isExistingEvent) {
            eventFormDialog.setDialogTitle(String.format("Editing Event (%s)", eventUUID));

            eventName = eventController.getEventTitle(conferenceUUID, userUUID, eventUUID);
            startTime = eventController.getEventTimeRange(conferenceUUID, userUUID, eventUUID).getStart();
            endTime = eventController.getEventTimeRange(conferenceUUID, userUUID, eventUUID).getEnd();
            timeRange = eventController.getEventTimeRange(conferenceUUID, userUUID, eventUUID);
            speakerUUIDs = eventController.getEventSpeakers(conferenceUUID, userUUID, eventUUID);
            selectedRoomUUID = eventController.getEventRoom(conferenceUUID, userUUID, eventUUID);

            eventFormDialog.setName(eventName);
            eventFormDialog.setStart(dateParser.dateTimeToString(startTime));
            eventFormDialog.setEnd(dateParser.dateTimeToString(endTime));
            //eventFormDialog.setRoomArea(controllerBundle.getRoomController().getRoomLocation(conferenceUUID, userUUID, roomUUID));

        } else {
            eventFormDialog.setDialogTitle("Create new event");
        }

    }

    void submit() {
        try {
            eventName = eventFormDialog.getName();

            startTime = dateParser.stringToDateTime(eventFormDialog.getStart());
            endTime = dateParser.stringToDateTime(eventFormDialog.getEnd());

            timeRange = new TimeRange(startTime, endTime);

            if (isExistingEvent) {
                eventController.setEventTitle(conferenceUUID, userUUID, eventUUID, eventName);
                eventController.setEventRoom(conferenceUUID, userUUID, eventUUID, selectedRoomUUID);
                if(!(eventController.getEventTimeRange(conferenceUUID,userUUID, eventUUID).equals(timeRange))){
                    eventController.setEventTimeRange(conferenceUUID, userUUID, eventUUID, timeRange);
                }

            } else {
                eventUUID = eventController.createEvent(conferenceUUID, userUUID, eventName, timeRange, selectedRoomUUID, speakerUUIDs);
                eventController.createEventConversation(conferenceUUID, userUUID, eventUUID);
            }

            // Update event UUID in case it has changed
            eventFormDialog.setEventUUID(eventUUID);
            eventFormDialog.setUpdated(true);

            // Close the dialog so it isn't blocking anymore
            eventFormDialog.close();
        } catch (DateTimeParseException e) {

            IDialog dateTimeParseErrorDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", String.format("Unable to submit form: Invalid date. Please follow the given format. [%s]", dateParser.getFormat()));
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            dateTimeParseErrorDialog.run();

        } catch (InvalidTimeRangeException e) {

            IDialog invalidTimeRangeDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid date range. End time must be after start time.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidTimeRangeDialog.run();

        } catch (InvalidNameException e) {

            IDialog invalidConferenceNameDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid name. Event name must be non empty.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidConferenceNameDialog.run();

        } catch (InvalidEventTimeException e) {
            IDialog invalidEventTimeDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: " + e.getMessage());
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidEventTimeDialog.run();
        } catch (NullRoomException e) {
            IDialog nullRoomExceptionDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid room selection");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            nullRoomExceptionDialog.run();
        }


    }

    void selectSpeakers() {
        // Getting all available speakerUUIDs
        Set<UUID> userUUIDs = conferenceController.getUsers(conferenceUUID, userUUID);
        for (UUID uuid : userUUIDs) {
            if (eventController.speakerTimeRangeOccupied(conferenceUUID, uuid, timeRange)) {
                availableUserUUIDS.add(uuid);
            }
        }

        IDialog chooseSpeakersDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MULTI_USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select speakers for this event");
                put("availableUserUUIDs", availableUserUUIDS);
                put("selectedUserUUIDs", selectedSpeakersUUIDS);
            }
        });

        Set<UUID> newSelectedUserUUIDs = (Set<UUID>) chooseSpeakersDialog.run();

        if (newSelectedUserUUIDs != null) {
            newSelectedUserUUIDs.add(userUUID); // We need to add the signed in user in the conversation too
            selectedSpeakersUUIDS = newSelectedUserUUIDs;
        }

    }

    void selectRoom() {
        Set<UUID> availableRoomUUIDS = roomController.getRooms(conferenceUUID, userUUID);
        for (UUID uuid : eventController.getEvents(conferenceUUID, userUUID)) {
            if (availableRoomUUIDS.contains(uuid)) {
                availableRoomUUIDS.remove(uuid);
            }
        }

        IDialog chooseRoomDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.ROOM_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select room for this event");
                put("availableRoomUUIDs", availableRoomUUIDS);
                put("conferenceUUID", conferenceUUID);
            }
        });

        selectedRoomUUID = (UUID) chooseRoomDialog.run();
    }
}
