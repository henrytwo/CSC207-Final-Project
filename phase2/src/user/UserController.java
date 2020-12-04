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

    /**
     * Creating an instance of the UserManager class
     *
     * @param userManager instance of the class
     */
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Setter for user's first name
     *
     * @param userUUID  unique user id
     * @param firstName user's first name
     */
    public void setUserFirstName(UUID userUUID, String firstName) {
        userManager.setUserFirstName(userUUID, firstName);
    }

    /**
     * Returns true iff the user has god mode
     *
     * @param userUUID unique user id
     * @return true iff use has god mode
     */
    public boolean getUserIsGod(UUID userUUID) {
        return userManager.getUserIsGod(userUUID);
    }

    /**
     * Checks if a UUID belongs to a valid user
     *
     * @param userUUID UUID of user to check
     * @return true iff the UUID corresponds to a valid user
     */
    public boolean isUser(UUID userUUID) {
        return userManager.isUser(userUUID);
    }

    /**
     * Returns the full name
     *
     * @param userUUID unique user id
     * @return full name of the user
     */
    public String getUserFullName(UUID userUUID) {
        return userManager.getUserFullName(userUUID);
    }

    /**
     * Returns the first name
     *
     * @param userUUID unique user id
     * @return first name of the user
     */
    public String getUserFirstName(UUID userUUID) {
        return userManager.getUserFirstName(userUUID);
    }

    /**
     * Setter for last name of the User
     *
     * @param userUUID unique user id
     * @param lastName user's last name
     */
    public void setUserLastName(UUID userUUID, String lastName) {
        userManager.setUserLastName(userUUID, lastName);
    }

    /**
     * Returns the last name
     *
     * @param userUUID unique user id
     * @return the last lastname of the user
     */
    public String getUserLastName(UUID userUUID) {
        return userManager.getUserLastName(userUUID);
    }

    /**
     * Setter for username
     *
     * @param userUUID unique user id
     * @param username user's username
     */
    public void setUserUsername(UUID userUUID, String username) {
        userManager.setUserUsername(userUUID, username);
    }

    /**
     * Returns the username
     *
     * @param userUUID unique user id
     * @return the username of the User
     */
    public String getUserUsername(UUID userUUID) {
        return userManager.getUserUsername(userUUID);
    }

    /**
     * Setter for password
     *
     * @param userUUID unique user id
     * @param password user's password
     */
    public void setUserPassword(UUID userUUID, String password) {
        userManager.setUserPassword(userUUID, password);
    }

    /**
     * Method for registering the user
     *
     * @param firstName user's first name
     * @param lastName  user's last name
     * @param username  user's username
     * @param password  user's password
     * @return user id of the registered user
     */
    public UUID registerUser(String firstName, String lastName, String username, String password) {
        return userManager.registerUser(firstName, lastName, username, password);
    }

    /**
     * Method for user login
     *
     * @param username user's username
     * @param password user's password
     * @return the user id of the logged in User
     */
    public UUID login(String username, String password) {
        return userManager.login(username, password);
    }

    /**
     * Logouts the user
     */
    public void logout() {
        userManager.clearCurrentUser();
    }

    /**
     * Returns the UUID
     *
     * @return UUID of the logged in user
     */
    public UUID getCurrentUser() {
        return userManager.getCurrentUser();
    }

    /**
     * Returns a list of users
     *
     * @return set of registered Users
     */
    public Set<UUID> getUsers() {
        return userManager.getAllUsers();
    }

}

