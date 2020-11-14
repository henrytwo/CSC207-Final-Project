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
import java.util.*;
import java.util.function.Function;

public class ConferencesUI {
    ConsoleUtilities consoleUtilities;

    UserController userController;
    RoomController roomController;
    EventController eventController;
    ConferenceController conferenceController;
    UUID signedInUserUUID;

    /**
     * Creates the ConferenceUI
     *
     * @param userController
     * @param roomController
     * @param eventController
     * @param conferenceController
     */
    public ConferencesUI(UserController userController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;
        this.roomController = roomController;
        this.eventController = eventController;
        this.conferenceController = conferenceController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    /**
     * Create a new conference
     */
    private void createConference() {
        String[] fieldIDs = {
                "conferenceName",
                "startTime",
                "endTime"
        };

        Map<String, String> labels = new HashMap<String, String>() {
            {
                put("conferenceName", "Conference Name");
                put("startTime", String.format("Start Time/Date [%s]", consoleUtilities.getDateTimeFormat()));
                put("endTime", String.format("End Time/Date [%s]", consoleUtilities.getDateTimeFormat()));
            }
        };

        try {
            Map<String, String> response = consoleUtilities.inputForm("Create New Conference", labels, fieldIDs);

            // Parses input
            String conferenceName = response.get("conferenceName");
            LocalDateTime start = consoleUtilities.stringToDateTime(response.get("startTime"));
            LocalDateTime end = consoleUtilities.stringToDateTime(response.get("endTime"));
            TimeRange timeRange = new TimeRange(start, end);

            UUID newConferenceUUID = conferenceController.createConference(conferenceName, timeRange, signedInUserUUID);

            consoleUtilities.confirmBoxClear("Successfully created new conference.");

            viewSpecificConference(newConferenceUUID);
        } catch (InvalidNameException e) {
            consoleUtilities.confirmBoxClear("Unable to create Conference: Invalid name. Conference name must be non empty.");
        } catch (InvalidTimeRangeException e) {
            consoleUtilities.confirmBoxClear("Unable to create Conference: Invalid date range. End time must be after start time.");
        } catch (DateTimeParseException e) {
            consoleUtilities.confirmBoxClear(String.format("Unable to create Conference: Invalid date. Please follow the given format. [%s]", consoleUtilities.getDateTimeFormat()));
        }
    }

    /**
     * Join an existing conference (that you're not part of, of course)
     */
    private void joinConference() {
        Set<UUID> notMyConferences = conferenceController.getNotUserConferences(signedInUserUUID);

        if (notMyConferences.size() == 0) {
            consoleUtilities.confirmBoxClear("There aren't any conferences you can join.");
        } else {
            UUID selectedConferenceUUID = conferencePickerMenu("Select a conference to join", notMyConferences);

            if (selectedConferenceUUID != null) {

                conferenceController.addAttendee(selectedConferenceUUID, signedInUserUUID);

                consoleUtilities.confirmBoxClear("Successfully joined conference.");

                viewSpecificConference(selectedConferenceUUID);
            }
        }
    }

    /**
     * Displays menu with conferences the user is affiliated with.
     */
    private void selectConference() {
        Set<UUID> myConferences = conferenceController.getUserConferences(signedInUserUUID);

        if (myConferences.size() == 0) {
            consoleUtilities.confirmBoxClear("You are currently not affiliated with any conferences.");
        } else {
            UUID selectedConferenceUUID = conferencePickerMenu("Select a conference to view", myConferences);

            if (selectedConferenceUUID != null) {
                viewSpecificConference(selectedConferenceUUID);
            }
        }
    }

    /**
     * Displays a list of conferences and fetches relevant metadata.
     * <p>
     * Allows user to select conference to operate on.
     *
     * @param instructions string with instructions for this menu
     * @param conferences  set of conference UUIDs
     * @return UUID of the selected conference. Null if the user makes no selection.
     */
    private UUID conferencePickerMenu(String instructions, Set<UUID> conferences) {
        Function<UUID, String> fetchRoomMetadata = conferenceUUID -> conferenceController.getConferenceName(conferenceUUID) + " " + conferenceController.getConferenceTimeRange(conferenceUUID);

        return consoleUtilities.singleUUIDPicker(instructions, conferences, fetchRoomMetadata);
    }

    /**
     * Displays a list of rooms and fetches relevant metadata.
     * <p>
     * Allows user to select conference to operate on.
     *
     * @param instructions   string with instructions for this menu
     * @param rooms          set of room UUIDs
     * @param conferenceUUID UUID of the conference to fetch rooms from
     * @return UUID of the selected conference. Null if the user makes no selection.
     */
    private UUID roomPickerMenu(String instructions, Set<UUID> rooms, UUID conferenceUUID) {
        Function<UUID, String> fetchRoomMetadata = roomUUID -> "Location: " + roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID) + " | Capacity: " + roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID);

        return consoleUtilities.singleUUIDPicker(instructions, rooms, fetchRoomMetadata);
    }


    /**
     * Displays a list of events and fetches relevant metadata.
     * <p>
     * Allows user to select event to operate on.
     *
     * @param instructions   string with instructions for this menu
     * @param events         set of event UUIDs
     * @param conferenceUUID UUID of the conference to fetch events from
     * @return UUID of the selected conference. Null if the user makes no selection.
     */
    private UUID selectEvent(String instructions, Set<UUID> events, UUID conferenceUUID) {
        Function<UUID, String> fetchEventMetadata = eventUUID -> {

            String eventTitle = eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID);
            String eventTimeRange = eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).toString();
            int eventNumBooking = eventController.getNumRegistered(conferenceUUID, signedInUserUUID, eventUUID);

            UUID eventRoomUUID = eventController.getEventRoom(conferenceUUID, signedInUserUUID, eventUUID);
            String eventRoomLocation = roomController.getRoomLocation(conferenceUUID, signedInUserUUID, eventRoomUUID);
            int eventRoomCapacity = roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, eventRoomUUID);

            return String.format("Name: %s | Time: %s | Location: %s | [%d / %d]", eventTitle, eventTimeRange, eventRoomLocation, eventNumBooking, eventRoomCapacity);
        };

        return consoleUtilities.singleUUIDPicker(instructions, events, fetchEventMetadata);
    }

    /**
     * View a list of all events for a conference
     *
     * @param conferenceUUID
     */
    private void viewEvents(UUID conferenceUUID, Set<UUID> eventUUIDs, String emptyListText, String title) {
        if (eventUUIDs.size() == 0) {
            consoleUtilities.confirmBoxClear(emptyListText);
        } else {
            UUID selectedEventUUID = selectEvent("Select an event to view | " + title, eventUUIDs, conferenceUUID);

            if (selectedEventUUID != null) {
                viewSpecificEvent(conferenceUUID, selectedEventUUID);
            }
        }
    }

    /**
     * Create a event in this conference
     *
     * @param conferenceUUID
     */
    private void createEvent(UUID conferenceUUID) {

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
        UUID roomUUID = roomPickerMenu("Select a room to host this event in", roomController.getRooms(conferenceUUID, signedInUserUUID), conferenceUUID);

        // Both of these indicate the user wants to quit
        if (roomUUID == null) {
            return;
        }

        // Fetch the speakers to add to the room
        Set<UUID> speakerUUIDs = consoleUtilities.userPicker("Select speakers for this room", conferenceController.getUsers(conferenceUUID, signedInUserUUID));

        // Both of these indicate the user wants to quit
        if (speakerUUIDs == null) {
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

    /**
     * Create a conversation with any number of users in this conference
     *
     * @param conferenceUUID
     */
    private void messageUsers(UUID conferenceUUID) {

        // Remove the current user from the list, since you can't message yourself
        Set<UUID> conferenceUserUUIDs = conferenceController.getUsers(conferenceUUID, signedInUserUUID);
        conferenceUserUUIDs.remove(signedInUserUUID);

        // Add the current user as a user in the convo
        Set<UUID> conversationUserUUIDs = new HashSet<>();
        conversationUserUUIDs.add(signedInUserUUID);

        Set<UUID> pickedUserUUIDs = consoleUtilities.userPicker("Choose users to add to the conversation", conferenceUserUUIDs);

        // Null means the user want to cancel
        if (pickedUserUUIDs != null) {
            // Choose users to add to the convo
            conversationUserUUIDs.addAll(pickedUserUUIDs);

            // Actually create the conversation
            UUID newConversationUUID = conferenceController.createConversationWithUsers(conferenceUUID, signedInUserUUID, conversationUserUUIDs);
            consoleUtilities.confirmBoxClear(String.format("New conversation created with %d users (including you)", conversationUserUUIDs.size()));

            /**
             * TODO: Open the new conversation?
             */
        }
    }

    /**
     * Create a room in this conference
     * @param conferenceUUID
     */
    public void createRoom(UUID conferenceUUID) {
        String[] fieldIDs = {
                "roomLocation",
                "capacity"
        };

        Map<String, String> labels = new HashMap<String, String>() {
            {
                put("roomLocation", String.format("Room Location [%s]", consoleUtilities.getRoomLocationFormat()));
                put("capacity", "Room capacity");
            }
        };

        try {
            Map<String, String> response = consoleUtilities.inputForm("Create New Room", labels, fieldIDs);

            // Parses input
            String roomLocation = response.get("roomLocation");
            String capacityStr = response.get("capacity");
            //convert the input string for capacity into an int
            int capacity = Integer.parseInt(capacityStr);

            UUID newRoomUUID = roomController.createRoom(conferenceUUID, signedInUserUUID, roomLocation, capacity);

            consoleUtilities.confirmBoxClear("Successfully created new room.");

        } catch (InvalidNameException e) {
            consoleUtilities.confirmBoxClear("Unable to create Room: Invalid name. Room name must be non empty.");
        } catch (InvalidCapacityException e) {
            consoleUtilities.confirmBoxClear("Unable to create Room: Invalid room capacity. Please enter a number greater than zero.");
        }
    }

    /**
     * UI menu for specific event
     *
     * @param conferenceUUID UUID of conference event belongs to
     * @param eventUUID      UUID of event to view
     */
    private void viewSpecificEvent(UUID conferenceUUID, UUID eventUUID) {
        consoleUtilities.confirmBoxClear("Awesome, you selected " + eventUUID);
    }

    /**
     * UI Menu for specific conference
     *
     * @param conferenceUUID UUID of conference to view
     */
    private void viewSpecificConference(UUID conferenceUUID) {

        /**
         * Need a way to hide options in menu
         *
         * Display general information at the top of the menu
         *  - View your events (Attendee)
         *  - View your events (Spepaker)
         *  - View all events
         *      - <this is the event menu now>
         *          - DISPLAY GENERAL EVENT INFORMATION
         *          - Register for event/Unregister from event
         *          - Create an event conversation/Open event conversation (Speaker)
         *  - Create Event (Organizer)
         *
         *  - View rooms
         *      - View room schedule
         *      - Delete room
         *  - Create Room
         *
         *  - Organizer settings
         *    - Manage users
         *      - Create conversation
         *        - Delete conference
         */

        String conferenceName = conferenceController.getConferenceName(conferenceUUID);
        int numEvents;
        int numRooms;
        int numAttendees;

        // We store a mapping from the menu ID to the label
        // For each permission, we'll set out a different list of selection IDs, and then generate the selection menu using that
        HashMap<String, String> selectionIDToLabel = new HashMap<String, String>() {
            {
                put("yourAttendeeEvents", "View Registered Events");
                put("yourSpeakerEvents", "[Speaker] View Registered Events");
                put("viewEvents", "View all Events");
                put("createEvent", "[Organizer] Create Event");
                put("createRoom", "[Organizer] Create Room");
                put("messageUsers", "[Organizer] Message Users");
                put("back", "Back");
            }
        };

        String[] attendeeSelectionIDs = {
                "yourAttendeeEvents",
                "viewEvents",
                "back"
        };

        String[] speakerSelectionIDs = new String[]{
                "yourAttendeeEvents",
                "yourSpeakerEvents",
                "viewEvents",
                "back"
        };

        String[] organizerSelectionIDs = new String[]{
                "yourAttendeeEvents",
                "yourSpeakerEvents",
                "viewEvents",
                "createEvent",
                "createRoom",
                "messageUsers",
                "back"
        };

        // Displays the menu options based on permission
        boolean isSpeaker = conferenceController.isSpeaker(conferenceUUID, signedInUserUUID, signedInUserUUID);
        boolean isOrganizer = conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);

        String role;
        String[] selectionIDs;

        if (isOrganizer) {
            role = "Organizer";
            selectionIDs = organizerSelectionIDs;
        } else if (isSpeaker) {
            role = "Speaker";
            selectionIDs = speakerSelectionIDs;
        } else {
            role = "Attendee";
            selectionIDs = attendeeSelectionIDs;
        }

        /* Alright, lets get these labels ready */
        String[] options = new String[selectionIDs.length];

        for (int i = 0; i < selectionIDs.length; i++) {
            options[i] = selectionIDToLabel.get(selectionIDs[i]);
        }

        boolean running = true;

        while (running) {
            // These statistics may change every loop, so we'll update them here
            numEvents = eventController.getEvents(conferenceUUID, signedInUserUUID).size();
            numRooms = roomController.getRooms(conferenceUUID, signedInUserUUID).size();
            numAttendees = conferenceController.getAttendees(conferenceUUID, signedInUserUUID).size();

            // We use the selection ID here instead of just the option index, as it may change with more or less options
            int selection = consoleUtilities.singleSelectMenu(String.format("Conference: %s | Role: %s | # Events: %d | # Rooms: %d | # Attendees: %d", conferenceName, role, numEvents, numRooms, numAttendees), options);
            String selectionID = selectionIDs[selection - 1]; // Arrays start at 0

            switch (selectionID) {
                case "yourAttendeeEvents":
                    Set<UUID> attendeeEventUUIDs = eventController.getAttendeeEvents(conferenceUUID, signedInUserUUID);
                    viewEvents(conferenceUUID, attendeeEventUUIDs, "You are currently not registered to any events.", "Events you registered for");
                    break;
                case "yourSpeakerEvents":
                    Set<UUID> speakerEventUUIDs = eventController.getSpeakerEvents(conferenceUUID, signedInUserUUID);
                    viewEvents(conferenceUUID, speakerEventUUIDs, "You are currently not scheduled to speak at any events.", "Events you're speaking at");
                    break;
                case "viewEvents":
                    Set<UUID> eventUUIDs = eventController.getEvents(conferenceUUID, signedInUserUUID);
                    viewEvents(conferenceUUID, eventUUIDs, "No events available.", "All events");
                    break;
                case "createEvent":
                    createEvent(conferenceUUID);
                    break;
                case "createRoom":
                    createRoom(conferenceUUID);
                    break;
                case "messageUsers":
                    messageUsers(conferenceUUID);
                    break;
                case "back":
                    running = false;
            }
        }
    }

    /**
     * Run conference UI
     */
    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.signedInUserUUID = userController.getCurrentUser();

        String[] options = new String[]{
                "Create a conference",
                "Join a conference",
                "View your conferences",
                "Back"
        };

        boolean running = true;

        while (running) {
            int selection = consoleUtilities.singleSelectMenu("Conference Options", options);

            switch (selection) {
                case 1:
                    createConference();
                    break;
                case 2:
                    joinConference();
                    break;
                case 3:
                    selectConference();
                    break;
                case 4:
                    running = false;
            }
        }
    }
}
