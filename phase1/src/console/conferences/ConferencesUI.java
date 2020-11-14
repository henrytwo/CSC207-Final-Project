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
     * Create a room in this conference
     *
     * @param conferenceUUID UUID of the selected conference
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
     * Create a event in this conference
     *
     * @param conferenceUUID UUID of the selected conference
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
     * View a list of all conferences a user is affiliated with. Prompts the user to pick one and navigates them to that conference.
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
     * View a list of all events for a conference. Prompts the user to pick one and navigates them to that event.
     *
     * @param conferenceUUID UUID of the selected conference
     * @param eventUUIDs     set of UUIDs of events to display in the list
     * @param emptyListText  text displayed if no results are found
     * @param title          text displayed at the top
     */
    private void selectEvent(UUID conferenceUUID, Set<UUID> eventUUIDs, String emptyListText, String title) {
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
     * @return UUID of the selected room. Null if the user makes no selection.
     */
    private UUID roomPickerMenu(String instructions, Set<UUID> rooms, UUID conferenceUUID) {
        Function<UUID, String> fetchRoomMetadata = roomUUID -> "Location: " + roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID) + " | Capacity: " + roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID);

        return consoleUtilities.singleUUIDPicker(instructions, rooms, fetchRoomMetadata);
    }

    /**
     * Fetch metadata about an event
     *
     * @param conferenceUUID UUID of the conference to fetch events from
     * @param eventUUID      UUID of the event
     * @return Event metadata formatted as a string
     */
    private String getEventMetadata(UUID conferenceUUID, UUID eventUUID) {
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
    private UUID eventPickerMenu(String instructions, Set<UUID> events, UUID conferenceUUID) {
        Function<UUID, String> fetchEventMetadata = eventUUID -> getEventMetadata(conferenceUUID, eventUUID);

        return consoleUtilities.singleUUIDPicker(instructions, events, fetchEventMetadata);
    }

    /**
     * Create a conversation with any number of users in this conference
     *
     * @param conferenceUUID UUID of the selected conference
     * @param availableUsers Set of UUID of users who should be available to add to the conversation
     */
    private void messageUsers(UUID conferenceUUID, Set<UUID> availableUsers) {

        // Remove the current user from the list, since you can't message yourself
        availableUsers.remove(signedInUserUUID);

        if (availableUsers.size() == 0) {
            consoleUtilities.confirmBoxClear("No users available.");
        } else {
            // Add the current user as a user in the convo
            Set<UUID> conversationUserUUIDs = new HashSet<>();
            conversationUserUUIDs.add(signedInUserUUID);

            Set<UUID> pickedUserUUIDs = consoleUtilities.userPicker("Choose users to create a conversation with", availableUsers);

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
    }

    /**
     * UI Menu for specific conference
     *
     * @param conferenceUUID UUID of conference to view
     */
    private void viewSpecificConference(UUID conferenceUUID) {

        String conferenceName = conferenceController.getConferenceName(conferenceUUID);
        int numEvents;
        int numRooms;
        int numAttendees;

        // We store a mapping from the menu ID to the label
        // For each permission, we'll set out a different list of selection IDs, and then generate the selection menu using that
        HashMap<String, String> selectionIDToLabel = new HashMap<String, String>() {
            {
                put("yourAttendeeEvents", "View Events you're registered to");
                put("yourSpeakerEvents", "View Events you're speaking at");
                put("selectEvent", "View all Events");
                put("createEvent", "Create Event");
                put("createRoom", "Create Room");
                put("messageUsers", "Message Users");
                put("back", "Back");
            }
        };

        String[] attendeeSelectionIDs = {
                "yourAttendeeEvents",
                "selectEvent",
                "back"
        };

        String[] speakerSelectionIDs = new String[]{
                "yourAttendeeEvents",
                "yourSpeakerEvents",
                "selectEvent",
                "back"
        };

        String[] organizerSelectionIDs = new String[]{
                "yourAttendeeEvents",
                "yourSpeakerEvents",
                "selectEvent",
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

        while (true) {
            // These statistics may change every loop, so we'll update them here
            numEvents = eventController.getEvents(conferenceUUID, signedInUserUUID).size();
            numRooms = roomController.getRooms(conferenceUUID, signedInUserUUID).size();
            numAttendees = conferenceController.getAttendees(conferenceUUID, signedInUserUUID).size();

            // We use the selection ID here instead of just the option index, as it may change with more or less options
            String selectionID = consoleUtilities.singleSelectMenu(String.format("Conference: %s | Role: %s | # Events: %d | # Rooms: %d | # Attendees: %d", conferenceName, role, numEvents, numRooms, numAttendees), selectionIDs, selectionIDToLabel);

            switch (selectionID) {
                case "yourAttendeeEvents":
                    Set<UUID> attendeeEventUUIDs = eventController.getAttendeeEvents(conferenceUUID, signedInUserUUID);
                    selectEvent(conferenceUUID, attendeeEventUUIDs, "You are currently not registered to any events.", "Events you registered for");
                    break;
                case "yourSpeakerEvents":
                    Set<UUID> speakerEventUUIDs = eventController.getSpeakerEvents(conferenceUUID, signedInUserUUID);
                    selectEvent(conferenceUUID, speakerEventUUIDs, "You are currently not scheduled to speak at any events.", "Events you're speaking at");
                    break;
                case "selectEvent":
                    Set<UUID> eventUUIDs = eventController.getEvents(conferenceUUID, signedInUserUUID);
                    selectEvent(conferenceUUID, eventUUIDs, "No events available.", "All events");
                    break;
                case "createEvent":
                    createEvent(conferenceUUID);
                    break;
                case "createRoom":
                    createRoom(conferenceUUID);
                    break;
                case "messageUsers":
                    messageUsers(conferenceUUID, conferenceController.getUsers(conferenceUUID, signedInUserUUID)); // Fetch all the users in the conference
                    break;
                case "back":
                    return;
            }
        }
    }

    /**
     * Create a conversation for this event. This conversation is linked to the event and will automatically update
     * as users register and unregister.
     *
     * @param conferenceUUID UUID of conference event belongs to
     * @param eventUUID      UUID of event to operate on
     */
    private void openEventConversation(UUID conferenceUUID, UUID eventUUID) {
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

    private void registerForEvent(UUID conferenceUUID, UUID eventUUID) {

        try {
            eventController.registerForEvent(conferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
            consoleUtilities.confirmBoxClear("You have successfully registered for this event.");
        } catch (FullRoomException e) {
            consoleUtilities.confirmBoxClear("Sorry, you can't register for this event since the room is full.");
        }
    }

    private void unregisterFromEvent(UUID conferenceUUID, UUID eventUUID) {
        eventController.unregisterForEvent(conferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
        consoleUtilities.confirmBoxClear("You have successfully unregistered from this event.");
    }

    /**
     * UI menu for specific event
     *
     * @param conferenceUUID UUID of conference event belongs to
     * @param eventUUID      UUID of event to view
     */
    private void viewSpecificEvent(UUID conferenceUUID, UUID eventUUID) {
        // We store a mapping from the menu ID to the label
        // For each permission, we'll set out a different list of selection IDs, and then generate the selection menu using that
        HashMap<String, String> selectionIDToLabel = new HashMap<String, String>() {
            {
                put("register", "Register for this event");
                put("unregister", "Unregister from this event");
                put("eventConversation", "Event conversation");
                put("viewAttendees", "View/Message event attendees");
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
                "viewAttendees",
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
                case "viewAttendees":
                    messageUsers(conferenceUUID, eventController.getEventAttendees(conferenceUUID, signedInUserUUID, eventUUID));
                    break;
                case "back":
                    return;
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
