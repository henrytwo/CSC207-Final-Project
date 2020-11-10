package user;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    /**
     * Responsibilities:
     * - Stores a map of UUID -> User objects (i.e. instances of User)
     * - Method to create a user + add it to the map
     * - Method to test a username/password combination + loop through the users to see if there's a match
     */

    private Map<UUID, User> userMap = new HashMap<>();

    /*
   public boolean isUsernameTaken(UUID username){
        return userMap.containsKey(username);
    }

    public void registerUser(UUID username, User password){
        User passwordHash = getSimpleHash(password);
        userMap.put(username, passwordHash);
    }

    public boolean isLoginCorrect(UUID username, User password) {

        //username isn't registered
        if (!userMap.containsKey(username)) {
            return false;
        }
        for (User user : userMap.values()) {

            User passwordHash = getSimpleHash(password);
            User storedPasswordHash = userMap.get(username);

            return passwordHash == storedPasswordHash;
        }
    }*/

    // Add method to get name of user by UUID, get first name, get last name, etc.

    private User getUserByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public boolean registerUser(String firstName, String lastName, String username, String password) {
        if (getUserByUsername(username) == null) {
            User newUser = new User(firstName, lastName, username, password);
            UUID newUserUUID = newUser.getUuid();

            userMap.put(newUserUUID, newUser);

            return true;
        }

        return false;
    }

    public UUID login(String username, String password) {
        User user = getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user.getUuid();
        }

        return null;
    }
}

