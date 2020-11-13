package console;

import user.UserController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleUtilities {

    private static Scanner stdin = new Scanner(System.in);
    private static final String os = System.getProperty("os.name");
    private UserController userController;

    public ConsoleUtilities(UserController userController) {
        this.userController = userController;
    }

    /**
     * Clears the console
     * <p>
     * There's a special solution for Windows...
     */
    public void clearConsole() {
        /*if (os.contains("Windows")) {
            for(int i = 0; i < 100; i++) {
                System.out.println("");
            }
        }
        else {
            // This doesn't work in intelliJ :/
            System.out.print("\033[H\033[2J");
        }*/

        for (int i = 0; i < 100; i++) {
            System.out.println("");
        }
    }

    /**
     * Displays a generic input form and returns a hashmap with the user's responses
     *
     * @param title Title displayed at the top of the form
     * @param labels Hashmap from fieldID -> Label
     * @param fieldIDs list of field IDs in the order to be displayed
     * @return hashmap with the user input to the form (fieldID -> response)
     */
    public HashMap<String, String> inputForm(String title, HashMap<String, String> labels, String[] fieldIDs) {
        clearConsole();

        HashMap<String, String> responses = new HashMap<>();

        if (title.length() > 0) {
            System.out.println(String.format("%s\n", title));
        }

        for (String fieldID : fieldIDs) {
            System.out.printf("%s: ", labels.get(fieldID));
            String response = stdin.nextLine();

            responses.put(fieldID, response);
        }

        return responses;
    }

    /**
     * Displays a login prompt
     *
     * @return hashmap with username and password
     */
    public HashMap<String, String> loginPrompt() {
        HashMap<String, String> labels = new HashMap<>() {
            {
                put("username", "Username");
                put("password", "Password");
            }
        };

        String[] fieldIDs = {
                "username",
                "password"
        };

        return inputForm("Login", labels, fieldIDs);
    }

    public HashMap<String, String> registerPrompt() {
        HashMap<String, String> labels = new HashMap<>() {
            {
                put("username", "Username");
                put("password", "Password");
                put("firstname", "First Name");
                put("lastname", "Last Name");
            }
        };

        String[] fieldIDs = {
                "firstname",
                "lastname",
                "username",
                "password"
        };

        return inputForm("Register", labels, fieldIDs);
    }

    /**
     * Generates the text that goes before a menu that displays the user's name (if logged in)
     * @return
     */
    private String getUserFirstNamePrecaption() {
        if (userController.getCurrentUser() != null) {
            String fullName = userController.getUserFullName(userController.getCurrentUser());

            return String.format("Signed in as %s", fullName);
        }

        return "";
    }

    /**
     * Helper method for singleSelectMenu
     * Generates numerical menu based on String[] of options
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param precaption Caption displayed before menu
     * @param caption    Main Caption displayed on menu
     * @param options    String array with options
     * @return Integer with array index of selected item
     */
    public int singleSelectMenu(String precaption, String caption, String[] options) {
        return singleSelectMenu(precaption, caption, options, true);
    }

    /**
     * Helper method for singleSelectMenu
     * Generates numerical menu based on String[] of options
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param caption Main Caption displayed on menu
     * @param options String array with options
     * @param clear   Whether to clear screen before menu is displayed
     * @return Integer with array index of selected item
     */
    public int singleSelectMenu(String caption, String[] options, boolean clear) {
        return singleSelectMenu(getUserFirstNamePrecaption(), caption, options, clear);
    }

    /**
     * Helper method for singleSelectMenu
     * Generates numerical menu based on String[] of options
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param caption Main Caption displayed on menu
     * @param options String array with options
     * @return Integer with array index of selected item
     */
    public int singleSelectMenu(String caption, String[] options) {
        return singleSelectMenu(getUserFirstNamePrecaption(), caption, options, true);
    }

    /**
     * Displays a menu with n items
     * and returns the index of
     * selected item. Verifies that
     * selection is found on menu.
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param preCaption
     * @param caption    String to be used as the title
     * @param options    String Array with all possible options order of items is maintained with index
     * @param clear      Whether to clear screen before menu is displayed
     * @return Integer with array index of selected item
     */
    public int singleSelectMenu(String preCaption, String caption, String[] options, boolean clear) {

        int selection;

        while (true) {

            if (clear) {
                clearConsole();
            }

            if (preCaption.length() > 0) {
                System.out.println(preCaption);
            }

            /*
             * Displays table with options
             * along with corresponding value
             */
            System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println(String.format("║ %-105s ║", caption));
            System.out.println("╠════╦══════════════════════════════════════════════════════════════════════════════════════════════════════╣");

            for (int i = 0; i < options.length; i++) {
                System.out.println(String.format("║ %-2d ║ %-100s ║", i + 1, options[i]));
            }

            System.out.println("╚════╩══════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            System.out.print("[Enter Selection]> ");

            /*
             * Reads from Scanner to get
             * Input specified by user and
             * validates that item
             * is found on menu.
             */
            try {
                selection = Integer.parseInt(stdin.nextLine());

                if (selection > 0 && selection <= options.length) {
                    return selection;
                } else {
                    confirmBoxClear(String.format("Error: Please enter a valid item from the list <%d-%d>", 1, options.length));
                }
            } catch (Exception e) {
                stdin.nextLine();
                confirmBoxClear(String.format("Error: Please enter a valid item from the list <%d-%d>", 1, options.length));
            }

        }

    }

    /**
     * Displays a caption and presents a [y/n] prompt
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param caption String to be used as Main Caption
     * @return Boolean response to caption
     */
    public boolean booleanSelectMenu(String caption) {
        while (true) {

            System.out.println(caption);
            System.out.println("[Enter Selection]> ");

            try {
                char selection = stdin.next().toLowerCase().charAt(0);
                if (selection == 'y' || selection == 'n') {
                    return selection == 'y';
                } else {
                    confirmBoxClear("Error: Please enter a valid choice [y/n]");
                }
            } catch (Exception e) {
                confirmBoxClear("Error: Please enter a valid choice [y/n]");
                stdin.nextLine();
            }
        }
    }

    /**
     * Helper Method for confirmBox
     * Clears Screen before prompt
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param message String with message
     */
    public void confirmBoxClear(String message) {
        clearConsole();
        confirmBox(message);
    }

    /**
     * Displays message and prompts for confirmation [Enter]
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param message String with message
     */
    public void confirmBox(String message) {
        System.out.println(message);
        System.out.println("\nPress [Enter] to continue");
        try {
            System.in.read();
        } catch (IOException e) {

        }
        clearConsole();
    }
}
