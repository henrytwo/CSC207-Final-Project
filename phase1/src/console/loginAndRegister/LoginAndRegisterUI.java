package console.loginAndRegister;

import console.ConsoleUtilities;
import user.UserController;

import java.util.HashMap;
import java.util.Map;

public class LoginAndRegisterUI {

    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    UserController userController;

    public LoginAndRegisterUI(UserController userController) {
        this.userController = userController;
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

    public boolean register() {

        /**
         * TODO: Remove test code here
         */
        userController.registerUser("Test", "Testerson", "test", "password");
        userController.logout();

        consoleUtilities.confirmBoxClear("Ok ur new cred is:\nUsername: test\nPassword: password");

        return false;
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

            int selection = consoleUtilities.singleSelectMenu("LinkedIn Clone", options);

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
