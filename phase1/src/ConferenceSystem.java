import console.ConsoleUtilities;

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

        System.out.println(ConsoleUtilities.loginPrompt());
        ConsoleUtilities.confirmBoxClear("wtf ur password is wrong");
        ConsoleUtilities.singleSelectMenu("Welcome to our boi", "Cool system man", new String[]{
                "asd",
                "asdasdas",
                "asdsad"
        });
    }
}
