package user;

import java.util.*;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;


public class User {

    /**
     * Responsibilities:
     * - Store name, email, password, UUID
     */

    private String name;
    private String email;
    private String password;
    // List<User> user = new ArrayList<User>();
    private Set<UUID> userUUIDs = new HashSet<>();
    private UUID uuid;

    public User(String name, String email, UUID userUUID) {
        this.name = name;
        this.email = email;
        this.uuid = UUID.randomUUID();
        this.userUUIDs.add(userUUID);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

//    public int getSimpleHash(String password){

//    }

    public UUID getUuid() {
        return uuid;
    }

    boolean isUser(UUID userUUID) {
        return userUUIDs.contains(userUUID);
    }

    public Set<UUID> getUserUUIDs() {
        return userUUIDs;
    }

    public void addUser(UUID userUUID) {
        userUUIDs.add(userUUID);
    }

}



