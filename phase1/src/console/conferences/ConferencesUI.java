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

            String conferenceName = response.get("conferenceName");
            LocalDateTime start = consoleUtilities.stringToDateTime(response.get("startTime"));
            LocalDateTime end = consoleUtilities.stringToDateTime(response.get("endTime"));

            TimeRange timeRange = new TimeRange(start, end);

            conferenceController.createConference(conferenceName, timeRange, userUUID);
        } catch (InvalidNameException e) {
            consoleUtilities.confirmBoxClear("Invalid name. Conference name must be non empty.");
        } catch (InvalidTimeRangeException e) {
            consoleUtilities.confirmBoxClear("Invalid date range. End time must be after start time.");
        } catch (DateTimeParseException e) {
            consoleUtilities.confirmBoxClear(String.format("Invalid date. Please follow the given format. [%s]", consoleUtilities.getDateTimeFormat()));
        }
    }

    public void joinConference() {
        Set<UUID> allConferences = conferenceController.getNotUserConferences(userUUID);

        // don't show them conferences they're already in... duh

    }

    public void viewSpecificConference(UUID conferenceUUID) {

        /**
         * Display general information at the top of the menu
         *  - Get event calendar
         *  - View your events (Attendee)
         *  - View your speaker events
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
    }

    public void viewMyConferences() {

        Set<UUID> myConferences = conferenceController.getUserConferences(userUUID);

        if (myConferences.size() == 0) {
            consoleUtilities.confirmBoxClear("You are currently not affiliated with any conferences.");
        } else {
            // Now we have the UUIDs in order
            List<UUID> conferenceUUIDs = new ArrayList<>(myConferences);

            // Time to grab the conference names
            String[] conferenceNames = new String[conferenceUUIDs.size()];

            for (int i = 0; i < conferenceUUIDs.size(); i++) {
                UUID conferenceUUID = conferenceUUIDs.get(i);
                conferenceNames[i] = conferenceController.getConferenceName(conferenceUUID) + " " + conferenceController.getConferenceTimeRange(conferenceUUID);
            }

            // Arrays start a 0, so subtract
            int selectionIndex = consoleUtilities.singleSelectMenu("Select a conference", conferenceNames) - 1;

            UUID selectedConferenceUUID = conferenceUUIDs.get(selectionIndex);

            viewSpecificConference(selectedConferenceUUID);
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
