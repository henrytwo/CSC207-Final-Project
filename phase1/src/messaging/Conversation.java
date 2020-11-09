package messaging;

import user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Conversation {
    private String conversationName; // either group chat or 2 person chat
    private Set<Message> conversationMessages = new HashSet<>();
    private Set<UUID> writeAccessUsers = new HashSet<>();
    private Set<UUID> readAccessUsers = new HashSet<>();

    public Conversation(String convName, Set<UUID> usersWrite, Set<UUID>
            usersRead, Set<Message> convMessages){
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

    public String getConversationName(){
        return conversationName;
    }

}
