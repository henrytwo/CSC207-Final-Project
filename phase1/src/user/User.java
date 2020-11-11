package user;

import java.util.*;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;


public class User {

    /**
     * Responsibilities:
     * - Store name, username, password, UUID
     */

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UUID uuid;

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

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public UUID getUuid() {
        return uuid;
    }
}



