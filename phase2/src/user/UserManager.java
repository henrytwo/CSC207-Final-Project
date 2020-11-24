package user;

import user.exception.NullUserException;

import java.io.Serializable;
import java.util.*;

public class UserManager implements Serializable {

    /**
     * Responsibilities:
     * - Stores a map of UUID -> User objects (i.e. instances of User)
     * - Method to create a user + add it to the map
     * - Method to test a username/password combination + loop through the users to see if there's a match
     * - Method to logout the user
     */

    UUID currentUser;
    private Map<UUID, User> userMap = new HashMap<>();

    /**
     * Returns the full name
     *
     * @param userUUID unique user id
     * @return the full name of the user associated with their unique id
     */
    public String getUserFullName(UUID userUUID) {
        return getUser(userUUID).getName();
    }

    /**
     * Setter for User's first name
     *
     * @param userUUID  unique user id
     * @param firstName user's first name
     */
    public void setUserFirstName(UUID userUUID, String firstName) {
        getUser(userUUID).setFirstName(firstName);
    }

    /**
     * Returns User's first name
     *
     * @param userUUID unique user id
     * @return c
     */
    public String getUserFirstName(UUID userUUID) {
        return getUser(userUUID).getFirstName();
    }

    /**
     * Setter for user's last name
     *
     * @param userUUID unique user id
     * @param lastName user's last name
     */
    public void setUserLastName(UUID userUUID, String lastName) {
        getUser(userUUID).setLastName(lastName);
    }

    /**
     * Returns the user's last name
     *
     * @param userUUID unique user id
     * @return the last name of the user associated with their unique id
     */
    public String getUserLastName(UUID userUUID) {
        return getUser(userUUID).getLastName();
    }

    /**
     * Setter for user's username
     *
     * @param userUUID unique user id
     * @param username user's username
     */
    public void setUserUsername(UUID userUUID, String username) {
        getUser(userUUID).setUsername(username);
    }

    /**
     * Returns the user's username
     *
     * @param userUUID unique user id
     * @return user's username associated with their unique user id
     */
    public String getUserUsername(UUID userUUID) {
        return getUser(userUUID).getUsername();
    }

    /**
     * Returns true iff the user has god mode
     *
     * @param userUUID unique user id
     * @return true iff use has god mode
     */
    public boolean getUserIsGod(UUID userUUID) {
        return getUser(userUUID).getIsGod();
    }

    /**
     * Setter for user's password
     *
     * @param userUUID unique user id
     * @param password password of the user
     */
    public void setUserPassword(UUID userUUID, String password) {
        getUser(userUUID).setPassword(password);
    }

    /**
     * Returns the user based on their id
     *
     * @param uuid unique user id
     * @return the User based on their unique id
     */
    private User getUser(UUID uuid) {
        if (userMap.get(uuid) == null) {
            throw new NullUserException(uuid);
        }
        return userMap.get(uuid);
    }

    /**
     * Returns a user
     *
     * @param username of the user
     * @return the user given their username
     */
    private User getUserByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Method for registering a new user
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param username  user name of the user
     * @param password  password of the user
     * @param isGod     whether this user has god mode
     * @return the unique user id of the registered user
     */
    public UUID registerUser(String firstName, String lastName, String username, String password, boolean isGod) {
        if (getUserByUsername(username) == null) {
            User newUser = new User(firstName, lastName, username, password, isGod);
            UUID newUserUUID = newUser.getUuid();

            userMap.put(newUserUUID, newUser);
            currentUser = newUserUUID;

            return newUserUUID;
        }

        return null;
    }

    /**
     * Method for registering a new non-god user
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param username  user name of the user
     * @param password  password of the user
     * @return the unique user id of the registered user
     */
    public UUID registerUser(String firstName, String lastName, String username, String password) {
        return registerUser(firstName, lastName, username, password, false);
    }

    /**
     * Method for login of already registered users
     *
     * @param username user name of the user
     * @param password password of the user
     * @return the user id of the user of the credentials match
     */
    public UUID login(String username, String password) {
        User user = getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            currentUser = user.getUuid();
            return user.getUuid();
        }

        return null;
    }

    /**
     * Sets the current user to null when logged out
     */
    public void clearCurrentUser() {
        currentUser = null;
    }

    /**
     * Returns the id of a user
     *
     * @return the UUID of a user
     */
    public UUID getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns a set of UUID's of all users
     *
     * @return a set of all users registered
     */
    public Set<UUID> getAllUsers() {
        return new HashSet<>(userMap.keySet());
    }

}

