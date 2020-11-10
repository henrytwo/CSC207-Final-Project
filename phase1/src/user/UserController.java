package user;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private String username;
    private String password;
    boolean active;
    private Map<String, Integer> userMap = new HashMap<>();

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

