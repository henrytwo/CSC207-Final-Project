package user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserController {

    /**
     * Responsibilities:
     *
     * - Stores an instance of UserManager
     * - Handles login (i.e. tags in a username + password pair and returns whether or not the login was successful
     *   - Saves a copy of the logged in user's UUID in UserController
     *
     * - Getter for the logged in user's UUID
     *
     * - Handles logout (i.e. erases the UUID of the logged in user)
     * - Handles account registration (i.e. given a username + password pair, create a user)
     */

    UUID currentUser;
    UserManager userManager = new UserManager();

    public  boolean registerUser(String firstName, String lastName, String username, String password){
        return userManager.registerUser(firstName, lastName, username, password);
    }

    public boolean login(String username, String password) {
        currentUser = userManager.login(username, password) ;
        return true;
    }

    public UUID getCurrentUser() {
        return currentUser;
    }

}

