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

    public boolean login(String username, String password) {
        // call the user manager code to login
        // user manager will return the uuid of the user if successful

        // set currentUser = that userUID
        // return true;

        return true;
    }

    public UUID getCurrentUser() {
        return currentUser;
    }


}

