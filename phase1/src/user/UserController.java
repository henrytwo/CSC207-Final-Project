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

    private String username;
    private String password;
    boolean active;
    private Map<String, Integer> userMap = new HashMap<>();

    /*
    public UserController(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public boolean isUsernameTaken(String username){
        return userMap.containsKey(username);
    }

    public void registerUser(String username, String password){
//        int passwordHash = getSimpleHash(password);
//        userMap.put(username, passwordHash);
    }

//    public boolean isLoginCorrect(String username, String password) {

        // username isn't registered
//        if(!userMap.containsKey(username)){
//            return false;
//        }

//        int passwordHash = getSimpleHash(password);
//        int storedPasswordHash = userMap.get(username);

//        return passwordHash == storedPasswordHash;
//    }

//    public void deactivateAccount()
//    {
//        boolean active = false;
//    }
}
*/
