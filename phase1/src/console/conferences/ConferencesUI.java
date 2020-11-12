package console.conferences;

import console.ConsoleUtilities;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import user.UserController;

public class ConferencesUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();

    UserController userController;
    RoomController roomController;
    EventController eventController;
    ConferenceController conferenceController;

    public ConferencesUI(UserController userController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {
        this.userController = userController;
        this.roomController = roomController;
        this.eventController = eventController;
        this.conferenceController = conferenceController;
    }

    public void createConference() {

    }

    public void joinConference() {

    }

    public void viewMyConferences() {

    }

    public void run() {
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
