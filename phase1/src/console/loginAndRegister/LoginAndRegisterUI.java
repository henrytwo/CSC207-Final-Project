package console.loginAndRegister;

import console.ConsoleUtilities;
import user.UserController;

import java.util.Map;

public class LoginAndRegisterUI {

    ConsoleUtilities consoleUtilities;
    UserController userController;

    public LoginAndRegisterUI(UserController userController) {
        this.userController = userController;
        this.consoleUtilities = new ConsoleUtilities(userController);
    }

    /**
     * Presents the login prompt and authenticates with userController
     *
     * @return false iff the UI loop is to break on the next iteration
     */
    public boolean login() {
        Map<String, String> credentials = consoleUtilities.loginPrompt();

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (userController.login(username, password) != null) {
            return false;
        } else {
            consoleUtilities.confirmBoxClear("Incorrect credentials, please try again.");

            return true;
        }
    }

    /**
     * Presents the register prompt and let's the user create an account
     *
     * @return false iff the UI loop os to break on the next iteration
     */
    public boolean register() {

        Map<String, String> credentials = consoleUtilities.registerPrompt();

        String firstname = credentials.get("firstname");
        String lastname = credentials.get("lastname");
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (userController.registerUser(firstname, lastname, username, password) != null) {
            return false;
        } else {
            consoleUtilities.confirmBoxClear("Username is already taken");
            return true;
        }
    }


    /**
     * Run the LoginAndRegisterUI
     * @return false iff the user wants to quit the program
     */
    public boolean run() {
        boolean running = true;

        while (running) {
            consoleUtilities.clearConsole();

            String[] options = new String[]{
                    "Login",
                    "Register",
                    "Exit System"
            };

            int selection = consoleUtilities.singleSelectMenu("Bad LinkedIn Clone | Please authenticate to continue", options);

            switch (selection) {
                case 1:
                    running = login();
                    break;
                case 2:
                    running = register();
                    break;
                case 3: // User wants to Exit System
                    return false;
                default:
                    consoleUtilities.confirmBoxClear("An error occurred. Please try again.");
            }
        }

        return true;
    }
}
