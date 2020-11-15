package user;

import java.io.Serializable;
import java.util.UUID;


public class User implements Serializable {

    /**
     * Responsibilities:
     * - Store name, username, password, UUID
     */

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UUID uuid;

    /**
     * Contructor for User
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param username  username the user wants to keep
     * @param password  password of the user
     */
    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.uuid = UUID.randomUUID();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns first name of the user
     *
     * @return the firstname associated with this user
     */
    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns last name of the user
     *
     * @return the lastname associated with this user
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Returns the full name of the user
     *
     * @return the full name of the user by combining first and last name
     */
    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the user
     *
     * @return the username of the associated user
     */
    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * Returns UUID of the user
     *
     * @return the UUID associated with this user
     */
    public UUID getUuid() {
        return uuid;
    }
}



