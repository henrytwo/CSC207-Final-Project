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

    public String getUserFullName(UUID userUUID) {
        return getUser(userUUID).getName();
    }

    public void setUserFirstName(UUID userUUID, String firstName) {
        getUser(userUUID).setFirstName(firstName);
    }

    public String getUserFirstName(UUID userUUID) {
        return getUser(userUUID).getFirstName();
    }

    public void setUserLastName(UUID userUUID, String lastName) {
        getUser(userUUID).setLastName(lastName);
    }

    public String getUserLastName(UUID userUUID) {
        return getUser(userUUID).getLastName();
    }

    public void setUserUsername(UUID userUUID, String username) {
        getUser(userUUID).setUsername(username);
    }

    public String getUserUsername(UUID userUUID) {
        return getUser(userUUID).getUsername();
    }

    public void setUserPassword(UUID userUUID, String password) {
        getUser(userUUID).setPassword(password);
    }

    private User getUser(UUID uuid) {
        if (userMap.get(uuid) == null) {
            throw new NullUserException(uuid);
        }
        return userMap.get(uuid);
    }

    private User getUserByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public UUID registerUser(String firstName, String lastName, String username, String password) {
        if (getUserByUsername(username) == null) {
            User newUser = new User(firstName, lastName, username, password);
            UUID newUserUUID = newUser.getUuid();

            userMap.put(newUserUUID, newUser);
            currentUser = newUserUUID;

            return newUserUUID;
        }

        return null;
    }

    public UUID login(String username, String password) {
        User user = getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            currentUser = user.getUuid();
            return user.getUuid();
        }

        return null;
    }

    public void clearCurrentUser() {
        currentUser = null;
    }

    public UUID getCurrentUser() {
        return currentUser;
    }

    public Set<UUID> getAllUsers(){
        return new HashSet<>(userMap.keySet());
    }

}

