package messaging;

import user.User;

import java.util.ArrayList;
import java.util.UUID;

public class Conversation {
    private String conversationName; // either group chat or 2 person chat
    private ArrayList<Message> conversationMessages = new ArrayList<>();
    private ArrayList<UUID> writeAccessUsers = new ArrayList<>();
    private ArrayList<UUID> readAccessUsers = new ArrayList<>();

    public Conversation(String convName, ArrayList<UUID> usersWrite, ArrayList<UUID>
            usersRead, ArrayList<Message> convMessages){
        conversationName = convName;
        conversationMessages = convMessages;
        writeAccessUsers = usersWrite;
        readAccessUsers = usersRead;
    }

    public void addUserToWrite(UUID user){
        writeAccessUsers.add(user);
    }

    public void addUsersToRead(UUID user){
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

    public String NameOfConversation(){
        return conversationName;
    }

}
