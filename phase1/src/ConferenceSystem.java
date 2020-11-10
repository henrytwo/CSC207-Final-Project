import console.ConsoleUtilities;

import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceSystem {
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void run() {
        // Setup logger
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        UUID currentUser = UUID.randomUUID();

        // add option to serialize the current user's UUID too
        // "stay logged in", you know

        while (true) {
            if (currentUser == null) {
                // ConsoleUtilities.loginPrompt()
                // handle login and account creation here
            } else {
                String[] options = new String[]{
                        "asd",
                        "asdasdas",
                        "asdsad"
                };

                int selection = ConsoleUtilities.singleSelectMenu("Welcome to our boi", "Cool system man", options);

                switch (selection) {
                    case 1:
                        ConsoleUtilities.confirmBoxClear("u made the wrong choice");
                        break;
                    default:
                        ConsoleUtilities.confirmBoxClear("hi there stranger");
                }
            }
        }
    }
}
