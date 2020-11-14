package console;

import user.UserController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class ConsoleUtilities {

    private static Scanner stdin = new Scanner(System.in);
    private static final String os = System.getProperty("os.name");
    private UserController userController;

    String dateTimeFormat = "MM-dd-yyyy HH:mm";
    //for room
    String locationFormat = "Building code followed by room number (AB123)";

    public ConsoleUtilities(UserController userController) {
        this.userController = userController;
    }

    /**
     * Displays a prompt and allows operator to select any number of users
     *
     * @param instructions instructions displayed at the top of the menu
     * @param userUUIDs    set of UUID of users that should be available to be picked
     * @return set of chosen user UUIDs
     */
    public Set<UUID> userPicker(String instructions, Set<UUID> userUUIDs) {
        Set<UUID> availableUserUUIDs = new HashSet<>(userUUIDs);
        Set<UUID> selectedUserUUIDs = new HashSet<>();
        ArrayList<String> selectedUserNames = new ArrayList<>();

        /**
         * TODO: Update this with the full metadata table
         */

        while (true) {
            // Don't include users that we've already selected
            availableUserUUIDs.removeAll(selectedUserUUIDs);

            // Now, we'll grab the user's names and generate the menu options
            ArrayList<UUID> orderedAvailableUserUUIDs = new ArrayList<>(availableUserUUIDs);
            String[] options = new String[orderedAvailableUserUUIDs.size() + 2];

            for (int i = 0; i < availableUserUUIDs.size(); i++) {
                options[i] = userController.getUserFullName(orderedAvailableUserUUIDs.get(i));
            }

            // The last two options are the back button
            options[availableUserUUIDs.size()] = "<DONE>";
            options[availableUserUUIDs.size() + 1] = "<CANCEL>";

            String preCaption = selectedUserNames.size() > 0
                    ? "Selected Users: " + String.join(", ", selectedUserNames)
                    : "Selected Users: None";

            // Arrays start at 0, so subtract 1
            int selection = singleSelectMenu(preCaption, instructions, options) - 1;

            if (selection == availableUserUUIDs.size()) { // Finish creation
                boolean confirm = booleanSelectMenu("Are you sure you want to select these users?\n" + preCaption);

                if (confirm) {
                    return selectedUserUUIDs;
                }
            } else if (selection == availableUserUUIDs.size() + 1) { // Cancel
                return null;
            } else {
                // Store the user's name
                selectedUserNames.add(options[selection]);

                // Add user to list
                selectedUserUUIDs.add(orderedAvailableUserUUIDs.get(selection));
            }
        }
    }


    /**
     * Generic prompt to pick a UUID from metadata.
     *
     * @param instructions  string with instructions for this menu
     * @param uuids         set of UUIDs to pick from
     * @param fetchMetadata anonymous function called to fetch metadata associated with each UUID
     * @return UUID of the selected conference. Null if the user makes no selection.
     */
    public UUID singleUUIDPicker(String instructions, Set<UUID> uuids, Function<UUID, String> fetchMetadata) {
        /**
         * TODO: Update this to the more detailed table to show more metadata
         */

        // Convert to array so that UUIDs have order
        List<UUID> orderedUUIDs = new ArrayList<>(uuids);
        String[] options = new String[orderedUUIDs.size() + 1];

        // Back button
        options[orderedUUIDs.size()] = "Back";

        for (int i = 0; i < orderedUUIDs.size(); i++) {
            UUID selectedUUID = orderedUUIDs.get(i);
            options[i] = fetchMetadata.apply(selectedUUID);
        }

        // Arrays start a 0, so subtract
        int selectionIndex = singleSelectMenu(instructions, options) - 1;

        if (selectionIndex < orderedUUIDs.size()) {
            return orderedUUIDs.get(selectionIndex);
        } else {
            return null; // Back button was pressed
        }
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
     * @param a a 2D Arraylist of Strings
     * @return a text table for the 2D array for display
     */
    public String twoDArrayToTable(ArrayList<ArrayList<String>> a) {
        StringBuilder table = new StringBuilder();
        int width = a.get(0).size() * 21 +1;
        StringBuilder topLine = new StringBuilder();
        StringBuilder bottomLine = new StringBuilder();
        topLine.append("╔");
        bottomLine.append("╚");
        for(int i = 0; i < width - 2 ; i++){
            topLine.append("-");
            bottomLine.append("-");
        }
        topLine.append("╗");
        bottomLine.append("╝");
        topLine.append("\r\n");
        bottomLine.append("\r\n");
        table.append(topLine);
        for(ArrayList<String> sub: a) {
            StringBuilder row = new StringBuilder();
            for(String s: sub) {
                row.append(String.format("║%-20s", s));
            }
            row.append("║\r\n");
            table.append(row);
        }
        table.append(bottomLine);
        return table.toString();
    }

    /**
     * Date time format used by the system
     *
     * @return
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Room location format used by the system
     */
    public String getRoomLocationFormat() {
        return locationFormat;
    }

    /**
     * Method to convert a string to a LocalDateTime object
     *
     * @param inputDateTime string to convert
     * @return date time object
     */
    public LocalDateTime stringToDateTime(String inputDateTime) {
        return stringToDateTime(inputDateTime, dateTimeFormat);
    }

    /**
     * Method to convert a string to a LocalDateTime object
     *
     * @param inputDateTime string to convert
     * @param template      date format template
     * @return date time object
     */
    public LocalDateTime stringToDateTime(String inputDateTime, String template) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(template);
        return LocalDateTime.parse(inputDateTime, formatter);
    }

    /**
     * Displays a generic input form and returns a hashmap with the user's responses
     *
     * @param title    Title displayed at the top of the form
     * @param labels   Hashmap from fieldID -> Label
     * @param fieldIDs list of field IDs in the order to be displayed
     * @return hashmap with the user input to the form (fieldID -> response)
     */
    public HashMap<String, String> inputForm(String title, Map<String, String> labels, String[] fieldIDs) {
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
        Map<String, String> labels = new HashMap<String, String>() {
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
        Map<String, String> labels = new HashMap<String, String>() {
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
     *
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
     * Displays a menu with options in the order of selectionIDs, with the labels being the key value of the mapping
     * from the selectionID to the label hashmap.
     * <p>
     * Returning a string with the selection ID allows for advanced options, such as dynamically changing the order of the options.
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param caption            String to be used as the title
     * @param selectionIDs       String Array with the ID of the selections to be displayed
     * @param selectionIDToLabel Hashmap with mappings from selection ID to the label to be displayed
     * @return String with the selected selectionID
     */
    public String singleSelectMenu(String caption, String[] selectionIDs, HashMap<String, String> selectionIDToLabel) {
        return singleSelectMenu(getUserFirstNamePrecaption(), caption, selectionIDs, selectionIDToLabel, true);
    }

    /**
     * Displays a menu with options in the order of selectionIDs, with the labels being the key value of the mapping
     * from the selectionID to the label hashmap.
     * <p>
     * Returning a string with the selection ID allows for advanced options, such as dynamically changing the order of the options.
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param preCaption         text to be displayed before the menu box
     * @param caption            String to be used as the title
     * @param selectionIDs       String Array with the ID of the selections to be displayed
     * @param selectionIDToLabel Hashmap with mappings from selection ID to the label to be displayed
     * @return String with the selected selectionID
     */
    public String singleSelectMenu(String preCaption, String caption, String[] selectionIDs, HashMap<String, String> selectionIDToLabel) {
        return singleSelectMenu(preCaption, caption, selectionIDs, selectionIDToLabel, true);
    }

    /**
     * Displays a menu with options in the order of selectionIDs, with the labels being the key value of the mapping
     * from the selectionID to the label hashmap.
     * <p>
     * Returning a string with the selection ID allows for advanced options, such as dynamically changing the order of the options.
     * <p>
     * Courtesy of Henry Tu (github.com/henrytwo)
     *
     * @param preCaption         text to be displayed before the menu box
     * @param caption            String to be used as the title
     * @param selectionIDs       String Array with the ID of the selections to be displayed
     * @param selectionIDToLabel Hashmap with mappings from selection ID to the label to be displayed
     * @param clear              Whether to clear screen before menu is displayed
     * @return String with the selected selectionID
     */
    public String singleSelectMenu(String preCaption, String caption, String[] selectionIDs, HashMap<String, String> selectionIDToLabel, boolean clear) {
        // Generates the options by fetching the labels used on the selectionIDs
        String[] options = new String[selectionIDs.length];

        for (int i = 0; i < selectionIDs.length; i++) {
            options[i] = selectionIDToLabel.get(selectionIDs[i]);
        }

        int selection = singleSelectMenu(preCaption, caption, options, clear);
        return selectionIDs[selection - 1]; // Arrays start at 0
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

            System.out.println("╚════╩══════════════════════════════════════════════════════════════════════════════════════════════════════    public String twoDArrayToTable(ArrayList<ArrayList<String>> a) {\n" +
                    "        StringBuilder table = new StringBuilder();\n" +
                    "        int width = a.get(0).size() * 21 +1;\n" +
                    "        StringBuilder topLine = new StringBuilder();\n" +
                    "        StringBuilder bottomLine = new StringBuilder();\n" +
                    "        topLine.append(\"╔\");\n" +
                    "        bottomLine.append(\"╚\");\n" +
                    "        for(int i = 0; i < width - 2 ; i++){\n" +
                    "            topLine.append(\"-\");\n" +
                    "            bottomLine.append(\"-\");\n" +
                    "        }\n" +
                    "        topLine.append(\"╗\");\n" +
                    "        bottomLine.append(\"╝\");\n" +
                    "        topLine.append(\"\\r\\n\");\n" +
                    "        bottomLine.append(\"\\r\\n\");\n" +
                    "        table.append(topLine);\n" +
                    "        for(ArrayList<String> sub: a) {\n" +
                    "            StringBuilder row = new StringBuilder();\n" +
                    "            for(String s: sub) {\n" +
                    "                row.append(String.format(\"║%-20s\", s));\n" +
                    "            }\n" +
                    "            row.append(\"║\\r\\n\");\n" +
                    "            table.append(row);\n" +
                    "        }\n" +
                    "        table.append(bottomLine);\n" +
                    "        return table.toString();\n" +
                    "    }");
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
            clearConsole();

            System.out.println(caption);
            System.out.print("[Enter Selection] [y/n]> ");

            try {
                char selection = stdin.nextLine().toLowerCase().charAt(0);
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
