package user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    List<User> user = new ArrayList<User>();

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

}



