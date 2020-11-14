package console.conferences;

import console.ConsoleUtilities;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.calendar.TimeRange;
import convention.exception.InvalidNameException;
import convention.exception.InvalidTimeRangeException;
import convention.exception.SpeakerDoubleBookingException;
import user.UserController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

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
            consoleUtilities.confirmBoxClear("Invalid name. Conference name must be non empty.");
        } catch (InvalidTimeRangeException e) {
            consoleUtilities.confirmBoxClear("Invalid date range. End time must be after start time.");
        } catch (DateTimeParseException e) {
            consoleUtilities.confirmBoxClear(String.format("Invalid date. Please follow the given format. [%s]", consoleUtilities.getDateTimeFormat()));
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
            UUID selectedConferenceUUID = conferencePicker("Select a conference to join", notMyConferences);

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
    private void viewMyConferences() {
        Set<UUID> myConferences = conferenceController.getUserConferences(signedInUserUUID);

        if (myConferences.size() == 0) {
            consoleUtilities.confirmBoxClear("You are currently not affiliated with any conferences.");
        } else {
            UUID selectedConferenceUUID = conferencePicker("Select a conference to view", myConferences);

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
    private UUID conferencePicker(String instructions, Set<UUID> conferences) {

        /**
         * TODO: Update this to the more detailed table to show more metadata
         */

        // Now we have the UUIDs in order
        List<UUID> conferenceUUIDs = new ArrayList<>(conferences);

        // Time to grab the conference names
        String[] options = new String[conferenceUUIDs.size() + 1];

        // Back button
        options[conferenceUUIDs.size()] = "Back";

        for (int i = 0; i < conferenceUUIDs.size(); i++) {
            UUID conferenceUUID = conferenceUUIDs.get(i);
            options[i] = conferenceController.getConferenceName(conferenceUUID) + " " + conferenceController.getConferenceTimeRange(conferenceUUID);
        }

        // Arrays start a 0, so subtract
        int selectionIndex = consoleUtilities.singleSelectMenu(instructions, options) - 1;

        if (selectionIndex < conferenceUUIDs.size()) {
            return conferenceUUIDs.get(selectionIndex);
        } else {
            return null; // Back button was pressed
        }
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
    private UUID roomPicker(String instructions, Set<UUID> rooms, UUID conferenceUUID) {

        /**
         * TODO: Update this to the more detailed table to show more metadata
         */

        // Now we have the UUIDs in order
        List<UUID> roomUUIDs = new ArrayList<>(rooms);

        // Time to grab the conference names
        String[] options = new String[roomUUIDs.size() + 1];

        // Back button
        options[roomUUIDs.size()] = "Back";

        for (int i = 0; i < roomUUIDs.size(); i++) {
            UUID roomUUID = roomUUIDs.get(i);
            options[i] = "Location: " + roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID) + " | Capacity: " + roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID);
        }

        // Arrays start a 0, so subtract
        int selectionIndex = consoleUtilities.singleSelectMenu(instructions, options) - 1;

        if (selectionIndex < roomUUIDs.size()) {
            return roomUUIDs.get(selectionIndex);
        } else {
            return null; // Back button was pressed
        }
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
    private UUID eventPicker(String instructions, Set<UUID> events, UUID conferenceUUID) {

        /**
         * TODO: Update this to the more detailed table to show more metadata
         */

        // Now we have the UUIDs in order
        List<UUID> eventUUIDs = new ArrayList<>(events);

        // Time to grab the conference names
        String[] options = new String[eventUUIDs.size() + 1];

        // Back button
        options[eventUUIDs.size()] = "Back";

        for (int i = 0; i < eventUUIDs.size(); i++) {
            UUID eventUUID = eventUUIDs.get(i);
            options[i] = "Name: " + eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID) + " | Time: " + eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID);
        }

        // Arrays start a 0, so subtract
        int selectionIndex = consoleUtilities.singleSelectMenu(instructions, options) - 1;

        if (selectionIndex < eventUUIDs.size()) {
            return eventUUIDs.get(selectionIndex);
        } else {
            return null; // Back button was pressed
        }
    }

    /**
     * View a list of all events for a conference
     *
     * @param conferenceUUID
     */
    private void viewEvents(UUID conferenceUUID) {
        Set<UUID> eventUUIDs = eventController.getEvents(conferenceUUID, signedInUserUUID);

        UUID selectedEventUUID = eventPicker("All Events", eventUUIDs, conferenceUUID);

        if (selectedEventUUID != null) {
            /**
             * TODO: Open up a menu for this event
             */
        }
    }

    /**
     * View a list of all events for a conference (Ones that this user has registered with as an attendee)
     *
     * @param conferenceUUID
     */
    private void viewAttendeeRegisteredEvents(UUID conferenceUUID) {
        Set<UUID> eventUUIDs = eventController.getAttendeeEvents(conferenceUUID, signedInUserUUID);

        if (eventUUIDs.size() == 0) {
            consoleUtilities.confirmBoxClear("You are currently not registered to any events.");
        } else {
            UUID selectedEventUUID = eventPicker("Events you're registered in", eventUUIDs, conferenceUUID);

            if (selectedEventUUID != null) {
                /**
                 * TODO: Open up a menu for this event
                 */
            }
        }
    }

    /**
     * View a list of all events for a conference (Ones that this user has registered with as an speaker)
     *
     * @param conferenceUUID
     */
    private void viewSpeakerRegisteredEvents(UUID conferenceUUID) {
        Set<UUID> eventUUIDs = eventController.getSpeakerEvents(conferenceUUID, signedInUserUUID);

        if (eventUUIDs.size() == 0) {
            consoleUtilities.confirmBoxClear("You are currently not scheduled to speak at any events.");
        } else {
            UUID selectedEventUUID = eventPicker("Events you're speaking at ", eventUUIDs, conferenceUUID);

            if (selectedEventUUID != null) {
                /**
                 * TODO: Open up a menu for this event
                 */
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
        UUID roomUUID = roomPicker("Select a room to host this event in", roomController.getRooms(conferenceUUID, signedInUserUUID), conferenceUUID);

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

            /**
             * TODO: Open up the new event
             */
        } catch (InvalidNameException e) {
            consoleUtilities.confirmBoxClear("Invalid name. Event name must be non empty.");
        } catch (InvalidTimeRangeException e) {
            consoleUtilities.confirmBoxClear("Invalid date range. End time must be after start time.");
        } catch (DateTimeParseException e) {
            consoleUtilities.confirmBoxClear(String.format("Invalid date. Please follow the given format. [%s]", consoleUtilities.getDateTimeFormat()));
        } catch (SpeakerDoubleBookingException e) {
            consoleUtilities.confirmBoxClear("One or more speakers are not available at the selected time.");
        } catch (CalendarDoubleBookingException e) {
            consoleUtilities.confirmBoxClear("The room is not available at the selected time.");
        }
    }

    /**
     * Create a room in this conference
     *
     * @param conferenceUUID
     */
    private void createRoom(UUID conferenceUUID) {

        /**
         * TODO: Remove test code
         */
        // Create two mock rooms
        roomController.createRoom(conferenceUUID, signedInUserUUID, "BA6969", 2);
        roomController.createRoom(conferenceUUID, signedInUserUUID, "BA6970", 2);

        consoleUtilities.confirmBoxClear("two test rooms created");
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

    /**
     * Create a room in this conference
     * @param conferenceUUID
     */
    public void createRoom(UUID conferenceUUID) {

        /**
         * TODO: Remove test code
         */
        // Create two mock rooms
        roomController.createRoom(conferenceUUID, signedInUserUUID, "BA6969", 2);
        roomController.createRoom(conferenceUUID, signedInUserUUID, "BA6970", 2);

        consoleUtilities.confirmBoxClear("two test rooms created");
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
                    viewAttendeeRegisteredEvents(conferenceUUID);
                    break;
                case "yourSpeakerEvents":
                    viewSpeakerRegisteredEvents(conferenceUUID);
                    break;
                case "viewEvents":
                    viewEvents(conferenceUUID);
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
                    viewMyConferences();
                    break;
                case 4:
                    running = false;
            }
        }
    }
}
