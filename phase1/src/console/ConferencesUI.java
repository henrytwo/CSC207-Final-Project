package console;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import user.UserController;

public class ConferencesUI {
    ConsoleUtilities consoleUtilities = new ConsoleUtilities();

    public ConferencesUI(UserController userController, RoomController roomController, EventController eventController, ConferenceController conferenceController) {

    }

    public void run() {

        consoleUtilities.confirmBoxClear("This should be the conferences menu");
    }
}
