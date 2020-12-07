package user;

import java.io.Serializable;
import java.util.UUID;

/**
 * Stores name, username, password, UUID of a user
 */
class User implements Serializable {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UUID uuid;

    // God mode users are organizers for all conferences
    private boolean isGod;

    /**
     * Constructor for User
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param username  username the user wants to keep
     * @param password  password of the user
     * @param isGod     whether a user is a god
     */
    User(String firstName, String lastName, String username, String password, boolean isGod) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.uuid = UUID.randomUUID();
        this.isGod = isGod;
    }

    /**
     * Setter for first name
     *
     * @param firstName
     */
    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns first name of the user
     *
     * @return the firstname associated with this user
     */
    String getFirstName() {
        return this.firstName;
    }

    /**
     * Setter for last name
     *
     * @param lastName
     */
    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns last name of the user
     *
     * @return the lastname associated with this user
     */
    String getLastName() {
        return this.lastName;
    }

    /**
     * Returns the full name of the user
     *
     * @return the full name of the user by combining first and last name
     */
    String getName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Setter for username
     *
     * @param username
     */
    void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the user
     *
     * @return the username of the associated user
     */
    String getUsername() {
        return this.username;
    }

    /**
     * Setter for password
     *
     * @param password
     */
    void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password of the user
     *
     * @return the password of the associated user
     */
    String getPassword() {
        return this.password;
    }

    /**
     * Returns UUID of the user
     *
     * @return the UUID associated with this user
     */
    UUID getUuid() {
        return uuid;
    }

    /**
     * Returns if this user is god
     *
     * @return true iff this user is god
     */
    boolean getIsGod() {
        return isGod;
    }
}



