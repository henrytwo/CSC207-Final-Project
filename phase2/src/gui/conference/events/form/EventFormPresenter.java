package gui.conference.events.form;

import convention.calendar.TimeRange;
import convention.exception.*;
import gui.conference.util.AbstractConferencePresenter;
import gui.util.date.DateParser;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Manages EventFormDialog
 */
class EventFormPresenter extends AbstractConferencePresenter {

    private boolean isExistingEvent;
    private UUID eventUUID;

    private IEventFormDialog eventFormDialog;

    private String eventName = "";
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeRange timeRange;

    private Set<UUID> selectedSpeakersUUIDS = new HashSet<>();
    private UUID selectedRoomUUID;

    private DateParser dateParser = new DateParser();

    /**
     * Constructor for EventFormPresenter
     *
     * @param mainFrame       current frame
     * @param eventFormDialog dialog to be managed
     * @param conferenceUUID  UUID of the current conference.
     * @param eventUUID       UUID of the current event.
     */
    EventFormPresenter(IFrame mainFrame, IEventFormDialog eventFormDialog, UUID conferenceUUID, UUID eventUUID) {
        super(mainFrame, conferenceUUID);

        this.eventFormDialog = eventFormDialog;
        this.eventUUID = eventUUID;

        // Existing conferences will have a non-null UUID
        isExistingEvent = eventUUID != null;

        if (isExistingEvent) {
            eventFormDialog.setDialogTitle(String.format("Editing Event (%s)", eventUUID));

            eventName = eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID);
            startTime = eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).getStart();
            endTime = eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).getEnd();
            timeRange = eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID);
            selectedSpeakersUUIDS = eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID);
            selectedRoomUUID = eventController.getEventRoom(conferenceUUID, signedInUserUUID, eventUUID);

            eventFormDialog.setName(eventName);
            eventFormDialog.setStart(dateParser.dateTimeToString(startTime));
            eventFormDialog.setEnd(dateParser.dateTimeToString(endTime));

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
                eventController.setEventTitle(conferenceUUID, signedInUserUUID, eventUUID, eventName);

                // Don't update event time if it didn't change
                if (!eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).equals(timeRange)) {
                    eventController.setEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID, timeRange);
                }

                // Don't update room if it didn't change
                if (!eventController.getEventRoom(conferenceUUID, signedInUserUUID, eventUUID).equals(selectedRoomUUID)) {
                    eventController.setEventRoom(conferenceUUID, signedInUserUUID, eventUUID, selectedRoomUUID);
                }

                // Only update speakers that were added/removed
                Set<UUID> existingSpeakerUUIDs = eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID);

                for (UUID speakerUUID : existingSpeakerUUIDs) {
                    if (!selectedSpeakersUUIDS.contains(speakerUUID)) {
                        // Speaker was removed
                        eventController.removeEventSpeaker(conferenceUUID, signedInUserUUID, eventUUID, speakerUUID);
                    }
                }

                for (UUID speakerUUID : selectedSpeakersUUIDS) {
                    if (!existingSpeakerUUIDs.contains(speakerUUID)) {
                        eventController.addEventSpeaker(conferenceUUID, signedInUserUUID, eventUUID, speakerUUID);
                    }
                }

            } else {
                eventUUID = eventController.createEvent(conferenceUUID, signedInUserUUID, eventName, timeRange, selectedRoomUUID, selectedSpeakersUUIDS);
                eventController.createEventConversation(conferenceUUID, signedInUserUUID, eventUUID);
            }

            // Update event UUID in case it has changed
            eventFormDialog.setEventUUID(eventUUID);
            eventFormDialog.setUpdated(true);

            // Close the dialog so it isn't blocking anymore
            eventFormDialog.close();
        } catch (DateTimeParseException e) {
            IDialog dateTimeParseErrorDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                    put("title", "Error");
                    put("message", String.format("Unable to submit form: Invalid date. Please follow the given format. [%s]", dateParser.getFormat()));
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

        } catch (SpeakerDoubleBookingException | CalendarDoubleBookingException | InvalidEventTimeException e) {
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
        Set<UUID> signedInUserUUIDs = userController.getUsers();

        IDialog chooseSpeakersDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MULTI_USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select speakers for this event");
                put("availableUserUUIDs", signedInUserUUIDs);
                put("selectedUserUUIDs", selectedSpeakersUUIDS);
            }
        });

        Set<UUID> newSelectedUserUUIDs = (Set<UUID>) chooseSpeakersDialog.run();

        if (newSelectedUserUUIDs != null) {
            selectedSpeakersUUIDS = newSelectedUserUUIDs;
        }
    }


    void selectRoom() {
        Set<UUID> availableRoomUUIDS = roomController.getRooms(conferenceUUID, signedInUserUUID);

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
