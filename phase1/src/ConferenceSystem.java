import user.UserController;

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

        // write some test code here, or write unit tests somewhere else

        UserController userController = new UserController();

        userController.login("asdasd", "asd"); //this should return a boolean

        //userController.register();
        // userController.getCurrentUserUUID();
    }
}
