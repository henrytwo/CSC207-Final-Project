package user;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
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

        if(login.isLoginCorrect(username, password))
            System.out.println("You are logged in!");
        else
            System.out.println("The username and password you entered are incorrect.");
    }

    }

