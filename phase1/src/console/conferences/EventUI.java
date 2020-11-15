package console.conferences;

import console.ConsoleUtilities;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.calendar.TimeRange;
import convention.exception.*;
import user.UserController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class EventUI {

    ConsoleUtilities consoleUtilities;
    UserController userController;
    EventController eventController;
    ConferenceController conferenceController;
    RoomController roomController;

    public EventUI(UserController userController, EventController eventController, RoomController roomController, ConferenceController conferenceController) {
        this.userController = userController;
        this.eventController = eventController;
        this.roomController = roomController;
        this.conferenceController = conferenceController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    /**
     * Create a event in this conference
     *
     * @param conferenceUUID UUID of the selected conference
     */
    void createEvent(UUID conferenceUUID) {

        UUID signedInUserUUID = userController.getCurrentUser();
        RoomUI roomUI = new RoomUI(userController, roomController);
        Set<UUID> roomUUIDs = roomController.getRooms(conferenceUUID, signedInUserUUID);

        if (roomUUIDs.size() == 0) {
            consoleUtilities.confirmBoxClear("This conference has no rooms. You must create a room before you can create an event.");
        } else {
            String[] fieldIDs = {
                    "eventName",
                    "startTime",
                    "endTime"
            };

            Map<String, String> labels = new HashMap<String, String>() {
                {
                    put("eventName", "Event Name");
                    put("startTime", String.format("Start Time/Date [%s]", consoleUtilities.getDateTimeFormat()));
                    put("endTime", String.format("End Time/Date [%s]", consoleUtilities.getDateTimeFormat()));
                }
            };

            // Fetch core event metadata
            Map<String, String> response = consoleUtilities.inputForm("Create New Event", labels, fieldIDs);

            // Fetch the room to host the event in
            UUID roomUUID = roomUI.roomPickerMenu("Select a room to host this event in", roomUUIDs, conferenceUUID);

            // Both of these indicate the user wants to quit
            if (roomUUID == null) {
                consoleUtilities.confirmBoxClear("Event Creation has been terminated by the user.");
                return;
            }

            // Fetch the speakers to add to the room
            Set<UUID> speakerUUIDs = consoleUtilities.userPicker("Select speakers for this room", conferenceController.getUsers(conferenceUUID, signedInUserUUID));

            // Both of these indicate the user wants to quit
            if (speakerUUIDs == null) {
                consoleUtilities.confirmBoxClear("Event Creation has been terminated by the user.");
                return;
            }

            try {
                // Parses input
                String eventName = response.get("eventName");
                LocalDateTime start = consoleUtilities.stringToDateTime(response.get("startTime"));
                LocalDateTime end = consoleUtilities.stringToDateTime(response.get("endTime"));
                TimeRange timeRange = new TimeRange(start, end);

                UUID newEventUUID = eventController.createEvent(conferenceUUID, signedInUserUUID, eventName, timeRange, roomUUID, speakerUUIDs);

                consoleUtilities.confirmBoxClear("Successfully created new event.");

                viewSpecificEvent(conferenceUUID, newEventUUID);

            } catch (InvalidNameException e) {
                consoleUtilities.confirmBoxClear("Unable to create event: Invalid name. Event name must be non empty.");
            } catch (InvalidTimeRangeException e) {
                consoleUtilities.confirmBoxClear("Unable to create event: Invalid date range. End time must be after start time.");
            } catch (DateTimeParseException e) {
                consoleUtilities.confirmBoxClear(String.format("Invalid date. Please follow the given format. [%s]", consoleUtilities.getDateTimeFormat()));
            } catch (SpeakerDoubleBookingException e) {
                consoleUtilities.confirmBoxClear("Unable to create event: One or more speakers are not available at the selected time.");
            } catch (CalendarDoubleBookingException e) {
                consoleUtilities.confirmBoxClear("Unable to create event: The room is not available at the selected time.");
            }
        }
    }

    /**
     * View a list of all events for a conference. Prompts the user to pick one and navigates them to that event.
     *
     * @param conferenceUUID UUID of the selected conference
     * @param eventUUIDs     set of UUIDs of events to display in the list
     * @param emptyListText  text displayed if no results are found
     * @param title          text displayed at the top
     */
    void selectEvent(UUID conferenceUUID, Set<UUID> eventUUIDs, String emptyListText, String title) {
        if (eventUUIDs.size() == 0) {
            consoleUtilities.confirmBoxClear(emptyListText);
        } else {
            UUID selectedEventUUID = eventPickerMenu("Select an event to view | " + title, eventUUIDs, conferenceUUID);

            if (selectedEventUUID != null) {
                viewSpecificEvent(conferenceUUID, selectedEventUUID);
            }
        }
    }

    /**
     * Fetch metadata about an event
     *
     * @param conferenceUUID UUID of the conference to fetch events from
     * @param eventUUID      UUID of the event
     * @return Event metadata formatted as a string
     */
    String getEventMetadata(UUID conferenceUUID, UUID eventUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();

        String eventTitle = eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID);
        String eventTimeRange = eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).toString();
        int eventNumBooking = eventController.getNumRegistered(conferenceUUID, signedInUserUUID, eventUUID);

        UUID eventRoomUUID = eventController.getEventRoom(conferenceUUID, signedInUserUUID, eventUUID);
        String eventRoomLocation = roomController.getRoomLocation(conferenceUUID, signedInUserUUID, eventRoomUUID);
        int eventRoomCapacity = roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, eventRoomUUID);

        boolean isRegistered = eventController.isRegistered(conferenceUUID, signedInUserUUID, eventUUID);

        return String.format("%s | %s | Location: %s | [%d / %d] | Registered: %s", eventTitle, eventTimeRange, eventRoomLocation, eventNumBooking, eventRoomCapacity, isRegistered ? "Y" : "N");

    }

    /**
     * Displays a list of events and fetches relevant metadata.
     * <p>
     * Allows user to select event to operate on.
     *
     * @param instructions   string with instructions for this menu
     * @param events         set of event UUIDs
     * @param conferenceUUID UUID of the conference to fetch events from
     * @return UUID of the selected event. Null if the user makes no selection.
     */
    UUID eventPickerMenu(String instructions, Set<UUID> events, UUID conferenceUUID) {
        Function<UUID, String> fetchEventMetadata = eventUUID -> getEventMetadata(conferenceUUID, eventUUID);

        return consoleUtilities.singleUUIDPicker(instructions, events, fetchEventMetadata);
    }

    /**
     * Create a conversation for this event. This conversation is linked to the event and will automatically update
     * as users register and unregister.
     *
     * @param conferenceUUID UUID of conference event belongs to
     * @param eventUUID      UUID of event to operate on
     */
    void openEventConversation(UUID conferenceUUID, UUID eventUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();

        String eventTitle = eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID);
        UUID eventConversationUUID = eventController.getEventConversationUUID(conferenceUUID, signedInUserUUID, eventUUID);

        if (eventConversationUUID == null) {
            boolean confirm = consoleUtilities.booleanSelectMenu(String.format("Are you sure you want to create an event conversation for %s?", eventTitle));

            if (confirm) {

                UUID newEventConversationUUID = eventController.createEventConversation(conferenceUUID, signedInUserUUID, eventUUID);

                consoleUtilities.confirmBoxClear(String.format("Successfully created an event conversation for %s", eventTitle));

                /**
                 * TODO: Open the event conversation
                 */
                consoleUtilities.confirmBoxClear("This should open up the event conversation, but that code hasn't been written yet so...");

            } else {
                consoleUtilities.confirmBoxClear("Event conversation creation was cancelled.");
            }
        } else {

            /**
             * TODO: Open the event conversation
             */
            consoleUtilities.confirmBoxClear("This should open up the event conversation, but that code hasn't been written yet so...");
        }
    }

    void registerForEvent(UUID conferenceUUID, UUID eventUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();

        try {
            eventController.registerForEvent(conferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
            consoleUtilities.confirmBoxClear("You have successfully registered for this event.");
        } catch (FullRoomException e) {
            consoleUtilities.confirmBoxClear("Sorry, you can't register for this event since the room is full.");
        }
    }

    void unregisterFromEvent(UUID conferenceUUID, UUID eventUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();
        eventController.unregisterForEvent(conferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
        consoleUtilities.confirmBoxClear("You have successfully unregistered from this event.");
    }

    /**
     * UI menu for specific event
     *
     * @param conferenceUUID UUID of conference event belongs to
     * @param eventUUID      UUID of event to view
     */
    void viewSpecificEvent(UUID conferenceUUID, UUID eventUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();
        ConferenceMessageUI conferenceMessageUI = new ConferenceMessageUI(conferenceController, userController);

        // We store a mapping from the menu ID to the label
        // For each permission, we'll set out a different list of selection IDs, and then generate the selection menu using that
        HashMap<String, String> selectionIDToLabel = new HashMap<String, String>() {
            {
                put("register", "Register for this event");
                put("unregister", "Unregister from this event");
                put("eventConversation", "Event conversation");
                put("messageAttendees", "View/Message event attendees");
                put("back", "Back");
            }
        };

        String[] unregisteredAttendeeSelectionIDs = {
                "register",
                "back"
        };

        String[] registeredAttendeeSelectionIDs = {
                "unregister",
                "back"
        };

        String[] eventSpeakerSelectionIDs = new String[]{
                "messageAttendees",
                "eventConversation",
                "back"
        };

        String[] selectionIDs;

        while (true) {

            boolean isOrganizer = conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);
            boolean isEventSpeaker = eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID).contains(signedInUserUUID);
            boolean isRegistered = eventController.isRegistered(conferenceUUID, signedInUserUUID, eventUUID);

            if (isEventSpeaker || isOrganizer) {
                selectionIDs = eventSpeakerSelectionIDs;
            } else if (isRegistered) {
                selectionIDs = registeredAttendeeSelectionIDs;
            } else {
                selectionIDs = unregisteredAttendeeSelectionIDs;
            }

            String eventMetadata = getEventMetadata(conferenceUUID, eventUUID);

            // We use the selection ID here instead of just the option index, as it may change with more or less options
            String selectionID = consoleUtilities.singleSelectMenu(eventMetadata, selectionIDs, selectionIDToLabel);

            switch (selectionID) {
                case "register":
                    registerForEvent(conferenceUUID, eventUUID);
                    break;
                case "unregister":
                    unregisterFromEvent(conferenceUUID, eventUUID);
                    break;
                case "eventConversation":
                    openEventConversation(conferenceUUID, eventUUID);
                    break;
                case "messageAttendees":
                    conferenceMessageUI.messageUsers(conferenceUUID, eventController.getEventAttendees(conferenceUUID, signedInUserUUID, eventUUID));
                    break;
                case "back":
                    return;
            }
        }
    }
}
