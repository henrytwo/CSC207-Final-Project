package console;

import user.UserController;

import java.util.HashMap;
import java.util.Map;

public class LoginAndRegisterUI {

    ConsoleUtilities consoleUtilities = new ConsoleUtilities();
    UserController userController;

    public LoginAndRegisterUI(UserController userController) {
        this.userController = userController;
    }

    public boolean login() {
        Map<String, String> credentials = consoleUtilities.loginPrompt();

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (userController.login(username, password) != null) {
            return true;
        } else {
            consoleUtilities.confirmBoxClear("Incorrect credentials, please try again.");

            return false;
        }
    }

    public boolean register() {

        return true;
    }

    public boolean run() {
        boolean exit = false;

        while (!exit) {
            consoleUtilities.clearConsole();

            String[] options = new String[]{
                    "Login",
                    "Register",
                    "Exit"
            };

            int selection = consoleUtilities.singleSelectMenu("Welcome to what's arguably the worst LinkedIn clone ever.", options);

            switch (selection) {
                case 1:
                    exit = login();
                    break;
                case 2:
                    exit = register();
                    break;
                case 3:
                    exit = true;
                default:
                    consoleUtilities.confirmBoxClear("An error occurred. Please try again.");
            }
        }
    }
}
