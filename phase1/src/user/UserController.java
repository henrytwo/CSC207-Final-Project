package user;

import java.util.Set;
import java.util.UUID;

public class UserController {

    /**
     * Responsibilities:
     * <p>
     * - Stores an instance of UserManager
     * - Handles login (i.e. tags in a username + password pair and returns whether or not the login was successful
     * - Saves a copy of the logged in user's UUID in UserController
     * <p>
     * - Getter for the logged in user's UUID
     * <p>
     * - Handles logout (i.e. erases the UUID of the logged in user)
     * - Handles account registration (i.e. given a username + password pair, create a user)
     */

    UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setUserFirstName(UUID userUUID, String firstName) {
        userManager.setUserFirstName(userUUID, firstName);
    }

    public String getUserFullName(UUID userUUID) {
        return userManager.getUserFullName(userUUID);
    }

    public String getUserFirstName(UUID userUUID) {
        return userManager.getUserFirstName(userUUID);
    }

    public void setUserLastName(UUID userUUID, String lastName) {
        userManager.setUserLastName(userUUID, lastName);
    }

    public String getUserLastName(UUID userUUID) {
        return userManager.getUserLastName(userUUID);
    }

    public void setUserUsername(UUID userUUID, String username) {
        userManager.setUserUsername(userUUID, username);
    }

    public String getUserUsername(UUID userUUID) {
        return userManager.getUserUsername(userUUID);
    }

    public void setUserPassword(UUID userUUID, String password) {
        userManager.setUserPassword(userUUID, password);
    }

    public UUID registerUser(String firstName, String lastName, String username, String password) {
        return userManager.registerUser(firstName, lastName, username, password);
    }

    public UUID login(String username, String password) {
        return userManager.login(username, password);
    }

    public void logout() {
        userManager.clearCurrentUser();
    }

    public UUID getCurrentUser() {
        return userManager.getCurrentUser();
    }

    public Set<UUID> getUsers(){
        return userManager.getAllUsers();
    }

}

