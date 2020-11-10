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
    private String userfirstName;
    private String userlastName;
    private String useruserName;
    private String userpassword;

    public void setUserFirstName(String firstName) {
        this.userfirstName = firstName;
    }

    public String getUserFirstName() {
        return this.userfirstName;
    }

    public void setUserLastName(String lastName) {
        this.userlastName = lastName;
    }

    public String getUserLastName() {
        return this.userlastName;
    }

    public void setUserUsername(String username) {
        this.useruserName = username;
    }

    public String getUserUsername() {
        return this.useruserName;
    }

    /*
    public void setUserPassword(String password){
        this.userpassword = password;
    }*/

    // Example
    public void setUserPassword(UUID userUUID, String password){
        getUser(userUUID).setPassword(password);
    }

    // We should never do this, so don't let people grab user's passwords
    /*
    public String getPassword(){
        return this.userpassword;
    }*/

    // We make this private because we should only be able to grab the user object from within the manager
    private User getUser(UUID uuid){
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

