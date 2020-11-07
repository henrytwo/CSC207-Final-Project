package messaging;

import user.User;

import java.util.ArrayList;

public class Conversation {
    private String conversationName; // either group chat or 2 person chat
    private ArrayList<Message> conversationMessages = new ArrayList<>();
    private ArrayList<User> writeAccessUsers = new ArrayList<>();
    private ArrayList<User> readAccessUsers = new ArrayList<>();

    public Conversation(String convName, ArrayList<User> usersWrite, ArrayList<User>
            usersRead, ArrayList<Message> convMessages){
        conversationName = convName;
        conversationMessages = convMessages;
        writeAccessUsers = usersWrite;
        readAccessUsers = usersRead;
    }

    public void addUserToWrite(User user){
        writeAccessUsers.add(user);
    }

    public void addUsersToRead(User user){
        readAccessUsers.add(user);
    }

    public void changeConversationName(String newName){
        conversationName = newName;
    }

    public boolean addMessage(Message message){
        if (conversationMessages.contains(message)){
            System.out.println("Message has already been added");
            return false;
        }
        else{
        conversationMessages.add(message);
        return true;
        }
    }

    public boolean deleteMessage(Message message){
        if (conversationMessages.contains(message)){
            conversationMessages.remove(message);
            return true;
        }
        else{
            return false;
        }
    }

}
