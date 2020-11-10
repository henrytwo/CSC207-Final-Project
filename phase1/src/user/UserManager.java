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

    public static void main(String[] args) {
        Scanner input = new Scanner (System.in);

        String username;
        String password;


        System.out.println("Welcome to your Event booking site!");
        System.out.println("\n Enter your username and password to login to your account.");

        System.out.println("Username: ");
        username = input.nextLine();

        System.out.println("Password: ");
        password = input.nextLine();

        UserController login = new UserController(username, password);

        /*
        if(login.isLoginCorrect(username, password))
            System.out.println("You are logged in!");
        else
            System.out.println("The username and password you entered are incorrect.");*/
    }

    }

