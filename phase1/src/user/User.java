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
    private UUID uuid;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uuid = UUID.randomUUID();
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

    public UUID getUuid() {
        return uuid;
    }
}



