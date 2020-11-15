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
     * Displays a list of conferences and fetches relevant metadata.
     * <p>
     * Allows user to select conference to operate on.
     *
     * @param instructions string with instructions for this menu
     * @param conferences  set of conference UUIDs
     * @return UUID of the selected conference. Null if the user makes no selection.
     */
    private UUID conferencePickerMenu(String instructions, Set<UUID> conferences) {
        Function<UUID, String> fetchConferenceMetadata = conferenceUUID -> conferenceController.getConferenceName(conferenceUUID) + " " + conferenceController.getConferenceTimeRange(conferenceUUID);

        return consoleUtilities.singleUUIDPicker(instructions, conferences, fetchConferenceMetadata);
    }

    /**
     * UI Menu for specific conference
     *
     * @param conferenceUUID UUID of conference to view
     */
    private void viewSpecificConference(UUID conferenceUUID) {

        RoomUI roomUI = new RoomUI(userController, roomController);
        EventUI eventUI = new EventUI(userController, eventController, roomController, conferenceController);
        ConferenceMessageUI conferenceMessageUI = new ConferenceMessageUI(conferenceController, userController);

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
                put("messageUsers", "View/Message Users");
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
                    eventUI.selectEvent(conferenceUUID, attendeeEventUUIDs, "You are currently not registered to any events.", "Events you registered for");
                    break;
                case "yourSpeakerEvents":
                    Set<UUID> speakerEventUUIDs = eventController.getSpeakerEvents(conferenceUUID, signedInUserUUID);
                    eventUI.selectEvent(conferenceUUID, speakerEventUUIDs, "You are currently not scheduled to speak at any events.", "Events you're speaking at");
                    break;
                case "selectEvent":
                    Set<UUID> eventUUIDs = eventController.getEvents(conferenceUUID, signedInUserUUID);
                    eventUI.selectEvent(conferenceUUID, eventUUIDs, "No events available.", "All events");
                    break;
                case "createEvent":
                    eventUI.createEvent(conferenceUUID);
                    break;
                case "createRoom":
                    roomUI.createRoom(conferenceUUID);
                    break;
                case "messageUsers":
                    conferenceMessageUI.messageUsers(conferenceUUID, conferenceController.getUsers(conferenceUUID, signedInUserUUID)); // Fetch all the users in the conference
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
