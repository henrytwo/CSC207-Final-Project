package console.conferences;

import console.ConsoleUtilities;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.calendar.TimeRange;
import convention.exception.InvalidNameException;
import convention.exception.InvalidTimeRangeException;
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
    UUID userUUID;

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
    public void createConference() {
        String[] fieldIDs = {
                "conferenceName",
                "startTime",
                "endTime"
        };

        Map<String, String> labels = new HashMap<>() {
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

            UUID newConferenceUUID = conferenceController.createConference(conferenceName, timeRange, userUUID);

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
    public void joinConference() {
        Set<UUID> notMyConferences = conferenceController.getNotUserConferences(userUUID);

        if (notMyConferences.size() == 0) {
            consoleUtilities.confirmBoxClear("There aren't any conferences you can join.");
        } else {
            UUID selectedConferenceUUID = displayConferences("Select a conference to join", notMyConferences);

            if (selectedConferenceUUID != null) {

                conferenceController.addAttendee(selectedConferenceUUID, userUUID);

                consoleUtilities.confirmBoxClear("Successfully joined conference.");

                viewSpecificConference(selectedConferenceUUID);
            }
        }

    }

    /**
     * Displays menu with conferences the user is affiliated with.
     */
    public void viewMyConferences() {
        Set<UUID> myConferences = conferenceController.getUserConferences(userUUID);

        if (myConferences.size() == 0) {
            consoleUtilities.confirmBoxClear("You are currently not affiliated with any conferences.");
        } else {
            UUID selectedConferenceUUID = displayConferences("Select a conference to view", myConferences);

            if (selectedConferenceUUID != null) {
                viewSpecificConference(selectedConferenceUUID);
            }
        }
    }

    /**
     * Displays a list of conferences and fetches relevant metadata
     * <p>
     * Allows user to select conference to operate on.
     *
     * @param instructions string with instructions for this menu
     * @param conferences  set of conference UUIDs
     * @return UUID of the selected conference. Null if the user makes no selection.
     */
    private UUID displayConferences(String instructions, Set<UUID> conferences) {

        /**
         * TODO: Update this to the more detailed table to show more metadata
         */

        // Now we have the UUIDs in order
        List<UUID> conferenceUUIDs = new ArrayList<>(conferences);

        // Time to grab the conference names
        String[] conferenceNames = new String[conferenceUUIDs.size() + 1];

        // Back button
        conferenceNames[conferenceUUIDs.size()] = "Back";

        for (int i = 0; i < conferenceUUIDs.size(); i++) {
            UUID conferenceUUID = conferenceUUIDs.get(i);
            conferenceNames[i] = conferenceController.getConferenceName(conferenceUUID) + " " + conferenceController.getConferenceTimeRange(conferenceUUID);
        }

        // Arrays start a 0, so subtract
        int selectionIndex = consoleUtilities.singleSelectMenu(instructions, conferenceNames) - 1;

        if (selectionIndex < conferenceUUIDs.size()) {
            return conferenceUUIDs.get(selectionIndex);
        } else {
            return null; // Back button was pressed
        }
    }

    public void viewSpecificConference(UUID conferenceUUID) {

        /**
         * Display general information at the top of the menu
         *  - Get event calendar
         *  - View your events (Have a section for attendee events and organizer events)
         *  - View all events
         *      - <this is the event menu now>
         *          - DISPLAY GENERAL EVENT INFORMATION
         *          - Register for event/Unregister from event
         *          - Create an event conversation/Open event conversation (Speaker)
         *          - Delete event
         *  - Create Event (Organizer)
         *
         *  - View rooms
         *      - View room schedule
         *      - Delete room
         *  - Create Room
         *
         *  - Organizer settings
         *    - Manage users
         *      - Add/Remove users
         *      - Promote/Demote organizer
         *      - Create conversation
         *        - Delete conference
         */

        String conferenceName = conferenceController.getConferenceName(conferenceUUID);

        String[] options = new String[]{
                "View Calendar",
                "Your Events",
                "Register for Event",
                "Create Event",
                "View Rooms",
                "Do bad stuff",
                "Back"
        };

        boolean running = true;

        while (running) {
            int selection = consoleUtilities.singleSelectMenu(String.format("Conference: %s", conferenceName), options);

            /**
             * TODO: Finish this menu
             */

            switch (selection) {
                case 1:
                    running = false;
            }
        }
    }

    public void run() {
        // We fetch the user UUID here so we keep it up to date
        this.userUUID = userController.getCurrentUser();

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
