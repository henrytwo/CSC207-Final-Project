package console.conferences;

import console.ConsoleUtilities;
import convention.RoomController;
import convention.exception.InvalidCapacityException;
import convention.exception.InvalidNameException;
import user.UserController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * UI components for creating and managing rooms
 */
public class RoomUI {

    ConsoleUtilities consoleUtilities;
    RoomController roomController;
    UserController userController;

    /**
     * Constructs RoomUI
     *
     * @param userController
     * @param roomController
     */
    RoomUI(UserController userController, RoomController roomController) {
        consoleUtilities = new ConsoleUtilities(userController);
        this.userController = userController;
        this.roomController = roomController;
    }

    /**
     * Create a room in this conference
     *
     * @param conferenceUUID UUID of the selected conference
     */
    void createRoom(UUID conferenceUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();

        String[] fieldIDs = {
                "roomLocation",
                "capacity"
        };

        Map<String, String> labels = new HashMap<String, String>() {
            {
                put("roomLocation", String.format("Room Location [%s]", consoleUtilities.getRoomLocationFormat()));
                put("capacity", "Room capacity");
            }
        };

        try {
            Map<String, String> response = consoleUtilities.inputForm("Create New Room", labels, fieldIDs);

            // Parses input
            String roomLocation = response.get("roomLocation");
            String capacityStr = response.get("capacity");
            //convert the input string for capacity into an int
            int capacity = Integer.parseInt(capacityStr);

            UUID newRoomUUID = roomController.createRoom(conferenceUUID, signedInUserUUID, roomLocation, capacity);

            consoleUtilities.confirmBoxClear("Successfully created new room.");

            /**
             * TODO: Open new room? It's technically not a requirement so...
             */

        } catch (InvalidNameException e) {
            consoleUtilities.confirmBoxClear("Unable to create Room: Invalid name. Room name must be non empty.");
        } catch (InvalidCapacityException e) {
            consoleUtilities.confirmBoxClear("Unable to create Room: Invalid room capacity. Please enter a number greater than zero.");
        }
    }

    /**
     * Displays a list of rooms and fetches relevant metadata.
     * <p>
     * Allows user to select conference to operate on.
     *
     * @param instructions   string with instructions for this menu
     * @param rooms          set of room UUIDs
     * @param conferenceUUID UUID of the conference to fetch rooms from
     * @return UUID of the selected room. Null if the user makes no selection.
     */
    UUID roomPickerMenu(String instructions, Set<UUID> rooms, UUID conferenceUUID) {
        UUID signedInUserUUID = userController.getCurrentUser();
        Function<UUID, String> fetchRoomMetadata = roomUUID -> "Location: " + roomController.getRoomLocation(conferenceUUID, signedInUserUUID, roomUUID) + " | Capacity: " + roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, roomUUID);

        return consoleUtilities.singleUUIDPicker(instructions, rooms, fetchRoomMetadata);
    }
}
