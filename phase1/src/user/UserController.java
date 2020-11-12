package user;

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

    UUID currentUser;
    UserManager userManager = new UserManager();

    public void setUserFirstName(String firstName) {
        userManager.setUserFirstName(currentUser, firstName);
        ;
    }

    public String getUserFirstName() {
        return userManager.getUserFirstName(currentUser);
    }

    public void setUserLastName(String lastName) {
        userManager.setUserLastName(currentUser, lastName);
    }

    public String getUserLastName() {
        return userManager.getUserLastName(currentUser);
    }

    public void setUserUsername(String username) {
        userManager.setUserUsername(currentUser, username);
    }

    public String getUserUsername() {
        return userManager.getUserUsername(currentUser);
    }

    public void setUserPassword(String password) {
        userManager.setUserPassword(currentUser, password);
    }

    public UUID registerUser(String firstName, String lastName, String username, String password) {
        currentUser = userManager.registerUser(firstName, lastName, username, password);
        return currentUser;
    }

    public UUID login(String username, String password) {
        currentUser = userManager.login(username, password);
        return currentUser;
    }

    public UUID getCurrentUser() {
        return currentUser;
    }

}

